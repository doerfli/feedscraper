<template>
  <li v-on:click="selectFeed">
    {{data.title}}
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

<style scoped>

</style>
