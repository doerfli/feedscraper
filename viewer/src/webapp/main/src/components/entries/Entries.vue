<template>
  <div>
    <div class="content">
      <ul>
        <Entry
            v-for="entry in entries" v-bind:key="entry.pkey" v-bind:data="entry" />
      </ul>
    </div>
  </div>
</template>

<script>
    import Entry from "./Entry";

    export default {
        name: "Entries",
        components: {
            Entry
        },
        computed: {
            entries () {
                let activeFeedPkey = this.$store.state.session.activeFeed;
                return this.$store.state.entries.all[activeFeedPkey];
            }
        }
    }
</script>

<style scoped>

</style>
