// Add these rules under module.exports
module.exports = {
    module: {
      rules: [
        {
          test: /\.svg$/,
          use: [
            {
              loader: '@svgr/webpack',
              options: {
                svgoConfig: {
                  plugins: [
                    { name: 'removeViewBox', active: false },
                    { name: 'prefixIds', active: true }
                  ]
                }
              }
            }
          ]
        }
      ]
    }
  };
  