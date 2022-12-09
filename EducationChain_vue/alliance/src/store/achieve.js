import {
    reqStudentAchieve, reqUpdateAchieve, reqAddAchieve, reqDownloadAchieve
} from '../api'


const state = {
    achieveList: null,
    // achieveList: [
    //     {
    //         id: "101",
    //         eduId: "10000000000000000001",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         acquireTime: "2020-01-05",
    //         type: "奖项",
    //         title: "绘蓝杯",
    //         remark: "123123",
    //         auditStatus: 2,
    //         feedback: "",
    //         certifyUri: ""
    //     },
    //     {
    //         id: "102",
    //         eduId: "10000000000000000002",
    //         personId: "450103200001010002",
    //         name: "张三",
    //         majorName: "物联网",
    //         eduType: "本科",
    //         acquireTime: "2020-01-05",
    //         type: "奖项",
    //         title: "绘蓝杯",
    //         remark: "123123",
    //         auditStatus: 2,
    //         feedback: "",
    //         certifyUri: ""
    //     },
    //     {
    //         id: "103",
    //         eduId: "10000000000000000003",
    //         personId: "450103200001010003",
    //         name: "李四",
    //         majorName: "软件工程",
    //         eduType: "硕士",
    //         acquireTime: "2020-01-06",
    //         type: "创新实践活动",
    //         title: "外企实践",
    //         remark: "456456",
    //         auditStatus: 1,
    //         feedback: "",
    //         certifyUri: ""
    //     },
    //     {
    //         id: "104",
    //         eduId: "10000000000000000004",
    //         personId: "450103200001010004",
    //         name: "王五",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         acquireTime: "2020-01-08",
    //         type: "社会实践活动",
    //         title: "扫地",
    //         remark: "789789",
    //         auditStatus: 0,
    //         feedback: "",
    //         certifyUri: ""
    //     },
    // ]
}

const mutations = {
    // 学生成就信息
    ACHIEVE_INFO(state, data) {
        state.achieveList = data
    },
    // 修改审核状态
    CHANGE_ACHIEVE_STATE(state, value) {
        for (let i = 0; i < state.achieveList.length; i++) {
            if (state.achieveList[i].id == value.id) {
                if (value.auditStatus == 1) {
                    state.achieveList[i].auditStatus = 1
                }
                else {
                    state.achieveList[i].auditStatus = 0
                }
                break
            }

        }
    },
    // 修改审核信息
    CHANGE_ACHIEVE_TEXT(state, value) {
        for (let i = 0; i < state.achieveList.length; i++) {
            if (state.achieveList[i].id == value.id) {
                state.achieveList[i].feedback = value.e
                break
            }
        }
    },

}

const actions = {
    // 联盟学生成就经历
    async getStudentAchieve({ commit }) {
        let result = await reqStudentAchieve()
        if (result.code == 200) {
            commit("ACHIEVE_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 改变审核条例状态
    async updateAchieve({ commit }, { id, auditStatus, formData }) {
        let result = await reqUpdateAchieve(formData)
        if (result.code == 200) {
            commit("CHANGE_ACHIEVE_STATE", { id, auditStatus })
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 添加成就
    async addAchieve(context, achieve) {
        let result = await reqAddAchieve(achieve)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 下载验证文件
    async downloadAchieve(context, file) {
        let result = await reqDownloadAchieve(file)
        if (true) {
            return Promise.resolve(result)
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