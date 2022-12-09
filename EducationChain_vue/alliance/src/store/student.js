import {
    reqStudent, reqAddStudent, reqDelStudent, reqUpdateStudent, reqAddStudents, reqCertificate,  reqUplink, reqStudentId
} from '../api'


const state = {
    studentList: [],
    // studentList: [
    //     {
    //         stuId: "10000000000000000001",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000002",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000003",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000004",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000005",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000006",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000007",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000008",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000009",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000010",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000011",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000012",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000013",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000014",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000015",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000016",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000017",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000018",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000019",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000020",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000021",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000022",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000023",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000024",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000025",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000026",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000027",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000028",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000029",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000030",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000031",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000032",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //     },
    //     {
    //         stuId: "10000000000000000033",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000034",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000035",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "物联网",
    //         eduType: "硕士",
    //         isUplinked: 1,
    //     },
    //     {
    //         stuId: "10000000000000000036",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 1,
    //     },
    // ],
}

const mutations = {
    // 学生
    STUDENT_INFO(state, data) {
        state.studentList = data
    },
    // 添加学生
    ADD_STUDENT(state, student) {
        state.studentList.push(student)
    },
    // 删除学生
    DEL_STUDENT(state, stuId) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == stuId) {
                state.studentList.splice(i, 1)
                break
            }
        }
    },
    // 修改学生数据
    UPDATE_STUDENT(state, student) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == student.origin) {
                delete state.studentList[i].editable
                state.studentList[i].stuId = student.new
                break
            }
        }
    },
    // 学生数据上链
    UPLINK_STUDENT(state, students) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (students.indexOf(state.studentList[i].stuId) != -1) {
                state.studentList[i].isUplinked = 1
            }
        }
    },
    // 添加editable属性
    GET_EDITABLE(state, stuId) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == stuId) {
                state.studentList[i].editable = true
                break
            }
        }
    },
    // 删除editable属性
    DEL_EDITABLE(state, stuId) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == stuId) {
                delete state.studentList[i].editable
                break
            }
        }
    },
}

const actions = {
    // 学生
    async getStudent({ commit }) {
        let result = await reqStudent()
        if (result.code == 200) {
            if (result.data == "该机构还未注册学生") {
                return Promise.resolve('noData')
            } else {
                commit("STUDENT_INFO", result.data)
                return 'ok'
            }
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 增加学生
    async addStudent({ commit }, student) {
        let result = await reqAddStudent(student)
        if (result.code == 200) {
            commit('ADD_STUDENT', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 删除学生
    async delStudent({ commit }, stuId) {
        let result = await reqDelStudent(stuId)
        if (result.code == 200) {
            commit('DEL_STUDENT', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改学生信息
    async updateStudent({ commit }, student) {
        let result = await reqUpdateStudent(student)
        if (result.code == 200) {
            commit("UPDATE_STUDENT", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 批量增加学生
    async addStudents(context, students) {
        let result = await reqAddStudents(students)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 学生毕业证书
    async addCertificate(context, data) {
        let result = await reqCertificate(data)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 学生数据上链
    async uplinkStudent({ commit }, students) {
        let result = await reqUplink(students)
        if (result.code == 200) {
            commit("UPLINK_STUDENT", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 根据personId获取教育经历id
    async getStudentId(context, personId) {
        let result = await reqStudentId(personId)
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