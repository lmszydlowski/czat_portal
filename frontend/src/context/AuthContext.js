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
      const userData = await api.get('/auth/profile'); // Changed from '/user/profile'
      setCurrentUser(userData);
      setIsAdmin(userData.role === 'ADMIN');
    } catch (error) {
      console.error('Error fetching user data:', error);
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
