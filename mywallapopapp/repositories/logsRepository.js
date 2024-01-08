module.exports = {
    mongoClient: null,
    app: null,
    init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, addNewLog: async function (log) {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("myWallapop");
        const loggingCollection = database.collection('logs');
        await loggingCollection.insertOne(log).then(() => {
            client.close();
        });
    }, filterLogByType: async function (type) {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("myWallapop");
        const loggingCollection = database.collection('logs');
        const filteredLogs = await loggingCollection
            .find({type: type})
            .sort({timestamp: -1}) // Ordenamos por fecha descendente
            .toArray();
        return filteredLogs;
    }, findAllLogs: async function () {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("myWallapop");
        const loggingCollection = database.collection('logs');
        const allRegisteredLogs = await loggingCollection
            .find({})
            .sort({timestamp: -1}) // Ordenamos por fecha descendente
            .toArray();
        return allRegisteredLogs;
    }, deleteAllLogs: async function (callback) {
        const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
        const database = client.db("myWallapop");
        const loggingCollection = database.collection('logs');
        await loggingCollection.deleteMany({}).then((result) => {
            callback(result.deletedCount === 1);
        });
        client.close();
    }
}