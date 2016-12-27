var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
	devtool: 'source-map',
	entry: [
		'./src/js/main.js',
		'./src/css/main.css'
	],
	output: {
		path: path.join(__dirname, 'target'),
		filename: 'compiled.[hash].js'
	},
	plugins: [
		new webpack.optimize.OccurenceOrderPlugin(),
		new webpack.DefinePlugin({
			'process.env': {
				'NODE_ENV': JSON.stringify('production')
			}
		}),
		new webpack.optimize.UglifyJsPlugin({
			compressor: {
				warnings: false
			}
		}),
		new ExtractTextPlugin("styles.css", {
			allChunks: true
		}),
		new HtmlWebpackPlugin({
			hash: true,
			filename: 'index.html',
			template: 'assets/pages/index.html',
		})
	],
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
	}
};
