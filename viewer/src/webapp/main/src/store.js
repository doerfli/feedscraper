import Vue from 'vue'
import Vuex from 'vuex'
import feeds from "./stores/feeds";
import items from "./stores/items";
import session from "./stores/session";
import messages from "./stores/messages";
import users from "./stores/users";

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
    modules: {
        feeds,
        items,
        messages,
        session,
        users
    },
    state: {},
    mutations: {},
    actions: {},
    strict: debug
})
