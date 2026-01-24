const path = require('path');
const HtmlPlugin = require("html-webpack-plugin");

module.exports = {
  target: 'web',
  entry: './src/index.js',
  output: {
    filename: 'app.js',
    path: path.resolve(__dirname, 'dist'),
    assetModuleFilename: '[name][ext]'
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx|(s*)css)$/,
        enforce: 'pre',
        use: 'import-glob-loader2'
      },
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader"
        }
      },
      {
        test: /\.html$/,
        use: [
          {
            loader: "html-loader"
          }
        ]
      },
      {
        test: /\.(s*)css$/,
        use: ['style-loader','css-loader', 'sass-loader']
      },
      {
        test: /\.(png|jpg|gif)$/,
        type: 'asset',
        parser: {
          dataUrlCondition: {
            maxSize: 5000
          }
        }
      },
      {
        test: /\.svg$/,
        type: 'asset/resource'
      }
    ]
  },

  plugins: [
    new HtmlPlugin({
      template: "./src/index.html",
      filename: "./index.html"
    })
  ]
};
