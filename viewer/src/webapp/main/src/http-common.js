import axios from 'axios';
import store from "./store";
import router from '@/router';

const AXIOS = axios.create({
    baseURL: `http://${process.env.VUE_APP_API_HOST}:8080/api`,
    headers: {
        // 'Access-Control-Allow-Origin': 'http://localhost:8070'
    }
});

AXIOS.interceptors.request.use(config => {
    // console.log(`token: ${store.state.session.token}`);
    if (store.state.session.token != null) {
        config.headers.common['Authorization'] = `Bearer ${store.state.session.token}`;
    }
    return config;
});

AXIOS.interceptors.response.use(function (response) {
    console.log("1111");
    console.log(response);
    return Promise.resolve(response);
}, function (error) {
    // console.log(error);
    if (error.response.status === 403) {
        console.log("token rejected ... logout");
        store.dispatch("messages/add", { text: "The session has expired. Please sign in again.", type: "warning" });
        store.dispatch("session/logout");
        router.push({name: 'login'});
    } else {
        return Promise.reject(error);
    }
});

export default AXIOS

