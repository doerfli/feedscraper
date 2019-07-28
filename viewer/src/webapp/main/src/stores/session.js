// initial state
import * as localforage from "localforage";
import router from '@/router';

const state = {
    activeFeed: -1,
    token: null,
    username: ""
};

// getters
const getters = {};

// actions
const actions = {
    async setActiveFeed({ commit }, payload) {
        await this.dispatch('items/initialize', { feedPkey: payload.pkey});
        commit('setActiveFeed', { pkey: payload.pkey});
    },
    setToken({commit}, payload) {
        commit('setToken', { token: payload.token, username: payload.username });
    },
    logout({commit}) {
        commit('setToken', { token: null});
        router.push({name: "login"});
    }
};

// mutations
const mutations = {
    setActiveFeed(state, payload) {
        state.activeFeed = payload.pkey;
    },
    setToken(state, payload) {
        state.token = payload.token;
        if (payload.token) {
            localforage.setItem('token', payload.token);
        } else {
            localforage.removeItem('token');
        }
        state.username = payload.username;
        if (payload.username) {
            localforage.setItem('username', payload.username);
        } else {
            localforage.removeItem('username');
        }
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

