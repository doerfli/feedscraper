import AXIOS from "@/http-common";
import router from "@/router";
import {TIMEOUT, TIMEOUT_LONG} from "../messages-common";

// initial state
const state = {

};

// getters
const getters = {

};

// actions
const actions = {
    // eslint-disable-next-line no-unused-vars
    async login({ commit }, payload) {
        this.dispatch('messages/clear');
        return AXIOS.post(`/users/signin`, {
            username: payload.username,
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("login successful");
                this.dispatch('session/setToken', { token: response.data.token, username: response.data.username});
                console.log("redirecting to /");
                router.push({name: 'feeds'});
                return true;
            } else {
                return false;
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
            return false;
        });
    },
    // eslint-disable-next-line no-unused-vars
    async signup({commit}, payload) {
        this.dispatch('messages/clear');
        return AXIOS.post(`/users/signup`, {
            username: payload.username,
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("signup successful");
                this.dispatch('messages/add', { text: "Sign up successful! You will receive an email with a link to confirm your account.", type: "notification", timeout: TIMEOUT_LONG});
                return true;
            }
            return false;
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
            return false;
        });
    },
    // eslint-disable-next-line no-unused-vars
    resetPassword({commit}, payload) {
        AXIOS.post(`/users/passwordReset/${payload.token}`, {
            password: payload.password
        }).then(async response => {
            console.log(response);
            if (response.status === 200) {
                console.log("password reset successful");
                this.dispatch('messages/add', { text: "Your new password is set. You can use it to login now", type: "notification", timeout: TIMEOUT});
                router.push({name: 'login'});
            }
        }).catch(error => {
            if (error.response) {
                // The request was made and the server responded with a status code
                // that falls out of the range of 2xx
                console.log(error.response);
                if (error.response.status === 400) {
                    console.log("password reset request failed");
                    this.dispatch('messages/add', { text: "Password reset failed - request a new reset link", type: "error", timeout: TIMEOUT});
                } else if (error.response.status === 412) {
                    console.log("password reset request successful");
                    this.dispatch('messages/add', { text: "Password reset link expired - request a new link", type: "error", timeout: TIMEOUT});
                } else {
                    this.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                }

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

