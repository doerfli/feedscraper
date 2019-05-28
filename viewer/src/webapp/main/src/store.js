import Vue from 'vue'
import Vuex from 'vuex'
import feeds from "./stores/feeds";

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  modules: {
    feeds
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  strict: debug
})
