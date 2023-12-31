module.exports = function (app, usersRepository) {
    app.get('/users', function (req, res) {
        let page = parseInt(req.query.page);
        if (typeof req.query.page === "undefined" || req.query.page === null || req.query.page === "0")
            page = 1;
        let filter = {kind: 'Usuario Estándar'};
        usersRepository.getUsersPg(filter, {}, page).then(result => {
            let lastPage = result.total / 4;
            if (result.total % 4 > 0)
                lastPage = lastPage + 1;
            let pages = [];
            for (let i = page - 2; i <= page + 2; i++) {
                if (i > 0 && i <= lastPage)
                    pages.push(i);
            }
            let response = {users: result.users, pages: pages, currentPage: page};
            res.render('users/list.twig', response);
        }).catch(() => {
            res.redirect("/users" +
                "?message=No se pudieron listar los usuarios" + "&messageType=alert-danger");
        });
    });
    app.get('/users/signup', function (req, res) {
        res.render('users/signup.twig');
    });
    app.post('/users/signup', function (req, res) {
        const parsedDate = new Date(req.body.dateOfBirth);
        if (parsedDate.toString() === 'Invalid Date')
            res.redirect("/users/signup" + "?message=Fecha inválida" + "&messageType=alert-danger");
        else if (req.body.password !== req.body.verifyPassword)
            res.redirect("/users/signup" +
                "?message=Las contraseñas no coinciden" + "&messageType=alert-danger");
        else {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            const {email, name, surname, dateOfBirth} = req.body;
            const wallet = 100; // cuenta de dinero con 100€ iniciales
            let user = {
                email,
                name,
                surname,
                dateOfBirth,
                wallet,
                kind: "Usuario Estándar",
                password: securePassword
            };
            usersRepository.findUser({email: user.email}, {}).then(dbUser => {
                if (dbUser === null) {
                    usersRepository.insertUser(user).then(user => {
                        req.session.user = user.email;
                        res.render('offers/myOffers.twig', {sessionUser: req.session.user});
                    });
                } else {
                    res.redirect("/users/signup" +
                        "?message=El usuario ya se encuentra registrado en el sistema" +
                        "&messageType=alert-danger");
                }
            }).catch(() => {
                res.redirect("/users/signup" +
                    "?message=Se ha producido un error al registrar el usuario" +
                    "&messageType=alert-danger");
            });
        }
    });
    app.get('/users/login', function (req, res) {
        res.render("users/login.twig");
    });
    app.post('/users/login', function (req, res) {
        let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        let filter = {
            email: req.body.email,
            password: securePassword
        };
        usersRepository.findUser(filter, {}).then(user => {
            if (user == null) {
                req.session.user = null;
                res.redirect("/users/login"
                    + "?message=Email o contraseña incorrecta" + "&messageType=alert-danger");
            } else {
                req.session.user = user.email;
                if (user.kind === 'Administrador')
                    res.render('users/list.twig', {sessionUser: req.session.user});
                else
                    res.render('offers/myOffers.twig', {sessionUser: req.session.user});
            }
        }).catch(() => {
            req.session.user = null;
            res.redirect("/users/login"
                + "?message=Se ha producido un error al buscar el usuario" + "&messageType=alert-danger");
        });
    });
    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.render('users/login.twig', {sessionUser: req.session.user});
    });
    app.get('/users/delete', function (req, res) {
        let usersEmails = [];
        usersEmails = usersEmails.concat(req.body.userEmails);
        let filter = {email: {$in: usersEmails}};
        usersRepository.deleteUsers(filter, {}).then(result => {
            if (result === null || result.deletedCount === 0) {
                res.redirect("/users"
                    + "?message=No se ha podido realizar el borrado" + "&messageType=alert-danger");
            } else {
                res.redirect("/users"
                    + "?message=Borrado realizado correctamente" + "&messageType=alert-info");
            }
        }).catch(() => {
            res.redirect("/users"
                + "?message=No se ha podido realizar el borrado" + "&messageType=alert-danger");
        });
    });
}