module.exports = function (app, offersRepository) {
    app.get('/shop', function (req, res) {
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
            res.render("shop.twig", response);
        }).catch(() => {
            res.redirect("/shop" +
                "?message=Se produjo un error al listar las ofertas" + "&messageType=alert-danger");
        });
    });
    app.get('/offers/add', function (req, res) {
        res.render('offers/add.twig', {sessionUser: req.session.user});
    });
    app.post('/offers/add', function (req, res) {
        if (req.body.title && req.body.title !== ''
            && req.body.detail && req.body.detail !== '' && req.body.price > 0) {
            let {title, detail, price} = req.body;
            const offer = {
                title,
                detail,
                publicationDate: new Date(),
                price,
                seller: req.session.user
            };
            offersRepository.insertOffer(offer).then(offer => {
                if (offer)
                    res.render('users/myOffers.twig');
            }).catch(() => {
                res.redirect("/offers/add" +
                    "?message=No se pudo dar de alta la oferta" + "&messageType=alert-danger");
            });
        } else
            res.redirect("/offers/add" +
                "?message=Datos incorrectos" + "&messageType=alert-danger");
    });
    app.get('offers/myoffers', function (req, res) {
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        let filter = {seller: req.session.user};
        offersRepository.getUsersOffersPg(filter, {}, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let response = {offers: result.offers, pages: pages, currentPage: page};
            res.render('offers/myOffers.twig', response);
        }).catch(() => {
            res.redirect("/offers/myoffers" +
                "?message=No se pudieron listar tus ofertas" + "&messageType=alert-danger");
        });
    });
    app.get('/offers/delete/:id', function (req, res) {
        let filter = {_id: ObjectId(req.params.id)};
        offersRepository.deleteOffer(filter, {}).then(result => {
            if (result === null || result.deletedCount === 0)
                res.redirect("/offers/myoffers" +
                    "?message=No se pudo eliminar la oferta" + "&messageType=alert-danger");
            else
                res.redirect("/offers/myoffers");
        }).catch(() => {
            res.redirect("/offers/myoffers" +
                "?message=No se pudo eliminar la oferta" + "&messageType=alert-danger");
        });
    });
    app.get('offers/buy/:id', function (req, res) {
        let offerId = ObjectId(req.params.id);
        let shop = {user: req.session.user, offerId: offerId};
        offersRepository.buyOffer(shop, function (shopId) {
            if (shopId == null) {
                res.redirect("/shop" +
                    "?message=Error al realizar la compra" + "&messageType=alert-danger");
            } else {
                res.redirect("/purchases");
            }
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
                res.render("purchase.twig", {offers: offers});
            }).catch(error => {
                res.redirect("/shop" +
                    "?message=Se ha producido un error al listar tus ofertas compradas"
                    + "&messageType=alert-danger");
            });
        }).catch(error => {
            res.redirect("/shop" +
                "?message=Se ha producido un error al listar tus ofertas compradas"
                + "&messageType=alert-danger");
        });
    });
}