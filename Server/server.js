var express = require('express');
var morgan = require('morgan');
var bodyParser = require('body-parser');

var hypertrack = require('hypertrack-node').hypertrack('sk_test_fd7776598c620489d5b89dac65ec1c4fce394371');

var app = express();
app.use(morgan('dev'));

var port = 3000;

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

app.all(function(req,res,next){
  res.setHeader('Content-Type', 'application/json');
  res.setHeader('Authorization', 'token sk_test_fd7776598c620489d5b89dac65ec1c4fce394371');

  res.setHeader('Access-Control-Allow-Origin', '*');

  // Disable caching so we'll always get the latest comments.
  res.setHeader('Cache-Control', 'no-cache');

  next();
})

app.get('/', function(req, res){
    console.log(req.body);
    console.log(hypertrack);
    
    res.end('test');
})

app.listen(port, function() {
  console.log('Express server listening on port ' + port);
});
