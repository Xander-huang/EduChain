import VueRouter from 'vue-router'

import Login from '../pages/Login/Login'
import Forgot from '../pages/Forgot/Forgot'

import Register from '../pages/Register/Register'

import Menu from '../pages/Alliance/Menu'
import Basic from '../pages/Alliance/Basic'
import Major from '../pages/Alliance/Major'
import Course from '../pages/Alliance/Course'
import Student from '../pages/Alliance/Student'
import Education from '../pages/Alliance/Education'
import Achieve from '../pages/Alliance/Achieve'
import Manager from '../pages/Alliance/Manager'
import Home from '../pages/Alliance/Home'

import store from '../store'

// 重写push|replace
// 保存VueRouter.push原方法
let originPush = VueRouter.prototype.push
// 参数: 路由跳转方向, 成功回调, 失败回调
VueRouter.prototype.push = function (location, resolve, reject) {
	if (resolve && reject) {
		// call
		originPush.call(this, location, resolve, reject)
	}
	else {
		originPush.call(this, location, () => { }, () => { })
	}
}
// 保存VueRouter.replace原方法
let originReplace = VueRouter.prototype.replace
// 参数: 路由跳转方向, 成功回调, 失败回调
VueRouter.prototype.replace = function (location, resolve, reject) {
	if (resolve && reject) {
		// call
		originReplace.call(this, location, resolve, reject)
	}
	else {
		originReplace.call(this, location, () => { }, () => { })
	}
}

let router = new VueRouter({
	routes: [
		{
			name: 'Login',
			path: '/login',
			component: Login,
			children: []
		},
		{
			name: 'Register',
			path: '/register',
			component: Register,
			children: []
		},
		{
			name: 'Forgot',
			path: '/forgot',
			component: Forgot,
			children: []
		},
		{
			name: 'Alliance',
			path: '/ ',
			component: Menu,
			redirect: '/home',
			children: [
				{
					name: 'Home',
					path: '/home',
					component: Home,
				},
				{
					name: 'Basic',
					path: '/user',
					meta: { keepAlive: true },
					component: Basic,
				},
				{
					name: 'Major',
					path: '/alliance/major',
					component: Major,
					meta: { keepAlive: false },
					children: [
						{
							name: 'Course',
							path: 'course',
							component: Course,
							meta: { keepAlive: false },
						}
					],
					beforeEnter: (to, from, next) => {
						if (store.state.user.userInfo.role != 'director') {
							next('/home')
						} else {
							next()
						}
					}
				},
				{
					name: 'Student',
					path: '/alliance/student',
					meta: { keepAlive: true },
					component: Student,
					beforeEnter: (to, from, next) => {
						if (store.state.user.userInfo.role != 'director') {
							next('/home')
						} else {
							next()
						}
					}
				},
				{
					name: 'Education',
					path: '/alliance/education',
					meta: { keepAlive: true },
					component: Education,
					beforeEnter: (to, from, next) => {
						if (store.state.user.userInfo.role == 'admin') {
							next('/home')
						} else {
							next()
						}
					}
				},
				{
					name: 'Achieve',
					path: '/alliance/achieve',
					meta: { keepAlive: true },
					component: Achieve,
					beforeEnter: (to, from, next) => {
						if (store.state.user.userInfo.role != 'teacher') {
							next('/home')
						} else {
							next()
						}
					}
				},
				{
					name: 'Manager',
					path: '/alliance/manager',
					meta: { keepAlive: true },
					component: Manager,
					beforeEnter: (to, from, next) => {
						if (store.state.user.userInfo.role != 'admin') {
							next('/home')
						} else {
							next()
						}
					}
				},
			]
		},
	]
})

// 全局前置守卫
router.beforeEach(async (to, from, next) => {
	document.title = 'EducationChain-Alliance'
	let token = store.state.user.token
	// 用户名已有, 即基础数据已有
	let name = store.state.user.userInfo.name
	if (token) {
		if (to.path == "/login" || to.path == '/register' || to.path == "/forgot" || to.path == "/alliance" || to.path == "/") {
			// 已经登录，想去登录页、忘记密码页面
			next("/home")
		} else {
			if (name) {
				next()
			} else {
				try {
					// 已经登录跳转系统内部页面, 若无用户信息, 请求基础数据
					await store.dispatch('user/basicInfo')
					next()
				}
				catch {
					// token失效
					// 清除token
					await store.dispatch('user/logout')
					next('/login')
				}
			}
		}
	} else {
		// 无token，未登录
		// 未登录不能去实际系统
		let toPath = to.path
		if (toPath.indexOf('/home') != -1 || toPath.indexOf('/user') != -1 || toPath.indexOf('/alliance') != -1) {
			//把未登录的时候想去而没有去成的信息, 存储于地址栏中【路由】
			next('/login?redirect=' + toPath);
		} else if (to.path == "/") {
			next("/login")
		}
		else {
			//去的不是系统---放行
			next()
		}
	}
})

export default router