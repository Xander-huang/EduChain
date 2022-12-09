module.exports = {
  // 关闭eslint
  lintOnSave: false,
  // 代理跨域，上线时删除
  devServer: {
    open: true,
    https: false,
    hotOnly: false,
    proxy: {
      // 配置跨域
      '/api': {
        target: 'http://localhost:8082',//检索端后端接口地址
        ws: true,
        // changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/qwe': {
        target: 'http://localhost:8084',//检索端后端接口地址
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/qwe': ''
        }
      },
    },
    before: app => { }
  }
}
