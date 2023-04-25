const eventSource = new EventSource('/stateSSE');

eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('Received data:', data);
}

eventSource.onerror = (error) => {
  console.error('SSE error:', error);
}
