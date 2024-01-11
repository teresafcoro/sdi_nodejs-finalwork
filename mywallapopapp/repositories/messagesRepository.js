module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getMessages: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("sdi-2324-entrega2-505");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            return await messagesCollection.find(filter, options).toArray();
        } catch (error) {
            throw (error);
        }
    }, addMessage: function (message, callbackFunction) {
        try {
            this.mongoClient.connect(this.app.get('connectionStrings'), function (err, dbClient) {
                if (err) {
                    callbackFunction(null)
                } else {
                    const database = client.db("sdi-2324-entrega2-505");
                    const collectionName = 'messages';
                    const messagesCollection = database.collection(collectionName);
                    messagesCollection.insertOne(message)
                        .then(result => callbackFunction(result.insertedId))
                        .then(() => dbClient.close())
                        .catch(err => callbackFunction({error: err.message}));
                }
            });
        } catch (error) {
            throw (error);
        }
    }, markAsReadMessage: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("sdi-2324-entrega2-505");
            const collectionName = 'messages';
            const messagesCollection = database.collection(collectionName);
            const updateObj = {$set: {isReaded: true}};
            const result = await messagesCollection.updateOne(filter, updateObj, options);
            return result;
        } catch (error) {
            throw (error);
        }
    }, deleteMessage: async function (offerId, callback) {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("sdi-2324-entrega2-505");
        const offerCollection = database.collection('messages');
        await offerCollection.deleteOne({_id: offerId}).then((result) => {
            callback(result.deletedCount === 1);
        });
    }
}