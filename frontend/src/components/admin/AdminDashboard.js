// src/context/admin/AdminDashboard.js
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { api } from '../../services/api';
import { useMediaQuery } from 'react-responsive';
import '../../styles/components/admin/AdminDashboard.css';
// import { handleUserBlock, handleMessageDelete } from '../../utils/adminUtils'; // Utility functions for blocking users and deleting messages

api.get('/admin/users')
api.get('/admin/chats')
api.get('/admin/payments')

const AdminDashboard = () => {
  const [users, setUsers] = useState([]);
  const [chats, setChats] = useState([]);
  const [payments, setPayments] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [chatHistory, setChatHistory] = useState([]);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { currentUser } = useAuth();
  
  // Responsive breakpoints
  const isMobile = useMediaQuery({ maxWidth: 768 });
  const isTablet = useMediaQuery({ minWidth: 769, maxWidth: 1024 });
  
  useEffect(() => {
    fetchAdminData();
  }, []);
  
  const fetchAdminData = async () => {
    try {
      // These calls will use the updated api service with HTTPS
      const [usersData, chatsData, paymentsData] = await Promise.all([
        api.get('/admin/users'),
        api.get('/admin/chats'),
        api.get('/admin/payments')
      ]);
      setUsers(usersData.data);
      setChats(chatsData.data);
      setPayments(paymentsData.data);
    } catch (error) {
      console.error('Error fetching admin data:', error);
    }
  };
  
  const viewUserChatHistory = async (userId) => {
    setSelectedUser(userId);
    try {
      const response = await api.get(`/admin/chat-history/${userId}`);
      setChatHistory(response.data);
      // On mobile, auto-scroll to chat history section
      if (isMobile) {
        document.querySelector('.chat-history').scrollIntoView({ behavior: 'smooth' });
      }
    } catch (error) {
      console.error('Error fetching chat history:', error);
    }
  };
  
  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };

  return (
    <div className="admin-dashboard">
      {isMobile && (
        <button className="sidebar-toggle" onClick={toggleSidebar}>
          {sidebarOpen ? 'Close Menu' : 'Open Menu'}
        </button>
      )}
      
      <h2>Admin Dashboard</h2>
      
      <div className={`admin-panel ${isMobile ? 'mobile' : ''} ${sidebarOpen ? 'sidebar-open' : ''}`}>
        <div className={`section user-list ${isMobile && !sidebarOpen ? 'hidden' : ''}`}>
          <h3>Users</h3>
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  {!isMobile && <th>User ID</th>}
                  <th>Email</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map(user => (
                  <tr key={user.id}>
                    {!isMobile && <td>{user.id}</td>}
                    <td>{user.email}</td>
                    <td>{user.status}</td>
                    <td>
                      {/* <button onClick={() => viewUserChatHistory(user.id)}>View</button> */}
                      {/* <button onClick={() => handleUserBlock(user.id)}>Block</button> */}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
        
        {(!isMobile || (isMobile && !sidebarOpen)) && (
          <>
            <div className="section chat-history">
              <h3>Chat History</h3>
              {selectedUser ? (
                <div className="messages">
                  <div className="table-container">
                    <table>
                      <thead>
                        <tr>
                          <th>Time</th>
                          <th>Message</th>
                          {!isMobile && <th>Source</th>}
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {chatHistory.map(msg => (
                          <tr key={msg.id} className={msg.isFromUser ? 'user-message' : 'system-message'}>
                            <td>{new Date(msg.timestamp).toLocaleString()}</td>
                            <td>{msg.content}</td>
                            {!isMobile && <td>{msg.isFromUser ? 'User' : 'Bot'}</td>}
                            <td>
                              {/* <button onClick={() => handleMessageDelete(msg.id)}>Delete</button> */}
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                </div>
              ) : (
                <p>Select a user to view chat history</p>
              )}
            </div>
            
            {!isMobile && (
              <div className="section payment-info">
                <h3>Payment Transactions</h3>
                <div className="table-container">
                  <table>
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Amount</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      {payments.map(payment => (
                        <tr key={payment.transactionId}>
                          <td>{payment.transactionId}</td>
                          <td>{payment.userEmail}</td>
                          <td>${payment.amount}</td>
                          <td>{payment.status}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default AdminDashboard;
