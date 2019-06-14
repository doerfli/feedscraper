import {Stomp} from "@stomp/stompjs/esm6/compatibility/stomp";
import * as SockJS from "sockjs-client";
import store from "./store";

const socket = new SockJS(`http://${process.env.VUE_APP_API_HOST}:8080/ws/`);
const wsClient = Stomp.over(socket);

function connectWs(cb) {
    return wsClient.connect({
        "X-Auth-Token": store.state.session.token
    }, cb);
}

export { connectWs, wsClient };
