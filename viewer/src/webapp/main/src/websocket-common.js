import {Stomp} from "@stomp/stompjs/esm6/compatibility/stomp";
import * as SockJS from "sockjs-client";
import store from "./store";

function createClient() {
    console.log("creating wsclient");
    const socket = new SockJS(`http://${process.env.VUE_APP_API_HOST}:8080/ws/`);
    return Stomp.over(socket);
}

function connectWs(client, cb) {
    console.log("connecting ws client");
    return client.connect({
        "X-Auth-Token": store.state.session.token
    }, cb);
}

export { createClient, connectWs };
