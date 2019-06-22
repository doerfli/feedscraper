import axios from 'axios';
import store from "./store";
import router from '@/router';
import {TIMEOUT} from "./messages-common";

const AXIOS = axios.create({
    baseURL: `http://${process.env.VUE_APP_API_HOST}:8080/api`,
    headers: {
        // 'Access-Control-Allow-Origin': 'http://localhost:8070'
    }
});

AXIOS.interceptors.request.use(config => {
    if (store.state.session.token != null) {
        config.headers.common['Authorization'] = `Bearer ${store.state.session.token}`;
    }
    return config;
});

AXIOS.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (error.response.status === 403) {
        console.log("token rejected ... logout");
        store.dispatch("messages/add", { text: "Your session has expired. Please sign in again.", type: "warning", timeout: TIMEOUT });
        store.dispatch("session/logout");
        router.push({name: 'login'});
    } else {
        return Promise.reject(error);
    }
});

export default AXIOS

