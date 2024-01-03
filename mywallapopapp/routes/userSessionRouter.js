const express = require('express');
const userSessionRouter = express.Router();
userSessionRouter.use(function (req, res, next) {
    if (req.session.user && req.session.user !== "admin@email.com") { // Usuario Estándar
        next();
    } else {
        res.redirect("/users/login");
    }
});
module.exports = userSessionRouter;