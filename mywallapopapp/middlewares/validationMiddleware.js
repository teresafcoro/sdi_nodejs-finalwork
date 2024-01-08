const {check, query} = require("express-validator");
module.exports = {
    validateAddOfferFields: function () {
        return [
            check('title', 'El título es obligatorio').notEmpty(),
            check('description', 'La descripción es obligatoria').notEmpty(),
            check('price', 'El precio es obligatorio').notEmpty(),
            check('price', 'El precio debe ser un número').isNumeric(),
            check('price', 'El precio debe ser mayor que 0').isFloat({min: 0}),
            check('title', 'El título debe tener como máximo 50 caracteres').isLength({max: 50}),
            check('description', 'La descripción debe tener como máximo 500 caracteres').isLength({max: 500})
        ];
    }, validateRemoveOfferFields: function () {
        return [
            query('offerId', 'El id de la oferta es obligatorio').trim().notEmpty()
        ];
    }
}