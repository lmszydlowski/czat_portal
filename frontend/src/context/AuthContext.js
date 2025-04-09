// src/context/AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { api } from '../services/api';
import { Navigate } from 'react-router-dom';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [currentUser, setCurrentUser] = useState(null);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      fetchUserData();
    }
  }, []);

  const fetchUserData = async () => {
    try {
      const response = await api.get('/api/auth/profile');
      if(response.status === 401) {
        localStorage.removeItem('token');
        window.location.reload();
      }
      setCurrentUser(response.data);
    } catch (error) {
      console.error('Authentication error:', error);
    }
  };

  const requireAdmin = (Component) => {
    return function AdminProtected(props) {
      if (!isAdmin) {
        return <Navigate to="/" />;
      }
      return <Component {...props} />;
    };
  };

  return (
    <AuthContext.Provider value={{ 
      currentUser, 
      isAdmin,
      requireAdmin 
    }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
