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
        let pkey = payload.feedPkey;
        console.log(`retrieving items for pkey ${pkey}`);
        return AXIOS.get(`/items/${pkey}`).then(async response => {
            console.log(response);
            commit('setItems', {pkey: pkey, items: response.data})
        })
        .catch(e => {
            console.log(e)
        })
    }
};

// mutations
const mutations = {
    setItems(state, payload) {
        console.log(payload.items);
        state.all[payload.pkey] = payload.items
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

