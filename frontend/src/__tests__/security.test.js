// Add to src/__tests__/security.test.js
test('nth-check version', () => {
    const nthCheck = require('nth-check/package.json').version;
    expect(nthCheck).toBe('2.1.1');
  });
  