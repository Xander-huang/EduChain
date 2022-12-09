import Vue from "vue"
import Vuex from 'vuex'

import user from './user'
import major from './major'
import course from './course'
import student from './student'
import education from './education'
import achieve from './achieve'
import manager from './manager'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        user,
        major,
        course,
        student,
        education,
        achieve,
        manager
    }
})