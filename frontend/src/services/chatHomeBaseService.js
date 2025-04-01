import io from 'socket.io-client';

class ChatHomeBaseService {
  constructor() {
    this.socket = null;
  }

  connect() {
    this.socket = io('https://api.chathomebase.com');
  }

  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
    }
  }

  onMessage(callback) {
    this.socket.on('message', callback);
  }

  onStatusChange(callback) {
    this.socket.on('status_change', callback);
  }

  sendMessage(content) {
    this.socket.emit('send_message', { content });
  }
}

export const chatHomeBaseService = new ChatHomeBaseService();
