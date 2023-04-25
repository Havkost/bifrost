let express = require('express');
let router = express.Router();

// Define devices
let lightbulb = {power: 0} // Power = [0;100]
let motionSensor = {timeSinceMovement: 9999}

let SSEdevices = [lightbulb];
let SSEconnections = [];

// SSE Helper functions
const connectSSE = (res) => {
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Connection', 'keep-alive');
  res.flushHeaders(); // flush the headers to establish SSE with client
}

const updateSSEs = () => {
  for(let res of SSEconnections){
    res.write('TESTING');
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

// Backend API
router.post('/lightbulb', function(req, res, next) {
  let power = req.query.power;
  
  if(power == null) return res.status(400).send('No power specified.');
  power = parseInt(power);
  if(!Number.isInteger(power)) return res.status(400).send('Power must be a whole number.');
  if(power < 0 || power > 100) return res.status(400).send('Power must be between 0 and 100.');

  lightbulb.power = power;
  updateSSEs();
  
  res.status(204).send();
});

module.exports = router;
