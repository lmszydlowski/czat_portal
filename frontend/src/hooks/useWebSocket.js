import { useEffect, useRef, useState } from 'react';
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
    const socket = new SockJS(config.wsUrl);
    const client = Stomp.over(socket);

    client.connect({}, () => {
      client.subscribe('/topic/messages', (message) => {
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
      const response = await api.post('/auth/register', { email, password });
      if (response.data.token) {
        login(response.data.token);
      }
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

useWebSocket = () => {
  const [lastMessage, setLastMessage] = useState(null);
  const wsRef = useRef(null);

  useEffect(() => {
    // Use secure WebSocket URL from config
    wsRef.current = new WebSocket(WS_URL);

    wsRef.current.onmessage = (event) => {
      const message = JSON.parse(event.data);
      setLastMessage(message);
    };

    return () => {
      if (wsRef.current) {
        wsRef.current.close();
      }
    };
  }, []);

  const sendMessage = (message) => {
    if (wsRef.current?.readyState === WebSocket.OPEN) {
      wsRef.current.send(JSON.stringify(message));
    }
  };

  return { sendMessage, lastMessage };
};

export default Register;
