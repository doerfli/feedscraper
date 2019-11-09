<template>
  <li v-on:click="selectFeed">
    <span class="icon fa-li" ><i class="fas fa-rss"></i></span>
    <a>{{data.title}}</a>
    <span v-if="hasUnreadItems()">
      <a>({{this.data.unreadItems}})</a>
    </span>
    <span v-if="hasUpdatedItems()">
      <span >
          <i class="far fa-dot-circle fa-xs"></i>
      </span>
    </span>
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
              this.$store.dispatch('feeds/updatedItemsFalse', { pkey: this.data.pkey});
              this.$store.dispatch('session/setActiveFeed', { pkey: this.data.pkey});
          },
          hasUnreadItems: function() {
            return this.data.unreadItems > 0;
          },
          hasUpdatedItems: function() {
            let h = this.data.hasUpdatedItems;
            return h === "yes";
          }
        },
    }
</script>

<style scoped lang="scss">

</style>
