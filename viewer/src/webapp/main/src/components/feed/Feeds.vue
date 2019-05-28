<template>
  <div>
    <HeroHeader title="Feeds" />
    <section>
      <div class="container">
        <div class="columns">
          <div class="column is-one-fifth">
            <FeedList />
          </div>
          <div class="column">Data here ...</div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
    import HeroHeader from "../HeroHeader";
    import FeedList from "./FeedList";
    export default {
        name: "Feeds",
        components: {
            HeroHeader,
            FeedList
        },
        created() {
            this.$store.dispatch('feeds/getAll');
        }
    }
</script>

<style scoped>

</style>
