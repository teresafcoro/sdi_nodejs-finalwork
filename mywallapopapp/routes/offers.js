const {ObjectId} = require("mongodb");
module.exports = function (app, offersRepository) {
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
        offersRepository.findOffer(filter, {}).then(offer => {
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
    app.get('/offers/shop', function (req, res) {
        let filter = {};
        let options = {sort: {title: 1}};
        if (req.query.search != null && typeof (req.query.search) != "undefined" && req.query.search !== "") {
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
        offersRepository.getOffersPg(filter, options, page).then(result => {
            let lastPage = result.total / 5;
            if (result.total % 5 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let response = {offers: result.offers, pages: pages, currentPage: page};
            res.render("offers/shop.twig", response);
        }).catch(() => {
            res.redirect("/offers/shop" +
                "?message=Se produjo un error al listar las ofertas" + "&messageType=alert-danger");
        });
    });
    app.get('/offers/buy/:id', function (req, res) {
        let offerId = ObjectId(req.params.id);
        let user = req.session.user;
        let filter = {_id: offerId};
        offersRepository.findOffer(filter, {}).then(offer => {
            if (offer.seller !== user && offer.price <= user.wallet) {
                let shop = {user: user, offerId: offerId};
                offersRepository.buyOffer(shop, function (shopId) {
                    if (shopId !== null) {
                        res.redirect("/offers/shop" +
                            "?message=Error al realizar la compra" + "&messageType=alert-danger");
                    } else {
                        user.wallet -= offer.price; // TO-DO?
                        offer.isSold = true;
                        offersRepository.updateOffer(offer, filter, {}).then(result => {
                            if (result !== null)
                                res.redirect("/offers/purchases");
                            else
                                res.redirect("/offers/shop" +
                                    "?message=Error al actualizar la oferta" + "&messageType=alert-danger");
                        });

                    }
                });
            } else
                res.redirect("/offers/shop" +
                    "?message=Es su oferta o saldo insuficiente" + "&messageType=alert-danger");
        });
    });
    app.get('/offers/purchases', function (req, res) {
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        let filter = {user: req.session.user};
        let options = {projection: {_id: 0, offerId: 1}};
        offersRepository.getPurchasesPg(filter, options, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let purchases = [];
            for (let i = 0; i < result.purchaseIds.length; i++)
                purchases.push(result.purchaseIds[i].offerId);
            let filter = {"_id": {$in: purchases}};
            let options = {sort: {title: 1}};
            offersRepository.getOffers(filter, options).then(offers => {
                res.render("offers/purchase.twig", {offers: offers});
            }).catch(() => {
                res.redirect("/offers/shop" +
                    "?message=Se ha producido un error al listar tus ofertas compradas"
                    + "&messageType=alert-danger");
            });
        }).catch(() => {
            res.redirect("/offers/shop" +
                "?message=Se ha producido un error al listar tus ofertas compradas"
                + "&messageType=alert-danger");
        });
    });
    app.get('/offers/featured/:id', function (req, res) {
        let offerId = ObjectId(req.params.id);
        let filter = {_id: offerId};
        let user = req.session.user;
        offersRepository.findOffer(filter, {}).then(offer => {
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
}