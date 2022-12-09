import Vue from 'vue'
import App from './App.vue'
// 路由
import VueRouter from 'vue-router'
import router from './router'
Vue.use(VueRouter)
// 样式
import Antd from 'ant-design-vue/es';
import 'ant-design-vue/dist/antd.css';
Vue.use(Antd)
// moment
import moment from 'moment'
Vue.prototype.$moment = moment
// Vuex
import store from './store'
// 生成环境
Vue.config.productionTip = false
// lodash防抖、节流
import _ from 'lodash'
Vue.prototype._ = _
// token管理
import { removeToken } from './utils/token'
// pdf下载
import vueToPdf from 'vue-to-pdf';
Vue.use(vueToPdf);
new Vue({
	el: '#app',
	render: h => h(App),
	router: router,
	store,
	beforeCreate() {
		Vue.prototype.$bus = this //安装全局事件总线
		window.onresize = () => {
			window.screenWidth = document.body.clientWidth;
			window.screenWidth <= 450
				? (Vue.prototype.$mobile = true)
				: (Vue.prototype.$mobile = false);
		};
		window.onresize();
	},
	beforeDestroy() {
		removeToken()
	}
})
