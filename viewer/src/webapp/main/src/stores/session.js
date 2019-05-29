
// initial state
const state = {
    activeFeed: 0
};

// getters
const getters = {};

// actions
const actions = {
    setActiveFeed({ commit }, payload) {
        commit('setActiveFeed', { pkey: payload.pkey})
    }
};

// mutations
const mutations = {
    setActiveFeed(state, payload) {
        state.activeFeed = payload.pkey
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

