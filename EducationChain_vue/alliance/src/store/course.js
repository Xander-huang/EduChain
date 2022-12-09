import {
    reqCourse, reqAddCourse, reqDelCourse, reqUpdateCourse, reqAddCourses, reqAddStudentCourse, reqDelStudentCourse
} from '../api'


const state = {
    courseList: [],
    // courseList: [
    //     {
    //         courseId: "10000000001",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000002",
    //         name: "软件工程",
    //         credit: 3
    //     },
    //     {
    //         courseId: "10000000003",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000004",
    //         name: "软件工程",
    //         credit: 3
    //     },
    //     {
    //         courseId: "10000000005",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000006",
    //         name: "软件工程",
    //         credit: 3
    //     },
    //     {
    //         courseId: "10000000007",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000008",
    //         name: "软件工程",
    //         credit: 3
    //     },
    //     {
    //         courseId: "10000000009",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000010",
    //         name: "软件工程",
    //         credit: 3
    //     },
    //     {
    //         courseId: "10000000011",
    //         name: "计算机体系结构",
    //         credit: 2
    //     },
    //     {
    //         courseId: "10000000012",
    //         name: "软件工程",
    //         credit: 3
    //     },
    // ],
    creditList: [0, 1, 2, 3, 4, 5, 6, 7],
}

const mutations = {
    // 课程
    COURSE_INFO(state, data) {
        state.courseList = data
    },
    // 添加课程
    ADD_COURSE(state, course) {
        state.courseList.push(course)
    },
    // 删除课程
    DEL_COURSE(state, courseId) {
        for (let i = 0; i < state.courseList.length; i++) {
            if (state.courseList[i].courseId == courseId) {
                state.courseList.splice(i, 1)
                break
            }
        }
    },
    // 添加editable属性
    GET_EDITABLE(state, courseId) {
        for (let i = 0; i < state.courseList.length; i++) {
            if (state.courseList[i].courseId == courseId) {
                state.courseList[i].editable = true
                break
            }
        }
    },
    // 删除editable属性
    DEL_EDITABLE(state, courseId) {
        for (let i = 0; i < state.courseList.length; i++) {
            if (state.courseList[i].courseId == courseId) {
                delete state.courseList[i].editable
                break
            }
        }
    },
}

const actions = {
    // 获取课程
    async getCourse({ commit }, majorId) {
        if (majorId != undefined) {
            let result = await reqCourse(majorId)
            if (result.code == 200) {
                commit("COURSE_INFO", result.data)
                return Promise.resolve(result.data)
            } else {
                return Promise.reject(new Error('faile'))
            }
        }
    },
    // 增加课程
    async addCourse({ commit }, course) {
        let result = await reqAddCourse(course)
        if (result.code == 200) {
            commit('ADD_COURSE', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 删除课程
    async delCourse({ commit }, courseId) {
        let result = await reqDelCourse(courseId)
        if (result.code == 200) {
            commit('DEL_COURSE', result.data)
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 修改课程信息
    async updateCourse(context, course) {
        let result = await reqUpdateCourse(course)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 批量增加课程
    async addCourses(context, courses) {
        let result = await reqAddCourses(courses)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 向学生添加单个课程
    async addStudentCourse(context, course) {
        let result = await reqAddStudentCourse(course)
        if (result.code == 200) {
            return 'ok'
        } else {
            return Promise.reject(new Error('faile'))
        }
    },
    // 向学生删除单个课程
    async delStudentCourse(context, course) {
        let result = await reqDelStudentCourse(course)
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