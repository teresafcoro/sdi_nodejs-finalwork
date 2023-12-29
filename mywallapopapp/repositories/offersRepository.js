module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, insertOffer: async function (offer) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            return await offersCollection.insertOne(offer);
        } catch (error) {
            throw (error);
        }
    }, getUsersOffersPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'offers';
            const offersCollection = database.collection(collectionName);
            const offersCollectionCount = await offersCollection.count();
            const cursor = offersCollection.find(filter, options).skip((page - 1) * limit).limit(limit);
            const offers = await cursor.toArray();
            return {offers: offers, total: offersCollectionCount};
        } catch (error) {
            throw (error);
        }
    }
};