import {
    reqManager, reqAddManager, reqDelManager, reqUpdateManager, reqAddManagers, reqResetManager
} from '../api'


const state = {
    managerList: null,
    // managerList: [
    //     {
    //         account: "181818001",
    //         name: "张三",
    //         email: "123123@qq.com",
    //         phone: "13344445555",
    //         role: "admin",
    //     },
    //     {
    //         account: "181818002",
    //         name: "张三",
    //         email: "456456@qq.com",
    //         phone: "13355556666",
    //         role: "director",
    //     },
    //     {
    //         account: "181818003",
    //         name: "张三",
    //         email: "789789789@qq.com",
    //         phone: "13366667777",
    //         role: "teacher",
    //     },
    // ]
}

const mutations = {
    // 联盟用户
    MANAGER_INFO(state, newData) {
        state.managerList = newData
    },
    // 添加用户
    ADD_MANAGER(state, manager) {
        state.managerList.push(manager)
    },
    // 删除用户
    DEL_MANAGER(state, account) {
        for (let i = 0; i < state.managerList.length; i++) {
            if (state.managerList[i].account == account) {
                state.managerList.splice(i, 1)
                break
            }
        }
    },
    // 添加editable属性
    GET_EDITABLE(state, account) {
        for (let i = 0; i < state.managerList.length; i++) {
            if (state.managerList[i].account == account) {
                state.managerList[i].editable = true
                break
            }
        }
    },
    // 删除editable属性
    DEL_EDITABLE(state, account) {
        for (let i = 0; i < state.managerList.length; i++) {
            if (state.managerList[i].account == account) {
                delete state.managerList[i].editable
                break
            }
        }
    }
}

const actions = {
    // 联盟用户
    async getManager({ commit }) {
        let result = await reqManager()
        if (result.code == 200) {
            commit("MANAGER_INFO", result.data)
        }
    },
    // 增加用户
    async addManager({ commit }, manager) {
        let result = await reqAddManager(manager)
        console.log(result)
        if (result.code == 200) {
            commit('ADD_MANAGER', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 删除用户
    async delManager({ commit }, manager) {
        let result = await reqDelManager(manager)
        if (result.code == 200) {
            commit('DEL_MANAGER', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改用户信息，传回数据库
    async updateManager(context, manager) {
        let result = await reqUpdateManager(manager)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 批量增加课程
    async addManagers(context, managers) {
        let result = await reqAddManagers(managers)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 重置密码
    async resetManager(context, manager) {
        let result = await reqResetManager(manager)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
}

const getters = {}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}