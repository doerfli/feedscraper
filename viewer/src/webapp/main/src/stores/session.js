
// initial state
const state = {
    activeFeed: -1,
    token: null
};

// getters
const getters = {};

// actions
const actions = {
    async setActiveFeed({ commit }, payload) {
        await this.dispatch('items/getByFeed', { feedPkey: payload.pkey});
        commit('setActiveFeed', { pkey: payload.pkey})
    },
    setToken({commit}, payload) {
        commit('setToken', { token: payload.token })
    }
};

// mutations
const mutations = {
    setActiveFeed(state, payload) {
        state.activeFeed = payload.pkey;
    },
    setToken(state, payload) {
        state.token = payload.token;
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

