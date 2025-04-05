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
// Add to chatHomeBaseService.js class
onModalEvent(callback) {
  this.socket.on('modal_event', callback);
}

// Add method to trigger chat-related modals
triggerChatModal(userId) {
  this.socket.emit('open_chat', { userId });
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
