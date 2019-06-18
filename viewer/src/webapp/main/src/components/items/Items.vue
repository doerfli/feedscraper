<template>
  <div class="items">
    <div class="content">
      <ul class="fa-ul">
        <Item
            v-for="(item,index) in items" v-bind:key="item.pkey" v-bind:item="item" v-bind:index="index" />
      </ul>
    </div>
  </div>
</template>

<script>
  import Item from "./Item";

  export default {
        name: "Items",
        components: {
            Item
        },
        computed: {
            items() {
                let activeFeedPkey = this.$store.state.session.activeFeed;
                return this.$store.state.items.all[activeFeedPkey];
            }
        }
    }
</script>

<style scoped lang="scss">
  .items {
    margin-left: $mygap;
  }
  .content ul {
    list-style-type: none;
  }
</style>
