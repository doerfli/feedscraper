import {AXIOS} from "@/http-common"

// initial state
const state = {
    all: []
};

// getters
const getters = {};

// actions
const actions = {
    getAll({ commit }) {
        AXIOS.get(`/feeds`).then(response => {
            console.log(response);
            commit('setAll', response.data)
        })
        .catch(e => {
            console.log(e)
        })
    }
};

// mutations
const mutations = {
    setAll(state, feeds) {
        state.all = feeds
    },
    add(state, feed) {
        state.all.push(feed)
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

