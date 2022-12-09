import {
    reqMajor, reqAddMajor, reqDelMajor, reqAddMajors, reqMajorByType
} from '../api'


const state = {
    majorList: [],
    // majorList: [
    //     {
    //         majorId: "10000001",
    //         majorType: "本科",
    //         name: "计算机科学与技术",
    //     },
    //     {
    //         majorId: "10000002",
    //         majorType: "本科",
    //         name: "软件工程",
    //     },
    //     {
    //         majorId: "10000003",
    //         majorType: "硕士",
    //         name: "物联网",
    //     },
    //     {
    //         majorId: "10000004",
    //         majorType: "博士",
    //         name: "网络安全",
    //     },
    // ],
}

const mutations = {
    // 专业
    MAJOR_INFO(state, data) {
        state.majorList = data
    },
    // 添加专业
    ADD_MAJOR(state, major) {
        state.majorList.push(major)
    },
    // 删除专业
    DEL_MAJOR(state, majorId) {
        for (let i = 0; i < state.majorList.length; i++) {
            if (state.majorList[i].majorId == majorId) {
                state.majorList.splice(i, 1)
                break
            }
        }
    },
}

const actions = {
    // 专业
    async getMajor({ commit }) {
        let result = await reqMajor()
        if (result.code == 200) {
            commit("MAJOR_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 增加专业
    async addMajor({ commit }, major) {
        let result = await reqAddMajor(major)
        if (result.code == 200) {
            commit('ADD_MAJOR', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 删除专业
    async delMajor({ commit }, majorId) {
        let result = await reqDelMajor(majorId)
        if (result.code == 200) {
            commit('DEL_MAJOR', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 批量增加专业
    async addMajors({ commit }, majors) {
        let result = await reqAddMajors(majors)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 根据教育类型获取专业
    async getMajorByType(context, majorType) {
        let result = await reqMajorByType(majorType)
        if (result.code == 200) {
            return Promise.resolve(result.data)
        } else {
            return Promise.reject(new Error('faile'))
        }
    }
}

const getters = {}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}