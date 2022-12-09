import Vue from 'vue'
import App from './App.vue'

//路由
import VueRouter from 'vue-router'
import router from "./router";
Vue.use(VueRouter)

// 样式
import Antd from 'ant-design-vue/es';
import 'ant-design-vue/dist/antd.css';

Vue.use(Antd)

// Vuex
import store from './store'

Vue.config.productionTip = false

new Vue({
	el: '#app',
	render: h => h(App),
	router:router,
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
})
