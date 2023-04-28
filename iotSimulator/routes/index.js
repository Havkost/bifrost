let express = require('express');
let router = express.Router();

// Define devices
let devices = {
  lightbulb: {power: false, brightness: 100}, // Brightness = [0;100]
  display: {content: "Standard tekst"},
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

router.get('/devices', function(req, res, next) {
  res.send(devices);
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

  if(content == null) return res.status(400).send('No content was supplied.');
  content = ""+content; // Convert to string
  if(content.length > 30) return res.status(400).send('Text is too large for the display!'); // max 150 characters in string
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
  let temperature = req.body?.temperature;
  if(temperature == null) return res.status(400).send('No temperature was supplied.');
  temperature = parseFloat(temperature);
  if(Number.isNaN(temperature)) return res.status(400).send('Temperature must be a number.');
  if(temperature < -50 || temperature > 50) res.status(400).send('Temperature must be between -50 to 50');

  devices.thermometer.temperature = temperature;

  res.status(204).send();
});

router.get('/thermometer', function(req, res, next) {
  res.send(devices.thermometer);
});


// MOTION SENSOR
router.post('/motionsensor', function(req, res, next) {
  let presence = req.body?.presence;
  let lastUpdated = req.body?.lastUpdated;
  
  if(presence != null) {
    if(typeof(presence) !== 'boolean') return res.status(400).send('Presence must be a boolean.');
    devices.motionSensor.presence = presence;
  }

  console.log('Presence:', devices.motionSensor.presence);
  console.log('Last Updated:', devices.motionSensor.lastUpdated);
  
  res.status(204).send();
});

router.get('/motionsensor', function(req, res, next) {
  res.send(devices.motionSensor);
});

module.exports = router;
