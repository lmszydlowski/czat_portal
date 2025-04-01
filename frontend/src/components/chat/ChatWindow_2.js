import React, { useEffect, useState } from 'react';
import { MessageList } from './MessageList';
import { MessageInput } from './MessageInput';
import { useWebSocket } from '../../hooks/useWebSocket';

export const ChatWindow = () => {
  const [messages, setMessages] = useState([]);
  const { sendMessage, lastMessage } = useWebSocket();

  useEffect(() => {
    if (lastMessage) {
      setMessages(prev => [...prev, lastMessage]);
    }
  }, [lastMessage]);

  return (
    <div className="chat-window">
      <MessageList messages={messages} />
      <MessageInput onSend={sendMessage} />
    </div>
  );
};
