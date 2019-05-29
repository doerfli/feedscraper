import Vue from 'vue'
import Vuex from 'vuex'
import feeds from "./stores/feeds";
import entries from "./stores/entries";
import session from "./stores/session";

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production'

export default new Vuex.Store({
  modules: {
    feeds,
    entries,
    session
  },
  state: {

  },
  mutations: {

  },
  actions: {

  },
  strict: debug
})
