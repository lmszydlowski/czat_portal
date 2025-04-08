// services/chatService.js

import { api } from './api';

export const chatService = {
  getMessages: async (chatId) => {
    try {
      return await api.get(`/chat/${chatId}/messages`);
    } catch (error) {
      throw error;
    }
  },

  sendMessage: async (chatId, message) => {
    try {
      return await api.post(`/chat/${chatId}/messages`, { content: message });
    } catch (error) {
      throw error;
    }
  },

  getChatHistory: async () => {
    try {
      return await api.get('/chat/history');
    } catch (error) {
      throw error;
    }
  }
};
