const express = require('express');
const adminSessionRouter = express.Router();
const ADMIN_KIND = "Administrador";
adminSessionRouter.use(function (req, res, next) {
    if (req.session.user && req.session.user.kind === ADMIN_KIND) {
        next();
    } else {
        // Si el usuario no es admin se muestra un mensaje
        res.redirect("/users/login" + "?message=Acción prohibida" + "&messageType=alert-danger");
    }
});
module.exports = adminSessionRouter;