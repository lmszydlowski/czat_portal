// src/services/authService.js
import { api } from './api';

export const authService = {
  login: async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      
      if (response.token) {
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
        return response;
      }
      throw new Error('Authentication failed - no token received');
    } catch (error) {
      console.error('Login error:', error);
      throw new Error(error.response?.data?.message || 'Login failed. Please check your credentials');
    }
  },

  register: async (userData) => {
    try {
      const urlParams = new URLSearchParams(window.location.search);
      const klikId = urlParams.get('klikid');
      
      const payload = klikId ? { ...userData, klikId } : userData;
      const response = await api.post('/auth/register', payload);
      
      if (response.token) {
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
      }
      return response;
    } catch (error) {
      console.error('Registration error:', error);
      throw new Error(error.response?.data?.message || 'Registration failed. Please try again');
    }
  },

  verifyEmail: async (token) => {
    try {
      return await api.post(`/auth/verify-email/${token}`);
    } catch (error) {
      console.error('Email verification error:', error);
      throw new Error(error.response?.data?.message || 'Email verification failed');
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: async () => {
    try {
      const user = localStorage.getItem('user');
      if (user) return JSON.parse(user);
      
      const response = await api.get('/auth/user');
      localStorage.setItem('user', JSON.stringify(response.data));
      return response.data;
    } catch (error) {
      console.error('Get user error:', error);
      localStorage.removeItem('token');
      throw new Error('Session expired. Please log in again');
    }
  },

  triggerModal: (modalType, data) => {
    const modalEvent = new CustomEvent('modal_event', { 
      detail: { modalType, data } 
    });
    window.dispatchEvent(modalEvent);
  },

  showLoginModal: () => {
    authService.triggerModal('login', {});
  },

  showRegisterModal: () => {
    authService.triggerModal('register', {});
  }
};
