import { reqEducation, reqUpLinkEdu, reqInfoKey, reqDownload } from '../api'


const state = {
    educationList: [],
    // educationList: [
    //     {
    //         eduId: "123123",
    //         nodeName: "桂林电子科技大学",
    //         type: "本科",
    //         majorName: "计算机科学与技术",
    //         beginTime: "2020-01-01",
    //         endTime: "2020-01-02",
    //         course: [
    //             {
    //                 courseId: "32131231",
    //                 name: "软件工程",
    //                 credit: 3,
    //                 certifyUri: "www.4399.com"
    //             },
    //             {
    //                 courseId: "32131232",
    //                 name: "操作系统",
    //                 credit: 3,
    //                 certifyUri: "www.4399.com"
    //             }
    //         ]
    //     },
    //     {
    //         eduId: "123124",
    //         nodeName: "桂林电子科技大学",
    //         type: "硕士",
    //         majorName: "人工智能",
    //         beginTime: "2024-01-01",
    //         endTime: "2026-01-02",
    //         course: [
    //             {
    //                 courseId: "32131233",
    //                 name: "深度学习",
    //                 credit: 3,
    //                 certifyUri: "www.4399.com"
    //             }
    //         ]
    //     }
    // ]
}

const mutations = {
    EDUCATION_LIST(state, data) {
        state.educationList = data
    }
}

const actions = {
    // 获取个人成绩信息
    async getEducation({ commit }) {
        let result = await reqEducation()
        if (result.code == 200) {
            if (result.data == "该用户暂不存在教育经历.") {
                return 'noData'
            } else {
                commit("EDUCATION_LIST", result.data)
                return 'ok'
            }
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 导出个人成绩验证码，邮箱收取
    async infoKey({ commit }, eduId) {
        let result = await reqInfoKey(eduId)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 获取个人上链信息
    async getUpLinkEducation({ commit }) {
        let result = await reqUpLinkEdu()
        if (result.code == 200) {
            if (result.data == null) {
                return 'noData'
            } else {
                return Promise.resolve(result.data)
            }
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 下载证明文件
    async getDownload({ commit }, file) {
        let result = await reqDownload(file)
        if (true) {
            return Promise.resolve(result)
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