import AXIOS from "@/http-common";
import {TIMEOUT} from "../messages-common";
import _ from "lodash";

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
            // console.log(response);
            commit('setAll', response.data);
            if (response.data.length > 0) {
                // console.log(response.data[0].pkey);
                // enable first feed
                this.dispatch('session/setActiveFeed', { pkey: response.data[0].pkey});
            }
        })
        .catch(e => {
            console.log(e)
        })
    },
    async pushFeedToServer(undefined, payload) {
        console.log(payload);
        AXIOS.post(`/feeds`, {
            url: payload.url
        }).then(async response => {
            console.log(response.status);
            this.dispatch('messages/add', {
                text: "New feed added - It will appear in the feed list after it was downloaded for the first time. This might take up to a minute.",
                type: "notification",
                timeout: TIMEOUT
            });
        });
    },
    async decreaseUnread({commit}, payload) {
        commit('changeUnread', { feedPkey: payload.feedPkey, amount: -1})
    },
    async increaseUnread({commit}, payload) {
        commit('changeUnread', { feedPkey: payload.feedPkey, amount: 1})
    },
    async updatedItems({commit}, payload) {
        let feedPkey = payload['pkey'];
        console.log(feedPkey);
        AXIOS.get(`/feeds/${feedPkey}`).then(async response => {
            // console.log(response.status);
            // console.log(response.data);
            commit('updatedItems', response.data)
        });
    },
    async addNew({commit}, payload) {
        let feedPkey = payload['pkey'];
        console.log(feedPkey);
        AXIOS.get(`/feeds/${feedPkey}`).then(async response => {
            // console.log(response.status);
            console.log(response.data);
            commit('add', response.data)
        });
    },
    async updatedItemsFalse({commit}, payload) {
        console.log("blaaaa");
        commit('updatedItemsFalse', payload)
    }
};

// mutations
const mutations = {
    setAll(state, feeds) {
        state.all = feeds;
        _.forEach(state.all, function(e) {
            e.hasUpdatedItems = "no";
        });
    },
    add(state, feed) {
        feed.hasUpdatedItems = "yes";
        state.all.push(feed);
        state.all = _.sortBy(state.all, (e) => { return e.title.toLowerCase(); })
    },
    changeUnread(state, payload) {
        let feed = _.find(state.all, function(e) { return e.pkey === payload.feedPkey });
        feed.unreadItems += payload.amount;
    },
    updatedItems(state, payload) {
        let feed = _.find(state.all, function(e) { return e.pkey === payload.pkey });
        feed.unreadItems = payload.unreadItems;
        feed.hasUpdatedItems = "yes";
    },
    updatedItemsFalse(state, payload) {
        let feed = _.find(state.all, function(e) { return e.pkey === payload.pkey });
        feed.hasUpdatedItems = "no";
        // this hack is necessary as vue does not react to changes to booleans alone, so we increment and decrement the number as a workaround
        feed.unreadItems = feed.unreadItems + 1;
        feed.unreadItems = feed.unreadItems - 1;
    },
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

