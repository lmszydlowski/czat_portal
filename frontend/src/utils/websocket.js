export const connectWebSocket = () => {
  const ws = new WebSocket(process.env.REACT_APP_WS_URL);
  
  ws.onopen = () => {
    console.log('WebSocket connected');
  };

  ws.onmessage = (event) => {
    const message = JSON.parse(event.data);
    // Handle incoming message
  };

  ws.onerror = (error) => {
    console.error('WebSocket error:', error);
  };

  return ws;
};
