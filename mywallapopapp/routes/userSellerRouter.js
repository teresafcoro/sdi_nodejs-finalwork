const express = require('express');
const path = require("path");
const {ObjectId} = require("mongodb");
const offersRepository = require("../repositories/offersRepository");
const userSellerRouter = express.Router();
userSellerRouter.use(function (req, res, next) {
    console.log("userSellerRouter");
    let offerId = path.basename(req.originalUrl);
    let filter = {_id: ObjectId(offerId)};
    offersRepository.findOffer(filter, {}).then(song => {
        if (req.session.user && offer.seller === req.session.user) {
            next();
        } else {
            res.redirect("/shop");
        }
    }).catch(error => {
        res.redirect("/shop");
    });
});
module.exports = userAuthorRouter;