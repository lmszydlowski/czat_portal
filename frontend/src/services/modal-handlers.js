// src/services/modal-handlers.js
import { authService } from './authService';
import { chatService } from './chatService';

class ModalHandlers {
  constructor() {
    this.activeModals = [];
    this.eventListeners = [];
  }

  initialize() {
    // Set up global event listeners for modals
    const modalEventListener = this.handleModalEvent.bind(this);
    window.addEventListener('modal_event', modalEventListener);
    this.eventListeners.push({ type: 'modal_event', handler: modalEventListener });
    
    // Listen for auth-related events
    document.querySelectorAll('.login-button').forEach(button => {
      button.addEventListener('click', () => this.openModal('login'));
    });
    
    document.querySelectorAll('.register-button').forEach(button => {
      button.addEventListener('click', () => this.openModal('register'));
    });
    
    // Close modal on backdrop click or X button
    document.addEventListener('click', (e) => {
      if (e.target.classList.contains('modal-backdrop') || 
          e.target.classList.contains('modal-close')) {
        this.closeAllModals();
      }
    });
  }

  cleanup() {
    // Remove all event listeners
    this.eventListeners.forEach(listener => {
      window.removeEventListener(listener.type, listener.handler);
    });
    this.eventListeners = [];
  }

  handleModalEvent(event) {
    const { modalType, data } = event.detail;
    this.openModal(modalType, data);
  }

  openModal(type, data = {}) {
    const modal = document.getElementById(`${type}-modal`);
    if (!modal) return;
    
    // Display the modal
    modal.style.display = 'block';
    document.body.classList.add('modal-open');
    this.activeModals.push(type);
    
    // Handle specific modal types
    switch (type) {
      case 'login':
        this.initLoginForm();
        break;
      case 'register':
        this.initRegisterForm();
        break;
      case 'chat':
        this.initChatModal(data);
        break;
      default:
        break;
    }
  }

  closeAllModals() {
    document.querySelectorAll('.modal').forEach(modal => {
      modal.style.display = 'none';
    });
    document.body.classList.remove('modal-open');
    this.activeModals = [];
  }

  initLoginForm() {
    const form = document.getElementById('login-form');
    if (!form) return;
    
    const handleSubmit = async (e) => {
      e.preventDefault();
      const email = form.querySelector('input[name="email"]').value;
      const password = form.querySelector('input[name="password"]').value;
      
      try {
        await authService.login({ email, password });
        this.closeAllModals();
        window.location.reload(); // Refresh to show authenticated state
      } catch (error) {
        // Display error message in the modal
        const errorElement = form.querySelector('.error-message');
        if (errorElement) {
          errorElement.textContent = error.message;
          errorElement.style.display = 'block';
        }
      }
    };
    
    // Remove existing event listener to prevent duplicates
    form.removeEventListener('submit', handleSubmit);
    // Add new event listener
    form.addEventListener('submit', handleSubmit);
  }

  initRegisterForm() {
    const form = document.getElementById('register-form');
    if (!form) return;
    
    const handleSubmit = async (e) => {
      e.preventDefault();
      const username = form.querySelector('input[name="username"]').value;
      const email = form.querySelector('input[name="email"]').value;
      const password = form.querySelector('input[name="password"]').value;
      
      try {
        await authService.register({ username, email, password });
        this.closeAllModals();
        window.location.reload(); // Refresh to show authenticated state
      } catch (error) {
        // Display error message in the modal
        const errorElement = form.querySelector('.error-message');
        if (errorElement) {
          errorElement.textContent = error.message;
          errorElement.style.display = 'block';
        }
      }
    };
    
    // Remove existing event listener to prevent duplicates
    form.removeEventListener('submit', handleSubmit);
    // Add new event listener
    form.addEventListener('submit', handleSubmit);
  }

  initChatModal(data) {
    const { userId } = data;
    const chatContainer = document.getElementById('chat-messages-container');
    if (!chatContainer || !userId) return;
    
    // Load chat history with this user
    chatService.getMessages(userId)
      .then(response => {
        // Render messages
        chatContainer.innerHTML = response.messages
          .map(msg => `<div class="message ${msg.sender === 'me' ? 'sent' : 'received'}">
                        <p>${msg.content}</p>
                        <span class="timestamp">${new Date(msg.timestamp).toLocaleTimeString()}</span>
                      </div>`)
          .join('');
          
        // Scroll to bottom
        chatContainer.scrollTop = chatContainer.scrollHeight;
      })
      .catch(error => {
        console.error('Failed to load messages:', error);
        chatContainer.innerHTML = '<p class="error">Failed to load messages. Please try again.</p>';
      });
      
    // Set up message form
    const messageForm = document.getElementById('chat-message-form');
    if (messageForm) {
      const handleSend = async (e) => {
        e.preventDefault();
        const messageInput = messageForm.querySelector('input[name="message"]');
        const message = messageInput.value.trim();
        
        if (message) {
          try {
            await chatService.sendMessage(userId, message);
            messageInput.value = '';
          } catch (error) {
            console.error('Failed to send message:', error);
          }
        }
      };
      
      // Remove existing event listener to prevent duplicates
      messageForm.removeEventListener('submit', handleSend);
      // Add new event listener
      messageForm.addEventListener('submit', handleSend);
    }
  }
}

export const modalHandlers = new ModalHandlers();