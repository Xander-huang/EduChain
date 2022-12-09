import {
    reqStudentEducation, reqUpdateStudentEducation, reqUpdateStudentEducations
} from '../api'


const state = {
    studentList: null,
    // studentList: [
    //     {
    //         stuId: "10000000000000000001",
    //         personId: "450103200001010001",
    //         name: "张三",
    //         majorName: "计算机科学与技术",
    //         eduType: "本科",
    //         isUplinked: 0,
    //         course: [
    //             {
    //                 courseId: "18000301401",
    //                 stuId: "10000000000000000001",
    //                 isUplinked: 0,
    //                 name: "计算机组成原理",
    //                 credit: 5,
    //                 score: null,
    //             },
    //             {
    //                 courseId: "18000301402",
    //                 stuId: "10000000000000000001",
    //                 isUplinked: 0,
    //                 name: "操作系统",
    //                 credit: 5,
    //                 score: 100,
    //             },
    //             {
    //                 courseId: "18000301403",
    //                 stuId: "10000000000000000001",
    //                 isUplinked: 0,
    //                 name: "计算机体系结构",
    //                 credit: 5,
    //                 score: 100,
    //             },
    //         ]
    //     },
    //     {
    //         stuId: "100000000000000000002",
    //         personId: "450103200001010002",
    //         name: "李四",
    //         majorName: "软件工程",
    //         eduType: "本科",
    //         isUplinked: 1,
    //         course: [
    //             {
    //                 courseId: "18000301411",
    //                 stuId: "100000000000000000002",
    //                 isUplinked: 1,
    //                 name: "计算机组成原理",
    //                 credit: 5,
    //                 score: 80,
    //             },
    //             {
    //                 courseId: "18000301412",
    //                 stuId: "100000000000000000002",
    //                 isUplinked: 1,
    //                 name: "操作系统",
    //                 credit: 5,
    //                 score: 80,
    //             },
    //             {
    //                 courseId: "18000301413",
    //                 stuId: "100000000000000000002",
    //                 isUplinked: 1,
    //                 name: "计算机体系结构",
    //                 credit: 5,
    //                 score: 80,
    //             },
    //         ]
    //     },
    //     {
    //         stuId: "100000000000000000003",
    //         personId: "450103200001010003",
    //         name: "王五",
    //         majorName: "软件工程",
    //         eduType: "硕士",
    //         isUplinked: 0,
    //         course: [
    //             {
    //                 courseId: "18000301421",
    //                 stuId: "100000000000000000003",
    //                 isUplinked: 0,
    //                 name: "计算机组成原理",
    //                 credit: 5,
    //                 score: 70
    //             },
    //             {
    //                 courseId: "18000301422",
    //                 stuId: "100000000000000000003",
    //                 isUplinked: 0,
    //                 name: "操作系统",
    //                 credit: 5,
    //                 score: 70
    //             },
    //             {
    //                 courseId: "1800301423",
    //                 stuId: "100000000000000000003",
    //                 isUplinked: 0,
    //                 name: "计算机体系结构",
    //                 credit: 5,
    //                 score: 70
    //             },
    //         ]
    //     },
    //     {
    //         stuId: "100000000000000000004",
    //         personId: "450103200001010004",
    //         name: "赵六",
    //         majorName: "软件工程",
    //         eduType: "博士",
    //         isUplinked: 0,
    //         course: [
    //             {
    //                 courseId: "18000301431",
    //                 stuId: "100000000000000000004",
    //                 isUplinked: 0,
    //                 name: "计算机组成原理",
    //                 credit: 3,
    //                 score: 80,
    //             },
    //             {
    //                 courseId: "18000301432",
    //                 stuId: "100000000000000000004",
    //                 isUplinked: 0,
    //                 name: "操作系统",
    //                 credit: 3,
    //                 score: 80,
    //             },
    //             {
    //                 courseId: "18000301433",
    //                 stuId: "100000000000000000004",
    //                 isUplinked: 0,
    //                 name: "计算机体系结构",
    //                 credit: 3,
    //                 score: 80,
    //             },
    //         ]
    //     },
    // ]
}

const mutations = {
    // 学生及其相关成绩
    EDUCATION_INFO(state, data) {
        // for (let i = 0; i < data.length; i++) {
        //     for (let j = 0; j < data[i].course.length; j++) {
        //         if (data[i].course[j].score == null) {
        //             data[i].course[j].score = 0;
        //         }
        //     }
        // }
        state.studentList = data
    },
    // 添加课程
    ADD_COURSE(state, course) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == course.stuId) {
                state.studentList[i].course.push(course)
                break
            }
        }
    },
    // 删除课程
    DEL_COURSE(state, course) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == course.stuId) {
                for (let j = 0; j < state.studentList[i].course.length; j++) {
                    if (state.studentList[i].course[j].courseId = course.courseId) {
                        state.studentList[i].course.splice(j, 1)
                        break
                    }
                }
                break
            }
        }
    },
    // 添加editable属性
    GET_EDITABLE(state, key) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == key) {
                state.studentList[i].editable = true
                for (let j = 0; j < state.studentList[i].course.length; j++) {
                    state.studentList[i].course[j].editable = true
                }
                break
            }
        }
    },
    // 删除editable属性
    DEL_EDITABLE(state, key) {
        for (let i = 0; i < state.studentList.length; i++) {
            if (state.studentList[i].stuId == key) {
                delete state.studentList[i].editable
                for (let j = 0; j < state.studentList[i].course.length; j++) {
                    delete state.studentList[i].course[j].editable
                }
                break
            }
        }
    },
}

const actions = {
    // 学生及其相关成绩
    async getStudentEducation({ commit }) {
        let result = await reqStudentEducation()
        if (result.code == 200) {
            commit("EDUCATION_INFO", result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改学生成绩
    async updateStudentEducation(context, { stuId, courseList }) {
        let course = courseList
        let result = await reqUpdateStudentEducation({ stuId, course })
        console.log(result)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 批量修改学生成绩
    async updateStudentEducations(context, students) {
        let result = await reqUpdateStudentEducations(students)
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