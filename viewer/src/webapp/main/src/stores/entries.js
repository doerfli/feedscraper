import {AXIOS} from "@/http-common"

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
        console.log(`retrieving entries for pkey ${pkey}`);
        return AXIOS.get(`/entries/${pkey}`).then(async response => {
            console.log(response);
            commit('setEntries', {pkey: pkey, entries: response.data})
        })
        .catch(e => {
            console.log(e)
        })
    }
};

// mutations
const mutations = {
    setEntries(state, payload) {
        console.log(payload.entries);
        state.all[payload.pkey] = payload.entries
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

