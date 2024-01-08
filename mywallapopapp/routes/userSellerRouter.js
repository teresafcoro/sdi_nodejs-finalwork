const express = require('express');
const path = require("path");
const {ObjectId} = require("mongodb");
const offersRepository = require("../repositories/offersRepository");
const userSellerRouter = express.Router();
userSellerRouter.use(function (req, res, next) {
    let offerId = path.basename(req.originalUrl);
    let filter = {_id: ObjectId(offerId)};
    offersRepository.getOffer(filter, {}).then(offer => {
        if (req.session.user && offer.seller === req.session.user.email) {
            next();
        } else {
            res.redirect("/offers/shop");
        }
    }).catch(error => {
        res.redirect("/offers/shop");
    });
});
module.exports = userSellerRouter;