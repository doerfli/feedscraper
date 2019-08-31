<template>
  <div class="fullheight">
    <section class="fullheight feedlist">
      <div class="container is-fluid fullheight nomarginright">
        <div class="columns is-gapless fullheight">
          <div class="column is-one-fifth feedlist gaptop">
            <FeedList />
            <FeedAdd />
          </div>
          <div class="column fullheight gaptop items">
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
            client.subscribe('/topic/feeds', function (greeting) {
              console.log(JSON.parse(greeting.body));
              // reload feeds
              thisStore.dispatch("feeds/getAll");
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
  .feedlist {
    background-color: $primary_light;
  }

  .nomarginright {
    margin-right: 0;
  }

  .columns.is-gapless > .column.gaptop {
    padding-right: $mygap !important;
  }

  .items {
    background-color: #f1f1f1;
  }
</style>
