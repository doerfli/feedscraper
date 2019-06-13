<template>
  <div class="content">
    <ul>
      <Feed
          v-for="feed in feeds" v-bind:key="feed.pkey" v-bind:data="feed" />
    </ul>
  </div>
</template>

<script>
  import Feed from "./Feed";
  import {Stomp} from "@stomp/stompjs/esm6/compatibility/stomp";
  import * as SockJS from "sockjs-client";

  var stompClient;

    export default {
        name: "FeedList",
        components: {
            Feed
        },
        computed: {
            feeds () {
                return this.$store.state.feeds.all
            }
        },
        methods: {
          connect: function() {
            var socket = new SockJS(`http://${process.env.VUE_APP_API_HOST}:8080/ws/`);
            stompClient = Stomp.over(socket);
            stompClient.connect({
              "X-Auth-Token": this.$store.state.session.token
            }, function (frame) {
              // setConnected(true);
              console.log("000");
              console.log('Connected: ' + frame);
              stompClient.subscribe('/topic/feeds', function (greeting) {
                console.log("111");
                console.log(JSON.parse(greeting.body));
                greeting.ack();
              });
              setTimeout(function () {
                stompClient.send("/app/hello", {}, JSON.stringify({'name': "bla11"}));
              }, 1000);
            });
          }
        },
      mounted() {
          this.connect();
      }
    }
</script>

<style scoped>
  .content ul {
    margin-top: 0px;
  }
</style>
