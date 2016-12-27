var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var config = {
	node: {
		fs: 'empty'
	},
	devtool: 'eval',
	entry: [
		'webpack-hot-middleware/client',
		'./src/js/main.js',
		'./src/css/main.css'
	],
	output: {
		path: path.join(__dirname, 'target'),
		filename: 'compiled.js',
		publicPath: '/static/'
	},
	module: {
		loaders: [{
			test: /\.js$/,
			loader: 'babel',
			query: {
				stage: 0,
				plugins: []
			}
		}, {
			test: /\.css$/,
			loader: ExtractTextPlugin.extract("style-loader", "css-loader")
		}, {
			test: /\.(png|woff|woff2|eot|ttf|svg)$/,
			loader: 'url-loader?limit=100000'
		}]
	},
	plugins: [
		new ExtractTextPlugin("styles.css", {
			allChunks: true
		})
	]
};

if (!process.env.NODE_ENV || process.env.NODE_ENV == "development") {
	config.plugins.unshift(new webpack.HotModuleReplacementPlugin());
}


module.exports = config;