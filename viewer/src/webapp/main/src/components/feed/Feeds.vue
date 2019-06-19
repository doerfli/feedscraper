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

  export default {
        name: "Feeds",
        components: {
            FeedList,
            Items,
            FeedAdd
        },
        created() {
            this.$store.dispatch('feeds/getAll');
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
