import { reqDataKey } from '../api'

const state = {
    // checkInfo: null,
    checkInfo: {
    },
}

const mutations = {
    // 存储查询信息
    SAVE_CHECK_INFO(state, info) {
        state.checkInfo = info
    },
    // 清空数据
    CLEAR(state, info) {
        state.checkInfo = {}
    }
}

const actions = {
    // 校验区块链上个人用户数据
    async dataKey({ commit }, key) {
        let result = await reqDataKey(key)
        if (result.code == 200) {
            commit("SAVE_CHECK_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error(result.code))
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