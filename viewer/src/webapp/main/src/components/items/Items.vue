<template>
  <div class="items">
    <div class="content">
      <ul class="fa-ul">
        <Item
            v-for="(item,index) in items" v-bind:key="item.pkey" v-bind:item="item" v-bind:index="index"/>
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
      items: function () {
        return this.$store.state.items.all;
      }
    },
    methods: {
      bottomDetection() {
        window.onscroll = () => {
          // console.log(document.documentElement.scrollTop);
          // console.log(window.innerHeight);
          // console.log(document.getElementsByClassName("items")[0].offsetHeight);
          let bottomOfWindow = document.documentElement.scrollTop + window.innerHeight >= document.getElementsByClassName("items")[0].offsetHeight;
          if (bottomOfWindow) {
            this.loadMoreItems();
          }
        };
      },
      loadMoreItems() {
        this.$store.dispatch('items/loadMoreItems', {feedPkey: this.$store.state.session.activeFeed});
      }
    },
    mounted() {
      this.bottomDetection();
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
