import {
    reqCaptchaImg, repEmailCaptcha,
    reqLogin, reqLogout, reqRegister, reqReset,
    reqBasicInfo, reqUpdatePassword, reqUpdatePhone,
    reqHomeInfo
} from '../api'
import { setToken, getToken, removeToken } from '../utils/token'


const state = {
    img: "",
    imgKey: "",
    token: getToken(),
    userInfo: {
        // nodeName: "北京大学",
        // type: 0,
        // name: "张三",
        // phone: "18815898671",
        // email: "zymail68@foxmail.com",
        // role: "admin",
    },
    homeInfo: {
        student: 0,
        major: 0,
        course: 0,
        uplinked: 0,
        annualEnrollment: {
            subscript: null,
            data: null,
        },
        annualGraduation: {
            subscript: null,
            data: null,
        },
        annualMajor: {
            subscript: null,
            data: null,
        },
        annualCourse: {
            subscript: null,
            data: null,
        },
        majorAverage: {
            subscript: null,
            data: null,
        },
        majorNum: {
            subscript: null,
            data: null,
        },
        education: [0, 0, 0, 0],
    }
}

const mutations = {
    // 存储登录验证图片
    SAVE_CAPTCHA_IMG(state, ver) {
        state.img = ver.img
        state.imgKey = ver.imgKey
    },
    // 存储token
    USER_LOGIN(state, token) {
        state.token = token;
    },
    // 获得用户基本信息
    USER_INFO(state, userInfo) {
        state.userInfo = userInfo;
    },
    // 获得首页基本信息
    HOME_INFO(state, homeInfo) {
        state.homeInfo = homeInfo;
        state.homeInfo.annualEnrollment = homeInfo.annualEnrollment || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.annualGraduation = homeInfo.annualGraduation || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.annualMajor = homeInfo.annualMajor || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.majorAverage = homeInfo.majorAverage || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.annualCourse = homeInfo.annualCourse || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.majorNum = homeInfo.majorNum || {
            subscript: [0],
            data: [0],
        }
        state.homeInfo.education = homeInfo.education || [0, 0, 0, 0]
    },
    // 退出
    CLEAR(state) {
        //帮仓库中先关用户信息清空
        state.token = '';
        state.userInfo = {};
        //本地存储数据清空
        removeToken();
    }
}

const actions = {
    // 图片验证码
    async captchaImg({ commit }) {
        let result = await reqCaptchaImg()
        if (result.code == 200) {
            commit("SAVE_CAPTCHA_IMG", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 邮箱验证码
    async emailCaptcha({ commit }, email) {
        console.log(email)
        let result = await repEmailCaptcha(email)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },

    // 登录                     
    async login({ commit }, user) {
        let result = await reqLogin(user)
        if (result.code == 200) {
            //用户已经登录成功且获取到token
            commit("USER_LOGIN", result.data);
            // 持久化存储token
            setToken(result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 退出
    async logout(context) {
        let result = await reqLogout()
        if (result.code == 200) {
            context.commit("CLEAR")
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 注册
    async register({ commit }, alliance) {
        let result = await reqRegister(alliance)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 重置密码
    async reset({ commit }, pwd) {
        let result = await reqReset(pwd)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 获得个人基本信息
    async basicInfo({ commit }) {
        let result = await reqBasicInfo()
        if (result.code == 200) {
            commit("USER_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改个人电话
    async updatePhone({ dispatch }, phone) {
        let result = await reqUpdatePhone(phone)
        if (result.code == 200) {
            dispatch("basicInfo")
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改密码
    async updatePassword({ commit }, pwd) {
        let result = await reqUpdatePassword(pwd)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 获取首页基本信息
    async getHomeInfo({ commit }) {
        let result = await reqHomeInfo()
        if (result.code == 200) {
            commit("HOME_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
}

const getters = {
}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}