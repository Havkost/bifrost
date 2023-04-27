const eventSource = new EventSource('/stateSSE');
const lightbulbElement = document.getElementById('lightbulb');

const deviceElements = {
  lightbulb: {
    glow: document.getElementById('glow'),
  },
  display: {
    content: document.getElementById('content'),
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
  if(newContent != undefined) {
    deviceElements.display.content.innerText = toString(newContent);
  } else {
    deviceElements.display.content.innerText = "...";
  }
}

const deviceUpdateFunctions = {
  lightbulb: updateLightbulb,
  display: updateDisplay,
}

// HANDLE SSE
eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  for(let key of Object.keys(data)){
    deviceUpdateFunctions[key](data[key]);
  }
}

eventSource.onerror = (error) => {
  console.error('SSE error:', error);
}
