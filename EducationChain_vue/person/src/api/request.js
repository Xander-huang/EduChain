// axios二次封装
import axios from "axios"
// 进度条模块
import nprogress from "nprogress"
// 进度条样式
import "nprogress/nprogress.css"
// vuex
import store from "../store"
// 错误消息总控制
import Vue from 'vue'
import { message } from 'ant-design-vue/es';
import 'ant-design-vue/dist/antd.css';
Vue.use(message)

// import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
// Vue.use(ElementUI);

// request == axios
const requests = axios.create({
    // 配置对象
    // 基础路径，发送请求时，路径中会出现api
    baseURL: "/api",
    // 超时5s
    // timeout: 5000
})

// 请求拦截器：发送请求前，请求拦截器可以检测到，可以在请求发送前操作
requests.interceptors.request.use((config) => {
    // 服务器需要token认证
    if (store.state.user.token) {
        config.headers.Authorization = store.state.user.token
    }
    nprogress.start()
    // config：配置对象，headers请求头
    return config
})

// 响应拦截器
requests.interceptors.response.use((res) => {

    nprogress.done()
    // 成功回调数据，响应拦截器可以接收到，可以操作
    message.destroy();
    let code = res.data.code
    if (code != 200 && code != undefined) {
        let state = null
        if (code == 400) {
            state = "客户端错误";
        } else if (code == 500) {
            state = "服务器错误";
        } else {
            state = "未知错误";
        }
        message.error(`${state},${res.data.data}`);
    }
    return res.data;
}, (error) => {
    alert("服务器响应失败");
    // 响应失败的回调函数
    return Promise.reject(new Error('faile'))
})

export default requests