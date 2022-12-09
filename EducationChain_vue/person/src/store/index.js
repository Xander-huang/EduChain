import Vue from "vue"
import Vuex from 'vuex'

import user from './user'
import education from './education'
import achieve from './achieve'
Vue.use(Vuex)

export default new Vuex.Store({
    modules:{
        user,
        education,
        achieve
    }
})