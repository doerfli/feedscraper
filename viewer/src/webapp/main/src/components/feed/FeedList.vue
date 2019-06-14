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
    import {connectWs, wsClient} from "@/websocket-common";

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

          }
        },
      mounted() {
        connectWs(function (frame) {
          console.log('Connected: ' + frame);
          wsClient.subscribe('/topic/feeds', function (greeting) {
            console.log(JSON.parse(greeting.body));
          });
          setTimeout(function () {
            wsClient.send("/app/hello", {}, JSON.stringify({'name': "bla11"}));
          }, 1000);
        });
      }
    }
</script>

<style scoped>
  .content ul {
    margin-top: 0px;
  }
</style>
