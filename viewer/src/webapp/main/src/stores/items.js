import AXIOS from "@/http-common";
import _ from "lodash";

let itemBatchSize = 30;

// initial state
const state = {
    all: [],
    endReached: false
};

// getters
const getters = {};

// actions
const actions = {
    async initialize({ commit }, payload) {
        commit('clear', null);
        let feedPkey = payload.feedPkey;
        console.log(`retrieving items for pkey ${feedPkey}`);

        // downloas two batched of items
        await this.dispatch('items/loadMoreItems', { feedPkey: feedPkey}).then(() => {
            return this.dispatch('items/loadMoreItems', {feedPkey: feedPkey});
        });
    },
    async loadMoreItems({commit}, payload) {
        if (state.endReached === true) {
            return;
        }
        let feedPkey = payload.feedPkey;
        console.log(`retrieving items for pkey ${feedPkey}`);
        return AXIOS.get(buildGetItemPath(feedPkey)).then(response => {
            // console.log(response);
            // store the items
            let items = response.data;
            let numItems = items.length;
            console.log(`got ${numItems} items`);
            commit('appendItems', {feedPkey: feedPkey, items: items});
            return items.length;
        }).catch(e => {
            console.log(e)
        });
    },
    markAsRead({commit}, payload) {
        let itemPkey = payload.itemPkey;
        console.log(`marking item ${itemPkey} as read`);
        AXIOS.post(`/items/${itemPkey}/read`);
        commit('markAsRead', {pkey: itemPkey, read: true, feedPkey: payload.feedPkey, index: payload.index});
    },
    markAsUnread({commit}, payload) {
        let itemPkey = payload.itemPkey;
        console.log(`marking item ${itemPkey} as unread`);
        AXIOS.post(`/items/${itemPkey}/unread`);
        commit('markAsRead', {pkey: itemPkey, read: false, feedPkey: payload.feedPkey, index: payload.index});
    }
};

// mutations
const mutations = {
    appendItems(state, payload) {
        // console.log(payload.items);
        // console.log(state.all[payload.feedPkey].concat(payload.items));
        // state.all[payload.feedPkey] = state.all[payload.feedPkey].concat(payload.items)
        let lengthBefore = state.all.length;
        for (let t in payload.items) {
            let item = payload.items[t];
            if (_.findIndex(state.all, function(e) { return e.pkey === item.pkey; }) === -1) {
                state.all.push(item)
            }
        }
        if (state.all.length === lengthBefore) {
            state.endReached = true;
        }
    },
    markAsRead(state, payload) {
        state.all.read = payload.read;
    },
    // eslint-disable-next-line no-unused-vars
    clear(state) {
        state.all = [];
        state.endReached = false;
    }
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

// helper

let buildGetItemPath = (feedPkey) => {
    let path = `/items/byFeed/${feedPkey}?size=${itemBatchSize}`;
    if(state.all.length > 0) {
        let lastElement = _.last(state.all);
        // console.log(lastElement);
        let lastPkey = lastElement.pkey;
        path = `${path}&from=${lastPkey}`;
        // console.log(path);
    }
    return path;
};

