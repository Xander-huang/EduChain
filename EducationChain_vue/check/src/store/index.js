import Vue from "vue"
import Vuex from 'vuex'

import check from './check'
import user from './user'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        check,
        user
    }
})