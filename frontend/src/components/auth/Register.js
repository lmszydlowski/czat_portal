// src/components/auth/Register.js
import React, { useState } from 'react';
import { useAuth } from '../../context/AuthContext';
import { api } from '../../services/api';

await api.post('/auth/register', { email, password, klikId });

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const urlParams = new URLSearchParams(window.location.search);
    const klikId = urlParams.get('klikid');
    try {
      // This will use the updated api service with HTTPS
      const response = await api.post('/auth/register', { email, password, klikId });
      if (response.data.token) {
        login(response.data.token);
      }
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };
  

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder="Email"
        required
      />
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        placeholder="Password"
        required
      />
      <button type="submit">Register</button>
    </form>
  );
};

export default Register;
