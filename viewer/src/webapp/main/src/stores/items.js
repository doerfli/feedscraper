import AXIOS from "@/http-common";
import _ from "lodash";

let itemBatchSize = 30;

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
        return AXIOS.get(buildGetItemPath(feedPkey)).then(async response => {
            // console.log(response);
            // store the items
            let items = response.data;
            let numItems = items.length;
            console.log(`got ${numItems} items`);
            commit('appendItems', {feedPkey: feedPkey, items: items});
            return numItems;
        }).then(async numItems => {
            // check if more items need to be downloaded
            if(numItems === itemBatchSize && state.all[feedPkey].length < 50) {
                console.log("get more results");
                await this.dispatch('items/getByFeed', { feedPkey: feedPkey});
            }
        }).catch(e => {
            console.log(e)
        })
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
        if (payload.feedPkey in state.all) {
            console.log(state.all[payload.feedPkey].concat(payload.items));
            state.all[payload.feedPkey] = state.all[payload.feedPkey].concat(payload.items)
        } else {
            state.all[payload.feedPkey] = payload.items
        }

    },
    markAsRead(state, payload) {
        state.all[payload.feedPkey][payload.index].read = payload.read;
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
    if(feedPkey in state.all) {
        let lastElement = _.last(state.all[feedPkey]);
        // console.log(lastElement);
        let lastPkey = lastElement.pkey;
        path = `${path}&from=${lastPkey}`;
        // console.log(path);
    }
    return path;
};

