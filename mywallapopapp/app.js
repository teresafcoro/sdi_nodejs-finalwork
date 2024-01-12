let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');

let app = express();

// Access-Control-Allow-Origin
app.use(function (req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Credentials", "true");
    res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token");
    // Debemos especificar todas las headers que se aceptan. Content - Type , token
    next();
});

// JWT
let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Express session
let expressSession = require('express-session');
app.use(expressSession({secret: 'abcdefg', resave: true, saveUninitialized: true}));

// Body parser
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

// Cifrado de passwords con Crypto
let crypto = require('crypto');
app.set('uploadPath', __dirname)
app.set('clave', 'abcdefg');
app.set('crypto', crypto);

// Acceso MongoDB
const {MongoClient} = require("mongodb");
// const url = 'mongodb+srv://admin:wqHXfPJz3vfbTsZb@mywallapopapp.jta7npq.mongodb.net/?retryWrites=true&w=majority';
const url = 'mongodb://localhost:27017'; // local
app.set('connectionStrings', url);

// Middlewares
const customLogger = require('./middlewares/loggerMiddleware');
app.use("/offers/", customLogger.loggerRouter);

// User Session Router
const userSessionRouter = require('./routes/userSessionRouter');
app.use("/offers/shop", userSessionRouter);
app.use("/offers/add", userSessionRouter);
app.use("/offers/myOffers", userSessionRouter);
app.use("/offers/buy", userSessionRouter);
app.use("/offers/purchases", userSessionRouter);

// Admin Session Router
const adminSessionRouter = require('./routes/adminSessionRouter');
app.use("/admin", adminSessionRouter);
app.use("/logs/delete/all", adminSessionRouter);
app.use("/users/delete", adminSessionRouter);
app.use("/users/list", adminSessionRouter);

// User Seller Router
const userSellerRouter = require('./routes/userSellerRouter');
app.use("/offers/delete", userSellerRouter);
app.use("/offers/featured", userSellerRouter);

// User Token Router
const userTokenRouter = require('./routes/userTokenRouter');
app.use("/api/v1.0/offers", userTokenRouter);
app.use("/api/v1.0/messages", userTokenRouter);
app.use("/api/v1.0/conversations", userTokenRouter);

// Rutas

const usersRepository = require("./repositories/usersRepository.js");
usersRepository.init(app, MongoClient);
require("./routes/users.js")(app, usersRepository);

const offersRepository = require("./repositories/offersRepository.js");
offersRepository.init(app, MongoClient);
require("./routes/offers.js")(app, offersRepository, usersRepository);

const conversationsRepository = require("./repositories/conversationsRepository.js");
conversationsRepository.init(app, MongoClient);

const messagesRepository = require("./repositories/messagesRepository.js");
messagesRepository.init(app, MongoClient);

require('./routes/api/myWallapopAPIv1.0.js')
(app, usersRepository, offersRepository, conversationsRepository, messagesRepository);

let logsRepository = require("./repositories/logsRepository.js");
logsRepository.init(app, MongoClient);
require("./routes/logsRouter.js")(app, logsRepository);

let indexRouter = require('./routes/index');

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.render('error');
});

module.exports = app;
