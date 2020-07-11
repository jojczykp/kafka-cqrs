const { merge } = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  devServer: {
    contentBase: './dist',
    proxy: {
      '/': {
        target: 'http://' + process.env.API_GATEWAY + '/',
        changeOrigin: true,
        pathRewrite: {
          '^/': '',
        }
      }
    }
  }
});
