<template>
  <li v-on:click="selectFeed">
    <span class="icon fa-li" ><i class="fas fa-rss"></i></span>
    <a v-if="hasUnreadItems()">{{data.title}} ({{this.data.unreadItems}})</a>
    <a v-else>{{data.title}}</a>
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
          },
          hasUnreadItems: function() {
            return this.data.unreadItems > 0;
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
