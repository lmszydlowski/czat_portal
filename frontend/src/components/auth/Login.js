// src/components/auth/Login.js
import React, { useState } from 'react';
import '../../styles/components/auth/Login.css';

export const Login = () => {
  const [credentials, setCredentials] = useState({
    email: '',
    password: ''
  });

  return (
    <div className="login-container">
      <form className="login-form">
        <input
          className="login-input"
          type="email"
          placeholder="Email"
          value={credentials.email}
          onChange={(e) => setCredentials({...credentials, email: e.target.value})}
        />
        <input
          className="login-input"
          type="password"
          placeholder="Password"
          value={credentials.password}
          onChange={(e) => setCredentials({...credentials, password: e.target.value})}
        />
        <button className="login-button" type="submit">
          Login
        </button>
      </form>
    </div>
  );
};
