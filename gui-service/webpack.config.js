const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {
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
                    limit: 5000
                  }
                }
              ]
            }
    ]
  },

  devServer: {
    proxy: {
      '/': {
        target: 'http://' + process.env.API_GATEWAY + '/',
        changeOrigin: true,
        pathRewrite: {
          '^/': '',
        }
      }
    }
  },

  plugins: [
    new HtmlWebPackPlugin({
      template: "./src/index.html",
      filename: "./index.html"
    })
  ]
};
