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
            <Entries />
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
    import HeroHeader from "../HeroHeader";
    import FeedList from "./FeedList";
    import Entries from "../entries/Entries";
    export default {
        name: "Feeds",
        components: {
            HeroHeader,
            FeedList,
            Entries
        },
        created() {
            this.$store.dispatch('feeds/getAll');
        }
    }
</script>

<style scoped>

</style>
