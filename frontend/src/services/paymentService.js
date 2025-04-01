export const processPayment = (amount, currency) => {
    return api.post('/payment', { amount, currency });
};
