const {ObjectId} = require("mongodb");
module.exports = function (app, usersRepository, offersRepository, conversationsRepository, messagesRepository) {
    /**
     * Identificación como usuario via token
     */
    app.post('/api/v1.0/users/login', function (req, res) {
        try {
            let securePassword = app.get("crypto").createHmac('sha256', app.get('clave'))
                .update(req.body.password).digest('hex');
            let filter = {
                email: req.body.email,
                password: securePassword
            };
            usersRepository.getUser(filter, {}).then(user => {
                if (user == null) {
                    res.status(401); // Unauthorized
                    res.json({
                        message: "usuario no autorizado",
                        authenticated: false
                    });
                } else {
                    let token = app.get('jwt').sign({user: user.email, time: Date.now() / 1000}, "secreto");
                    res.status(200);
                    res.json({
                        message: "usuario autorizado",
                        authenticated: true,
                        token: token
                    });
                }
            }).catch(() => {
                res.status(401);
                res.json({
                    message: "Se ha producido un error al verificar credenciales",
                    authenticated: false
                });
            });
        } catch (e) {
            res.status(500);
            res.json({
                message: "Se ha producido un error al verificar credenciales",
                authenticated: false
            });
        }
    });
    /**
     * Se destruye la sesión
     */
    app.get("/api/v1.0/logout", function (req, res) {
        app.get("jwt").destroy(req.headers.token);
        req.session.destroy();
        res.status(200);
        res.json({message: "logout"});
    });
    /**
     * Obtener el listado de ofertas disponibles de usuarios diferentes al identificado
     */
    app.get("/api/v1.0/offers", function (req, res) {
        try {
            let filter = {seller: {$ne: req.session.user.email}};
            offersRepository.getOffers(filter, {}).then(offers => {
                res.status(200);
                res.json({offers: offers});
            }).catch(() => {
                res.status(500);
                res.json({error: "Se ha producido un error al recuperar las ofertas."});
            });
        } catch (error) {
            res.status(500);
            res.json({error: "Se ha producido un error al buscar las ofertas."});
        }
    });
    /**
     * Enviar mensajes a una oferta
     */
    app.post('/api/v1.0/conversations/sendMessage', function (req, res) {
        let idConversation = ObjectId(req.body.idConversation);
        let idBuyer = res.user;
        let offerTitle = req.body.offerTitle;
        let idSeller = req.body.idSeller;
        let idOffer = req.body.idOffer;
        let text = req.body.text;
        let isReaded = false;
        let date = Date.now();
        let idSender = res.user;
        let idReceiver;
        if (res.user === idSeller)
            idReceiver = idBuyer;
        else
            idReceiver = idSeller;

        try {
            // Compruebo si existe esa conversacion
            let filter = {_id: ObjectId(idConversation)};
            conversationsRepository.getConversation(filter, {}).then(conversation => {
                try {
                    if (conversation) {
                        if (res.user === conversation.seller)
                            idReceiver = conversation.buyer;
                        else
                            idReceiver = conversation.seller;
                        let msg = {
                            idConversation: ObjectId(idConversation),
                            idSender: idSender,
                            idReceiver: idReceiver,
                            isReaded: isReaded,
                            text: text,
                            date: date
                        }
                        messagesRepository.addMessage(msg, function (messageId) {
                            if (messageId === null) {
                                res.status(409);
                                res.json({error: "No se ha podido añadir el mensaje."});
                            } else {
                                res.status(201);
                                res.json({
                                    message: "Mensaje añadido correctamente.",
                                    _id: messageId
                                });
                            }
                        });
                    } else {
                        let conv = {
                            buyer: idBuyer,
                            seller: idSeller,
                            offer: idOffer,
                            offerTitle: offerTitle
                        };
                        conversationsRepository.addConversation(conv, async function (conversationId) {
                            if (conversationId === null) {
                                res.status(409);
                                res.json({error: "No se ha podido crear la conversación."});
                            } else {
                                let msg = {
                                    idConversation: conversationId,
                                    idSender: idSender,
                                    idReceiver: idReceiver,
                                    isReaded: isReaded,
                                    text: text,
                                    date: date
                                }
                                messagesRepository.addMessage(msg, function (messageId) {
                                    if (messageId === null) {
                                        res.status(409);
                                        res.json({error: "No se ha podido añadir el mensaje."});
                                    } else {
                                        res.status(201);
                                        res.json({
                                            message: "Mensaje añadido correctamente.",
                                            _id: messageId
                                        });
                                    }
                                });
                            }
                        });
                    }
                } catch (error) {
                    res.status(500);
                    res.json({error: "Se ha producido un error al intentar añadir el mensaje: " + error});
                }
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error al intentar añadir el mensaje: " + e});
        }
    });
    /**
     * Obtener el listado de conversaciones
     */
    app.get('/api/v1.0/conversations', function (req, res) {
        let user = res.user;
        let filterBuyer = {buyer: user};
        let filterSeller = {seller: user};
        let conversations = [];
        try {
            conversationsRepository.getConversations(filterBuyer, {}).then(convs => {
                convs.forEach(c => {
                    conversations.push(c);
                });
                conversationsRepository.getConversations(filterSeller, {}).then(convs => {
                    convs.forEach(c => {
                        conversations.push(c);
                    })
                    res.status(200);
                    res.send({
                        conversations: conversations
                    });
                });
            });
        } catch (error) {
            console.log("Se ha producido un error al recuperar las conversaciones: " + error);
        }
    });
    /**
     * Obtener los mensajes de una conversación
     */
    app.get('/api/v1.0/messages/:id', function (req, res) {
        try {
            let filter = {idConversation: ObjectId(req.params.id)};
            messagesRepository.getMessages(filter, {}).then(messages => {
                res.status(200);
                res.send({messages: messages});
            }).catch(error => {
                res.status(500);
                res.json({error: "Se ha producido un error al recuperar las ofertas."});
            });
        } catch (error) {
            console.log("error", error);
        }
    });
    /**
     * Eliminar una conversación
     */
    app.delete('/api/v1.0/messages/:id', function (req, res) {
        try {
            let msgId = ObjectId(req.params.id);
            let filter = {_id: msgId};
            // Si la _id NO no existe, no crea un nuevo documento
            const options = {upsert: false};
            let actu;
            messagesRepository.deleteMessage(filter, options).then(result => {
                if (result.deletedCount === 0) {
                    res.status(404);
                    res.json({error: "ID inválido o no existe, no se ha borrado el registro."});
                } else {
                    res.status(200);
                    res.send(JSON.stringify(result));
                }
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error al intentar modificar la canción: " + e});
        }
    });
    /**
     * Marcar mensaje como leído
     */
    app.put('/api/v1.0/messages/:id', function (req, res) {
        try {
            let msgId = ObjectId(req.params.id);
            let filter = {_id: ObjectId(msgId)};
            // Si la _id NO no existe, no crea un nuevo documento
            const options = {upsert: false};
            let actu;
            messagesRepository.markAsReadMessage(filter, options).then(result => {
                actu = result;
            });
        } catch (e) {
            res.status(500);
            res.json({error: "Se ha producido un error al intentar modificar la canción: " + e});
        }
    });
}