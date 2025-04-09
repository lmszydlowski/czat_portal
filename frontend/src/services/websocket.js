// src/services/websocket.js
export const connectWebSocket = () => {
    const ws = new WebSocket(WS_URL); // Use environment variable
    
    // ... rest of implementation 
    // const ws = new WebSocket(process.env.REACT_APP_WS_URL || '/ws');
    // export const connectWebSocket = () => {
    // const ws = new WebSocket(process.env.REACT_APP_WS_URL || '/ws');
}
const WS_URL = process.env.REACT_APP_WS_URL || '/ws';


//const socket = io(process.env.REACT_APP_WS_URL || '/ws');