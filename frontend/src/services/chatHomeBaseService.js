class ChatHomeBaseService {
  connect() {
    this.socket = io(process.env.REACT_APP_WS_URL || 'https://seksnow.pl');
  }
}