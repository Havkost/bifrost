let express = require('express');
let router = express.Router();

// Define devices
let devices = {
  lightbulb: {power: false, brightness: 100}, // Brightness = [0;100]
  display: {content: ""},
  thermometer: {temperature: 20},
  motionSensor: {presence: false, lastUpdated: 0},
}

let SSEconnections = [];

// SSE Helper functions
const connectSSE = (res) => {
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Connection', 'keep-alive');
  res.flushHeaders(); // flush the headers to establish SSE with client
}

const updateSSEs = (key, device) => {
  let update = {};
  update[key] = device;
  const json = JSON.stringify(update);
  for(let res of SSEconnections){
    res.write(`data: ${json}\n\n`);
  }
}

// Frontend
router.get('/', function(req, res, next) {
  res.render('index', { title: 'IoT Simulator' });
});

router.get('/stateSSE', (req, res) => {
  connectSSE(res);
  SSEconnections.push(res);
  console.log('SSE connections:', SSEconnections.length);

  res.on('close', () => {
      console.log('Client disconnected');
      SSEconnections.pop(res);
      res.end();
  });
});

// TODO: flyt specifikke devices til deres egen routes fil
// Backend API
// LIGHTBULB
router.post('/lightbulb', function(req, res, next) {
  let brightness = req.body?.brightness;
  let power = req.body?.power;
  
  if(brightness != null) {
    brightness = parseInt(brightness);
    if(!Number.isInteger(brightness)) return res.status(400).send('Brightness must be a whole number.');
    if(brightness < 0 || brightness > 100) return res.status(400).send('Brightness must be between 0 and 100.');
    devices.lightbulb.brightness = brightness;
  }
  
  if(power != null) {
    if(typeof power !== 'boolean') return res.status(400).send('Power must be a boolean.');
    devices.lightbulb.power = power;
  }
  
  updateSSEs('lightbulb', devices.lightbulb);
  console.log('Lightbulb:', devices.lightbulb);
  
  res.status(204).send();
});

router.get('/lightbulb', function(req, res, next) {
  res.send(devices.lightbulb);
});

// LCD DISPLAY
router.post('/display', function(req, res, next) {
  let content = req.body?.content;

  if(content === null) return res.status(400).send('Content was not found!');
  content = toString(content);
  if(content.length > 150) return res.status(400).send('Text is too large for the display!'); // max 150 characters in string
  devices.display.content = content;

  updateSSEs('display', devices.display);
  console.log('Display:', devices.display);

  res.status(204).send();
});

router.get('/display', function(req, res, next) {
  res.send(devices.display);
});


// THERMOMETER
router.post('/thermometer', function(req, res, next) {
  //TODO

});

router.get('/thermometer', function(req, res, next) {
  res.send(devices.thermometer);
});


// MOTION SENSOR
router.post('/motionsensor', function(req, res, next) {
  //TODO

});

router.get('/motionsensor', function(req, res, next) {
  res.send(devices.motionSensor);
});

module.exports = router;
