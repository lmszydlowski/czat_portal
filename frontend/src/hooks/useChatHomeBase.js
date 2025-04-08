// hooks/useChatHomeBase.js
import { useState, useEffect } from 'react';
import { chatHomeBaseService } from '../services/chatHomeBaseService';

export const useChatHomeBase = () => {
  const [messages, setMessages] = useState([]);
  const [userStatus, setUserStatus] = useState('Offline');

  useEffect(() => {
    chatHomeBaseService.connect();
    chatHomeBaseService.onMessage((message) => {
      setMessages((prevMessages) => [...prevMessages, message]);
    });
    chatHomeBaseService.onStatusChange((status) => {
      setUserStatus(status);
    });

    return () => chatHomeBaseService.disconnect();
  }, []);

  const sendMessage = (content) => {
    chatHomeBaseService.sendMessage(content);
  };

  const connectToChat = () => {
    const socket = new WebSocket('wss://seksnow.pl/ws');
  }; // Added missing closing curly brace here

  return { messages, sendMessage, userStatus };
};

