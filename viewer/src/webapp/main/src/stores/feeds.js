import {AXIOS} from "@/http-common"

// initial state
const state = {
    all: []
};

// getters
const getters = {};

// actions
const actions = {
    async getAll({ commit }) {
        return AXIOS.get(`/feeds`).then(async response => {
            console.log(response);
            commit('setAll', response.data);
            if (response.data.length > 0) {
                console.log(response.data[0].pkey);
                await this.dispatch('entries/getByFeed', { feedPkey: response.data[0].pkey});
                this.dispatch('session/setActiveFeed', { pkey: response.data[0].pkey});
            }
        })
        .catch(e => {
            console.log(e)
        })
    }
};

// mutations
const mutations = {
    setAll(state, feeds) {
        state.all = feeds;
    },
    add(state, feed) {
        state.all.push(feed);
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

