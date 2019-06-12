<template>
  <div>
    <HeroHeader title="Feeds" />
    <section>
      <div class="container">
        <div class="columns">
          <div class="column is-one-fifth">
            <FeedAdd />
            <FeedList />
          </div>
          <div class="column">
            <Items />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
  import HeroHeader from "../HeroHeader";
  import FeedList from "./FeedList";
  import Items from "../items/Items";
  import FeedAdd from "./FeedAdd";

  export default {
        name: "Feeds",
        components: {
            HeroHeader,
            FeedList,
            Items,
            FeedAdd
        },
        created() {
            this.$store.dispatch('feeds/getAll');
        }
    }
</script>

<style scoped>

</style>
