module.exports = function (app, offersRepository) {
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
}