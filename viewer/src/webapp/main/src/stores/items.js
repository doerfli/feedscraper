import AXIOS from "@/http-common";

// initial state
const state = {
    all: {}
};

// getters
const getters = {};

// actions
const actions = {
    async getByFeed({ commit }, payload) {
        let feedPkey = payload.feedPkey;
        console.log(`retrieving items for pkey ${feedPkey}`);
        return AXIOS.get(`/items/${feedPkey}`).then(async response => {
            // console.log(response);
            commit('setItems', {feedPkey: feedPkey, items: response.data})
        })
        .catch(e => {
            console.log(e)
        })
    },
    async markAsRead({commit}, payload) {
        let itemPkey = payload.itemPkey;
        console.log(`marking item ${itemPkey} as read`);
        AXIOS.post(`/items/${itemPkey}/read`);
        commit('markAsRead', {pkey: itemPkey, read: true, feedPkey: payload.feedPkey, index: payload.index})
    }
};

// mutations
const mutations = {
    setItems(state, payload) {
        // console.log(payload.items);
        state.all[payload.feedPkey] = payload.items
    },
    markAsRead(state, payload) {
        state.all[payload.feedPkey][payload.index].read = payload.read
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

