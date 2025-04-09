// src/services/api.js
const API_URL = process.env.REACT_APP_API_URL || '/api';
const socket = io(process.env.REACT_APP_WS_URL || '/ws');

export const api = {
  // Update all endpoints to use /api prefix
  get: async (endpoint) => await fetch(`${API_URL}${endpoint}`, {/* headers */}),
  post: async (endpoint, data) => await fetch(`${API_URL}${endpoint}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${localStorage.getItem('token')}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  }),
  admin: {
    getUsers: () => api.get('/admin/users'),
    getChats: () => api.get('/admin/chats'),
    getPayments: () => api.get('/admin/payments')
  }
  ,

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
