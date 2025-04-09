// src/components/chat/MessageInput.js
import React, { useState } from 'react';
import '../../styles/components/MessageInput.css';

const MessageInput = ({ onSendMessage }) => {
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    const chatId = localStorage.getItem('currentChat');
    await api.post(`/api/chat/${chatId}/messages`, { content: message });
  };
  
  //const handleSubmit = async (e) => {
    //e.preventDefault();
    //if (message.trim()) {
      //try {
        //await api.post('/api/messages', {
          //content: message,
          //chatId: currentChatId // Add required chat ID
        //});
      //} catch (error) {
        //console.error('Message send failed:', error);
      //}
      //setMessage('');
    //}
  //};

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      handleSubmit(e);
    }
  };

  return (
    <div className="message-input-container">
      <form onSubmit={handleSubmit}>
        <div className="input-wrapper">
          <textarea
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            placeholder="Type a message..."
            className="message-textarea"
          />
          <button 
            type="submit" 
            className="send-button"
            disabled={!message.trim()}
          >
            Send
          </button>
        </div>
      </form>
    </div>
  );
};

export default MessageInput;
