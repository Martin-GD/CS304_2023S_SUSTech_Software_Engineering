const {defineConfig} = require('@vue/cli-service')
const MonacoWebpackPlugin = require('monaco-editor-webpack-plugin')

module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        proxy: {
            '/gitd': {
                target: 'http://10.24.3.53:8080',
                ws: true,//代理websocked
                changeOrigin: true,//虚拟的站点需要更管origin
                secure: true,
                pathRewrite: {
                    '^/gitd': '' // 重写请求
                }
            }
        },
        // disableHostCheck: true // 禁用主机检查
        allowedHosts: ['localhost', 'atom.nat300.top', '10.26.142.228']
    },
    configureWebpack: {
        plugins: [
            new MonacoWebpackPlugin()
        ]
    }
})
