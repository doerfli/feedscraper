import axios from 'axios';
import store from "./store";

// console.log(process.env);

const AXIOS = axios.create({
    baseURL: `http://${process.env.VUE_APP_API_HOST}:8080/api`,
    headers: {
        // 'Access-Control-Allow-Origin': 'http://localhost:8070'
    }
});

AXIOS.interceptors.request.use(config => {
    console.log(`token: ${store.state.session.token}`);
    if (store.state.session.token != null) {
        config.headers.common['Authorization'] = `Bearer ${store.state.session.token}`;
    }
    return config;
});

// TODO implement global error handler and redirect to /login on 403

export default AXIOS

