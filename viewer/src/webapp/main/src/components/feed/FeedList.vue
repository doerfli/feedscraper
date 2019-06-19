<template>
    <div class="content">
        <ul class="fa-ul">
            <Feed
                v-for="feed in feeds" v-bind:key="feed.pkey" v-bind:data="feed"/>
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
            feeds() {
                return this.$store.state.feeds.all
            }
        },
        methods: {
            connect: function () {

            }
        }
    }
</script>

<style scoped lang="scss">
    .content ul {
        list-style-type: none;
    }
</style>
