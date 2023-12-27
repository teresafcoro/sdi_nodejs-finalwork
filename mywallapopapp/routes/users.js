module.exports = function (app, usersRepository) {
    app.get('/users', function (req, res) {
        usersRepository.getUsers().then(users => {
            res.render('users.twig', {users: users});
        }).catch(() => {
            res.redirect("/users" +
                "?message=No se pudieron listar los usuarios" + "&messageType=alert-danger");
        });
    });
    app.get('/users/signup', function (req, res) {
        res.render('signup.twig');
    });
    app.post('/users/signup', function (req, res) {
        if (req.body.password !== req.body.repitePassword)
            res.redirect("/users/signup" +
                "?message=Las contraseñas no coinciden" + "&messageType=alert-danger");
        else {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            const {email, nombre, apellidos, fecha} = req.body;
            let user = {
                email,
                nombre,
                apellidos,
                fecha,
                kind: "Usuario Estándar",
                password: securePassword
            };
            usersRepository.findUser({email: user.email}, {}).then(dbUser => {
                if (dbUser === null) {
                    usersRepository.insertUser(user).then(userId => {
                        res.redirect("/users/offers" +
                            "?message=Usuario registrado correctamente: " + userId +
                            "&messageType=alert-info");
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
        res.render("login.twig");
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
                res.redirect('/shop');
            }
        }).catch(() => {
            req.session.user = null;
            res.redirect("/users/login"
                + "?message=Se ha producido un error al buscar el usuario" + "&messageType=alert-danger");
        });
    });
    app.get('/users/logout', function (req, res) {
        req.session.user = null;
        res.redirect("/users/login"
            + "?message=El usuario se ha desconectado correctamente" + "&messageType=alert-info");
    });
    app.get('/users/delete/:id', function (req, res) {
        // TO-DO: Obtener los usuarios seleccionados para el borrado!!!!!!!!!!!!!!!!
        let filter = {};
        usersRepository.deleteUser(filter, {}).then(result => {
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