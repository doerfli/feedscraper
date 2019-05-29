<template>
  <div>
    <HeroHeader title="Feeds" />
    <section>
      <div class="container">
        <div class="columns">
          <div class="column is-one-fifth">
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
    export default {
        name: "Feeds",
        components: {
            HeroHeader,
            FeedList,
            Items
        },
        created() {
            this.$store.dispatch('feeds/getAll');
        }
    }
</script>

<style scoped>

</style>
