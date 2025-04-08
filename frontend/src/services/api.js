// src/services/api.js
const API_URL = process.env.REACT_APP_API_URL || '/api';

export const api = {
  get: async (endpoint) => {
    try {
      const response = await fetch(`${API_URL}${endpoint}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        }
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return response.json();
    } catch (error) {
      console.error('API Get Error:', error);
      throw error;
    }
  },

  post: async (endpoint, data) => {
    try {
      const response = await fetch(`${API_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return response.json();
    } catch (error) {
      console.error('API Post Error:', error);
      throw error;
    }
  },
// Admin endpoints
  admin: {
    getUsers: () => api.get('/admin/users'),
    getChats: () => api.get('/admin/chats'),
    getPayments: () => api.get('/admin/payments'),
    blockUser: (userId) => api.post(`/admin/users/${userId}/block`),
    deleteChat: (chatId) => api.delete(`/admin/chats/${chatId}`),
  },

  delete: async (endpoint) => {
    try {
      const response = await fetch(`${API_URL}${endpoint}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json'
        }
      });
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      return response.json();
    } catch (error) {
      console.error('API Delete Error:', error);
      throw error;
    }
  }
};
