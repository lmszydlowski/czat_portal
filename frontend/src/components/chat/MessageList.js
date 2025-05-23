// src/components/chat/MessageList.js
import React from 'react';
import '../../styles/components/MessageList.css';

const MessageList = ({ messages }) => {
  return (
    <div className="message-list-container">
      {messages.map((message, index) => (
        <div 
          key={index} 
          className={`message ${message.isSender ? 'sent' : 'received'}`}
        >
          <div className="message-content">
            <div className="message-header">
              <span className="sender-name">{message.senderName}</span>
              <span className="message-time">
                {new Date(message.timestamp).toLocaleTimeString()}
              </span>
            </div>
            <div className="message-text">{message.text}</div>
          </div>
        </div>
      ))}
    </div>
  );
};
useEffect(() => {
  const fetchMessages = async () => {
    try {
      const response = await api.get(`/api/chat/${chatId}/messages`);
      setMessages(response.data);
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };
  fetchMessages();
}, [chatId]);

export default MessageList;
