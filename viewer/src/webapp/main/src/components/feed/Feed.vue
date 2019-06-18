<template>
  <li v-on:click="selectFeed">
    <span class="icon fa-li" ><i class="fas fa-rss"></i></span>
    <a>{{data.title}}</a>
  </li>
</template>

<script>
    export default {
        name: "Feed",
        props: {
            data: {}
        },
        methods: {
            selectFeed: function() {
                console.log(this);
                this.$store.dispatch('session/setActiveFeed', { pkey: this.data.pkey});
            }
        }
    }
</script>

<style scoped lang="scss">
  li {
    a,
    a:visited,
    a:link {
      color: $text;
    }
    a:hover {
      color: $primary;
    }
  }
</style>
