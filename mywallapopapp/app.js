let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');

let app = express();

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

// User Session Router
const userSessionRouter = require('./routes/userSessionRouter');
app.use("/offers/add", userSessionRouter);
app.use("/shop/", userSessionRouter);

// User Seller Router
const userSellerRouter = require('./routes/userSellerRouter');
app.use("/offers/edit", userSellerRouter);
app.use("/offers/delete", userSellerRouter);

// Rutas
const usersRepository = require("./repositories/usersRepository.js");
usersRepository.init(app, MongoClient);
require("./routes/users.js")(app, usersRepository);

const offersRepository = require("./repositories/offersRepository.js");
offersRepository.init(app, MongoClient);
require("./routes/offers.js")(app, offersRepository);

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
