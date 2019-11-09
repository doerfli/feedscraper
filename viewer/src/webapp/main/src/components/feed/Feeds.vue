<template>
  <div>
    <section>
      <div>
        <div>
          <div>
            <FeedList />
            <FeedAdd />
          </div>
          <div>
            <Items />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import FeedList from "./FeedList";
  import Items from "../items/Items";
  import FeedAdd from "./FeedAdd";
  import _ from "lodash";
  import {connectWs, createClient} from "../../websocket-common";

  export default {
        name: "Feeds",
        components: {
            FeedList,
            Items,
            FeedAdd
        },
        data: function () {
          return {
            wsClients: []
          }
        },
        created() {
            this.$store.dispatch('feeds/getAll');
        },
        mounted() {
          let thisStore = this.$store;
          let client = createClient();
          this.wsClients.push(client);
          connectWs(this.wsClients[0], function (frame) {
            console.log('Connected: ' + frame);
            client.subscribe('/topic/feeds', function (indata) {
              let data = JSON.parse(indata.body);
              console.log(data);
              // reload feeds
              thisStore.dispatch("feeds/addNew", { pkey: _.toInteger(data['feedPkey'])});
            });
            client.subscribe('/topic/updated_items', function (indata) {
              let data = JSON.parse(indata.body);
              console.log(data);
              // reload feeds
              thisStore.dispatch("feeds/updatedItems", { pkey: _.toInteger(data['feedPkey'])});
            });
          });
        },
        beforeDestroy() {
          if (this.wsClients.length > 0) {
            for(let i in this.wsClients) {
              this.wsClients[i].disconnect();
              console.log("websocket client disconnected");
            }
          }
        }
  }
</script>

<style scoped lang="scss">

</style>
