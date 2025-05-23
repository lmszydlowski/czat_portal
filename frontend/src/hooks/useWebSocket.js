// src/hooks/useWebSocket.js
import React, { useEffect, useRef, useState } from 'react';
import { WS_URL } from '../config';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { config } from '../config';

export var useWebSocket = () => {
  const [stompClient, setStompClient] = useState(null);
  const [lastMessage, setLastMessage] = useState(null);

  useEffect(() => {
    // Use secure WebSocket URL
    const socket = new SockJS(`${process.env.REACT_APP_WS_URL}/chat`);
    //const socket = new SockJS(`${config.wsUrl}/chat`);
const client = Stomp.over(socket);
client.connect({}, () => {
  client.subscribe('/topic/chat', (message) => {
    setLastMessage(JSON.parse(message.body));
  });
});

    setStompClient(client);

    return () => {
      if (client) {
        client.disconnect();
      }
    };
  }, []);

  const sendMessage = (content) => {
    if (stompClient) {
      stompClient.send("/app/chat", {}, JSON.stringify({ content }));
    }
  };

  return { sendMessage, lastMessage };
};

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/register', { 
        email, 
        password,
        role: 'USER' // Add required field
      });
      // ... rest of logic
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
      <button type="submit">Register</button>
    </form>
  );
};

export default useWebSocket;
