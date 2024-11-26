const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new (require('webpack')).DefinePlugin({
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false), // 플래그 설정
      }),
    ],
  },
  devServer: {
    proxy: {
      '/api': { 
        target: 'https://mydata-api.cyber-i.com', 
        changeOrigin: true,
        ws: false, 
        pathRewrite: { '^/api': '/svc/test' }, // ^/api = /api로 시작하는 모든 경로들을 의미
      },
    },
  },
});
