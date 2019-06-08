import AXIOS from "@/http-common";
import router from "@/router";

// initial state
const state = {

};

// getters
const getters = {

};

// actions
const actions = {
    // eslint-disable-next-line no-unused-vars
    login({ commit }, payload) {
        AXIOS.post(`/users/signin`, {
            username: payload.username,
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("login successful");
                this.dispatch('session/setToken', { token: response.data.token});
                console.log("redirecting to /");
                router.push({name: 'home'});
            }
            // TODO handle login failure
        })
        .catch(e => {
            console.log(e)
        })
    },
    // eslint-disable-next-line no-unused-vars
    signup({commit}, payload) {
        AXIOS.post(`/users/signup`, {
            username: payload.username,
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("signup successful");
                this.dispatch('messages/add', { text: "Signup successful", type: "notification"});
                this.dispatch('session/setToken', { token: response.data.token});
                router.push({name: 'home'});
                // console.log("redirect to home")
            }
            // TODO handle signup failure
        })
        .catch(e => {
            console.log(e)
        })
    }
};

// mutations
const mutations = {
};

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}

