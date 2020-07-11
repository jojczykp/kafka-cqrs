const path = require('path');
const HtmlPlugin = require("html-webpack-plugin");

module.exports = {
  target: 'node',
  entry: './src/index.js',
  output: {
    filename: 'app.js',
    path: path.resolve(__dirname, 'dist'),
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
        test: /\.(png|jpg|svg|gif)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              esModule: false,
              limit: 5000
            }
          }
        ]
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
