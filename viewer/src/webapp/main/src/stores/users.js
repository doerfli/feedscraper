import AXIOS from "@/http-common";
import router from "@/router";
import {TIMEOUT_LONG} from "../messages-common";

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
        this.dispatch('messages/clear');
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
        }).catch(error => {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.log(error.response);
                if (error.response.status === 422) {
                    this.dispatch('messages/add', { text: "Invalid username or password", type: "error"});
                } else {
                    this.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                }
            } else if (error.request) {
                this.dispatch('messages/add', { text: "Server could not be contacted. Please try again later", type: "error"});
            } else {
                this.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
            }
        })
    },
    // eslint-disable-next-line no-unused-vars
    signup({commit}, payload) {
        this.dispatch('messages/clear');
        AXIOS.post(`/users/signup`, {
            username: payload.username,
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("signup successful");
                this.dispatch('messages/add', { text: "Sign up successful! You will receive an email with a link to confirm your account.", type: "notification", timeout: TIMEOUT_LONG});
            }
        }).catch(error => {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.log(error.response);
                this.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
            } else if (error.request) {
                this.dispatch('messages/add', { text: "Server could not be contacted. Please try again later", type: "error"});
            } else {
                this.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
            }
        });
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

