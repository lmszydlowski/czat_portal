export const API_URL = process.env.REACT_APP_API_URL || 'https://www.seksnow.pl/api';
export const WS_URL = process.env.REACT_APP_WS_URL || 'wss://www.seksnow.pl/ws';

export const config = {
  apiUrl: API_URL,
  wsUrl: WS_URL,
  maxFileSize: 5 * 1024 * 1024, // 5MB
  supportedFileTypes: ['image/jpeg', 'image/png', 'image/gif'],
  maxMessageLength: 1000
};
