import io from 'socket.io-client';

class ChatHomeBaseService {
  constructor() {
    this.socket = null;
  }

  connect() {
    // Update to use seksnow.pl domain with HTTPS
    this.socket = io('https://seksnow.pl');
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
