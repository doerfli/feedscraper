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
    }
</script>

<style scoped>
  .content ul {
    margin-top: 0px;
  }
</style>
