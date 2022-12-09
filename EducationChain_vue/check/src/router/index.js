import VueRouter from 'vue-router'
import Register from "../pages/Register";
import Home from "../pages/Home"
import Login from "../pages/Login"
import authorize from "../pages/authorize"
// import App from "../App";
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
    routes:[
        {
            path:'/',
            redirect:'Login'
        },
        {
			name: 'Login',
			path: '/login',
			component: Login,
            
		},
        {
            name:'Register',
            path:'/register',
            component:Register,
        },
        {
            name:'Home',
            path:'/home',
            component:Home,
        },
        {
            name:'Authorize',
            path:'/authorize/:id',
            component:authorize,
        }
    ]
})


// // 全局前置守卫
// router.beforeEach(async (to, from, next) => {
// 	// 优化处理: token可持久存在, 会自动更新, 直接退出界面不会删除token
// 	// 状态: token name
// 	// 未登录:              无token, 无name, 无token请求name, 先判断token后可确定一路分支
// 	// 已经登录:            有token, 无name, 初次进入或刷新界面损失name, 有token可再请求name来判断
// 	// 已经登录, 未选择退出: 有token, 无name, 初次进入系统, 有token必须再请求name来判断

// 	// 原模式默认清除token, 首次登录无token
// 	// 更新: 后端目前不做持久化token
// 	// 目前处理: 销毁时移除token

// 	document.title = 'EducationChain-Checker'
// 	let token = store.state.user.token
// 	// 用户名已有, 即基础数据已有
// 	let name = store.state.user.userInfo.name
// 	if (token) {
// 		if (to.path == "/login" || to.path == '/register' || to.path == "/forgot" || to.path == "/" || to.path == "/person") {
// 			// 已经登录，想去登录页、注册、忘记密码页面
// 			next("/home")
// 		} else {
// 			if (name) {
// 				next()
// 			} else {
// 				try {
// 					// 已经登录跳转系统内部页面, 若无用户信息, 请求基础数据
// 					await store.dispatch('user/basicInfo')
// 					next()
// 				}
// 				catch {
// 					// token失效
// 					// 清除token
// 					await store.dispatch('user/logout')
// 					next('/login')
// 				}
// 			}
// 		}
// 	} else {
// 		// 无token，未登录
// 		// 未登录不能去实际系统
// 		let toPath = to.path
// 		if (toPath.indexOf('/home') != -1 || toPath.indexOf('/user') != -1 || toPath.indexOf('/person') != -1) {
// 			//把未登录的时候想去而没有去成的信息, 存储于地址栏中【路由】
// 			next('/login?redirect=' + toPath);
// 		} else if (to.path == "/") {
// 			next("/login")
// 		} else {
// 			//去的不是系统---放行
// 			next()
// 		}
// 	}
// })
export default router