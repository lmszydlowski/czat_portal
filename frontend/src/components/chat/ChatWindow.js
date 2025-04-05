import React from 'react';
import MessageList from './MessageList';
import MessageInput from './MessageInput';
import { useWebSocket } from '../../hooks/useWebSocket';
import { useChatHomeBase } from '../../hooks/useChatHomeBase';
import '../../styles/components/chat/ChatWindow.css';

export var  ChatWindow = () => {
  const { messages, sendMessage, userStatus } = useChatHomeBase();

  return (
    <div className="chat-window">
      <div className="user-status">{userStatus}</div>
      <MessageList messages={messages} />
      <MessageInput onSendMessage={sendMessage} />
    </div>
  );
};

ChatWindow = () => {
  const [messages, setMessages] = useState([]);
  const { sendMessage, lastMessage } = useWebSocket();
  const socket = new WebSocket('wss://www.seksnow.pl/ws');

  useEffect(() => {
    if (lastMessage) {
      setMessages((prevMessages) => [...prevMessages, lastMessage]);
    }
  }, [lastMessage]);

  const handleSendMessage = (content) => {
    sendMessage(content);
  };

  return (
    <div className="chat-window">
      <MessageList messages={messages} />
      <MessageInput onSendMessage={handleSendMessage} />
    </div>
  );
};

export default ChatWindow;

