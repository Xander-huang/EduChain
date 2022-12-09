import { reqAchieve, reqAlliance, reqCertificationForm } from '../api'


const state = {
    achieveList: [],
    allianceList: []
}

const mutations = {
    // 存储用户成就信息
    ACHIEVE_LIST(state, achieveList) {
        for (let i = 0; i < achieveList.length; i++) {
            achieveList[i].createTime = achieveList[i].createTime.slice(0, 10)
        }
        state.achieveList = achieveList
    },
    // 获得用户所学机构列表
    USER_ALLIANCE(state, alliance) {
        state.allianceList = alliance;
    },
}

const actions = {
    // 获取个人用户成就
    async getAchieve({ commit }) {
        let result = await reqAchieve()
        if (result.code == 200) {
            if (result.data == "该用户未存在教育记录") {
                commit("ACHIEVE_LIST", [])
                return 'noData'
            } else {
                commit("ACHIEVE_LIST", result.data)
                return 'ok'
            }
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 获得个人用户所学机构
    async getAlliance({ commit }) {
        let result = await reqAlliance()
        if (result.code == 200) {
            if (result.data == "该用户尚未参与机构学习") {
                commit("USER_ALLIANCE", [])
                return 'noData'
            }
            else {
                commit("USER_ALLIANCE", result.data)
                return 'ok'
            }
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 发送个人证明表单
    async certificationForm(context, form) {
        let result = await reqCertificationForm(form)
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