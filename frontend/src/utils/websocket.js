export const connectWebSocket = () => {
  // Update to use secure WebSocket with seksnow.pl domain
  const ws = new WebSocket('wss://seksnow.pl/ws');

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

