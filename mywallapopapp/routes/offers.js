const {ObjectId} = require("mongodb");
module.exports = function (app, offersRepository, usersRepository) {
    /**
     * Renderizado a la vista de creación de ofertas
     */
    app.get('/offers/add', function (req, res) {
        res.render('offers/add.twig', {sessionUser: req.session.user});
    });
    /**
     * Dar de alta una nueva oferta
     */
    app.post('/offers/add', function (req, res) {
        if (req.body.title && req.body.title !== ''
            && req.body.detail && req.body.detail !== '' && req.body.price > 0) {
            let {title, detail, price, featured} = req.body;
            price = parseInt(price);
            if (featured && req.session.user.wallet < 20)
                res.redirect("/offers/add" +
                    "?message=Dinero insuficiente para destacar la oferta (precio: 20€)"
                    + "&messageType=alert-danger");
            else if (featured && req.session.user.wallet >= 20)
                req.session.user.wallet -= 20;
            const offer = {
                title,
                detail,
                publicationDate: new Date(),
                price,
                featured: featured,
                isSold: false,
                seller: req.session.user.email
            };
            offersRepository.insertOffer(offer).then(offer => {
                if (offer)
                    res.render('offers/myOffers.twig', {sessionUser: req.session.user});
            }).catch(() => {
                res.redirect("/offers/add" +
                    "?message=No se pudo dar de alta la oferta" + "&messageType=alert-danger");
            });
        } else
            res.redirect("/offers/add" +
                "?message=Datos incorrectos" + "&messageType=alert-danger");
    });
    /**
     * Listado de ofertas propias
     */
    app.get('/offers/myOffers', function (req, res) {
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        let filter = {seller: req.session.user.email};
        offersRepository.getUserOffersPg(filter, {}, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let response = {
                offers: result.offers,
                pages: pages,
                currentPage: page,
                sessionUser: req.session.user
            };
            res.render('offers/myOffers.twig', response);
        }).catch(() => {
            res.redirect("/offers/myOffers" +
                "?message=No se pudieron listar tus ofertas" + "&messageType=alert-danger");
        });
    });
    /**
     * Dar de baja una oferta
     */
    app.get('/offers/delete/:id', function (req, res) {
        let filter = {_id: ObjectId(req.params.id)};
        offersRepository.getOffer(filter, {}).then(offer => {
            if (offer && offer.isSold)
                res.redirect("/offers/myOffers" +
                    "?message=Oferta vendida, no se puede eliminar" + "&messageType=alert-danger");
            else {
                offersRepository.deleteOffer(filter, {}).then(result => {
                    if (result === null || result.deletedCount === 0)
                        res.redirect("/offers/myOffers" +
                            "?message=No se pudo eliminar la oferta" + "&messageType=alert-danger");
                    else
                        res.render('offers/myOffers.twig', {sessionUser: req.session.user});
                }).catch(() => {
                    res.redirect("/offers/myOffers" +
                        "?message=No se pudo eliminar la oferta" + "&messageType=alert-danger");
                });
            }
        }).catch(error => {
            console.error("Error finding offer:", error);
            res.redirect("/offers/myOffers" +
                "?message=No se pudo encontrar la oferta" + "&messageType=alert-danger");
        });
    });
    /**
     * Ofertas del sistema con sistema de búsqueda por título
     */
    app.get('/offers/shop', function (req, res) {
        let filter = {};
        if (req.query.search != null &&
            typeof (req.query.search) != "undefined" &&
            req.query.search !== "") {
            // Búsqueda por título insensible a mayúsculas y minúsculas
            filter = {
                "title": {
                    $regex: ".*" + req.query.search + ".*",
                    $options: "i"
                }
            };
        }
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        offersRepository.getOffersPg(filter, {}, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let response = {
                offers: result.offers,
                pages: pages,
                currentPage: page,
                sessionUser: req.session.user,
                search: req.query.search
            }; // Se añade el parámetro de búsqueda al objeto de respuesta
            res.render("offers/shop.twig", response);
        }).catch(() => {
            res.redirect("/offers/shop" +
                "?message=Se produjo un error al listar las ofertas" + "&messageType=alert-danger");
        });
    });
    /**
     * Comprar una oferta
     */
    app.get('/offers/buy/:id', function (req, res) {
        let offerId = ObjectId(req.params.id);
        let user = req.session.user;
        let filter = {_id: offerId};
        offersRepository.getOffer(filter, {}).then(offer => {
            if (offer.seller !== user.email && offer.price <= user.wallet) {
                offer.isSold = true;
                let shop = {buyer: user.email, offer: offer};
                offersRepository.buyOffer(shop, function (shopId) {
                    if (shopId === null)
                        res.redirect("/offers/shop" +
                            "?message=Error al realizar la compra" + "&messageType=alert-danger");
                    else {
                        user.wallet -= offer.price;
                        req.session.user = user;
                        updateUser(user); // With the new wallet value
                        offersRepository.updateOffer(offer, filter, {}).then(result => {
                            if (result !== null)
                                res.redirect("/offers/purchases");
                        }).catch(() => {
                            res.redirect("/offers/shop" +
                                "?message=Error al actualizar la oferta" + "&messageType=alert-danger");
                        });
                    }
                });
            } else if (offer.seller === user.email)
                res.redirect("/offers/shop" +
                    "?message=¡Es su oferta!" + "&messageType=alert-danger");
            else
                res.redirect("/offers/shop" +
                    "?message=Saldo insuficiente para realizar la compra" + "&messageType=alert-danger");
        }).catch(() => {
            res.redirect("/offers/shop" +
                "?message=Error al obtener la oferta" + "&messageType=alert-danger");
        });
    });
    /**
     * Ver el listado de ofertas compradas
     */
    app.get('/offers/purchases', function (req, res) {
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        let filter = {"buyer": req.session.user.email};
        offersRepository.getPurchasesPg(filter, {}, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let offers = [];
            for (let i = 0; i < result.purchases.length(); i++)
                offers.push(result.purchases[i].offer);
            let response = {
                offers: offers,
                pages: pages,
                currentPage: page,
                sessionUser: req.session.user
            };
            res.render("offers/purchases.twig", response);
        }).catch(() => {
            res.redirect("/offers/purchases" +
                "?message=Se ha producido un error al listar tus ofertas compradas"
                + "&messageType=alert-danger");
        });
    });
    app.get('/offers/featured/:id', function (req, res) {
        let offerId = ObjectId(req.params.id);
        let filter = {_id: offerId};
        let user = req.session.user;
        offersRepository.getOffer(filter, {}).then(offer => {
            if (user.wallet >= 20) { // coste de destacar una oferta
                user.wallet -= 20;
                offer.featured = true;
                offersRepository.updateOffer(offer, filter, {}).then(result => {
                    if (result !== null)
                        res.redirect("/offers/myOffers");
                    else
                        res.redirect("/offers/myOffers" +
                            "?message=Error al destacar la oferta" + "&messageType=alert-danger");
                });
            } else
                res.redirect("/offers/myOffers" +
                    "?message=Saldo insuficiente para destacar la oferta" + "&messageType=alert-danger");
        });
    });

    function updateUser(user) {
        usersRepository.getUser({email: user.email}, {}).then(dbUser => {
            if (dbUser !== null) {
                const updateField = {wallet: user.wallet};
                usersRepository.updateUser(updateField, {_id: dbUser._id}, {}).then(result => {
                    if (result === null)
                        res.redirect("/offers/shop" +
                            "?message=Error al actualizar el usuario" + "&messageType=alert-danger");
                });
            }
        }).catch(() => {
            res.redirect("/offers/shop" +
                "?message=Error al actualizar el usuario" + "&messageType=alert-danger");
        });
    }
}