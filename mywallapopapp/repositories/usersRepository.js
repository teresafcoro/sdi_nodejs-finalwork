module.exports = {
    mongoClient: null, app: null, init: function (app, mongoClient) {
        this.mongoClient = mongoClient;
        this.app = app;
    }, getUser: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            return await usersCollection.findOne(filter, options);
        } catch (error) {
            throw (error);
        }
    }, insertUser: async function (user) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            return await usersCollection.insertOne(user);
        } catch (error) {
            throw (error);
        }
    }, getUsersPg: async function (filter, options, page) {
        try {
            const limit = 4;
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            const usersCollectionCount = await usersCollection.count();
            const cursor = usersCollection.find(filter, options).skip((page - 1) * limit).limit(limit);
            const users = await cursor.toArray();
            return {users: users, total: usersCollectionCount};
        } catch (error) {
            throw (error);
        }
    }, deleteUsers: async function (filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            return await usersCollection.deleteMany(filter, options);
        } catch (error) {
            throw (error);
        }
    }, updateUser: async function (user, filter, options) {
        try {
            const client = await this.mongoClient.connect(this.app.get('connectionStrings'));
            const database = client.db("myWallapop");
            const collectionName = 'users';
            const usersCollection = database.collection(collectionName);
            return await usersCollection.updateOne(filter, {$set: user}, options);
        } catch (error) {
            throw(error);
        }
    }
};