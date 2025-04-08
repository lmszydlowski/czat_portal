// services/websocket.js
const ws = new WebSocket(process.env.REACT_APP_WS_URL || '/ws');