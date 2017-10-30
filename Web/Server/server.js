var express = require('express');
var morgan = require('morgan');
var bodyParser = require('body-parser');
var http = require('http');

var hypertrack = require('hypertrack-node').hypertrack('sk_test_fd7776598c620489d5b89dac65ec1c4fce394371');

var app = express();
app.use(morgan('dev'));

var port = 3000;

var coords = "28.5401324,77.257066";
var c = {
  "type": "Point",
  "coordinates": [28.5401324,77.257066]
}

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
    place = {
        'address': "DTU Delhi IN"
    }

    var b = hypertrack.users.nearby({
      "location":"77.257066,28.5401324"
    }).then(function(users){
      //console.log(users);
      var a = hypertrack.actions.create({
          "expected_place": place,
          "scheduled_at":"2016-03-09T07:00:00.00Z"
      }).then(function(f){

        console.log(f.id);

        hypertrack.users.assignAction("78ac68b1-d25c-4a44-a2bf-7dc5fc94dadb", {"action_ids": ["47a4a0ab-090e-4470-8694-61a55778b0e4"]})

      });
    })

    res.end('test');
})

app.listen(port, function() {
  console.log('Express server listening on port ' + port);
});
