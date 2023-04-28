const eventSource = new EventSource('/stateSSE');
const lightbulbElement = document.getElementById('lightbulb');

const deviceElements = {
  lightbulb: {
    glow: document.getElementById('glow'),
  },
  display: {
    content: document.getElementById('content'),
  },
  thermometer: {
    label: document.getElementById('thermometer-label'),
    slider: document.getElementById('thermometer-slider'),
  },
  motionsensor: {
    area: document.getElementById('motion-sensor-area'),
  }
}

const updateLightbulb = (update) => {
  newBrightness = update.brightness ?? undefined;
  newPower = update.power ?? undefined;
  if(newBrightness != undefined) deviceElements.lightbulb.glow.style.opacity = newBrightness/100;
  if(newPower != undefined) deviceElements.lightbulb.glow.style.display = newPower ? 'block' : 'none'; 
}

const updateDisplay = (update) => {
  newContent = update.content ?? undefined;
  if(newContent != undefined) deviceElements.display.content.innerText = newContent;
}

const deviceUpdateFunctions = {
  lightbulb: updateLightbulb,
  display: updateDisplay,
}



// HANDLE SSE
eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  for(let key of Object.keys(data)){
    if(Object.keys(deviceUpdateFunctions).indexOf(key) != -1) deviceUpdateFunctions[key](data[key]);
  }
}

eventSource.onerror = (error) => {
  console.error('SSE error:', error);
}


// THERMOMETER
const updateThermometerLabel = () => {
  deviceElements.thermometer.label.innerText = parseFloat(deviceElements.thermometer.slider.value).toFixed(1) + " °C";
}

// Add eventlistener to thermometer slider
deviceElements.thermometer.slider.addEventListener('input', () => {
  updateThermometerLabel();
});

// TODO: Handle fetch errors
deviceElements.thermometer.slider.addEventListener('change', () => {
  fetch("http://localhost:3000/thermometer", {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({temperature: deviceElements.thermometer.slider.value})
  });
});

// MOTION SENSOR
deviceElements.motionsensor.area.addEventListener('mouseenter', () => {
  fetch("http://localhost:3000/motionsensor", {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({presence: true})
  });
});

deviceElements.motionsensor.area.addEventListener('mouseleave', () => {
  fetch("http://localhost:3000/motionsensor", {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({presence: false})
  });
});

// GET INITIAL STATES FROM BACKEND
const initializeDevices = async () => {
  res = await fetch("http://localhost:3000/devices", {
    method: 'GET',
    headers: {
        'Accept': 'application/json',
    },
  });
  let devices = await res.json();
  for(let key of Object.keys(devices)){
    if(Object.keys(deviceUpdateFunctions).indexOf(key) != -1) deviceUpdateFunctions[key](devices[key]);
  }

  // Initialize thermometer
  updateThermometerLabel();
}

initializeDevices();
