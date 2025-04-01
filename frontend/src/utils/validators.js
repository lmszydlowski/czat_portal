export const validators = {
  email: (email) => {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return regex.test(email);
  },

  password: (password) => {
    return password.length >= 8;
  },

  username: (username) => {
    return username.length >= 3;
  }
};
