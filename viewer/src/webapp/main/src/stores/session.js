
// initial state
const state = {
    activeFeed: -1
};

// getters
const getters = {};

// actions
const actions = {
    async setActiveFeed({ commit }, payload) {
        await this.dispatch('items/getByFeed', { feedPkey: payload.pkey});
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

