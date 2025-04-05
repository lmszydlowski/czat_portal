import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Login } from './components/auth/Login';
import { ChatWindow } from './components/chat/ChatWindow';
import AdminDashboard from './components/admin/AdminDashboard';
import Register from './components/auth/Register';
import { modalHandlers } from './services/modal-handlers';
import Modal from './components/common/Modal';

const App = () => {
  React.useEffect(() => {
    // Initialize modal handlers
    modalHandlers.initialize();
    
    return () => {
      // Clean up event listeners
      modalHandlers.cleanup();
    };
  }, []);
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          <h1>Chat Home Base</h1>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/chat" element={<ChatWindow />} />
            <Route path="/admin" element={<AdminDashboard />} />
	    <Route path="/register" element={<Register />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
};

export default App;