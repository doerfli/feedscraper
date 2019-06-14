<template>
    <div>
        <span class="icon">
            <i class="fas fa-plus"></i>
        </span>
        <a v-on:click="toggleAddForm">Add new feed</a>
        <div v-if="shown">
            <div class="field has-addons">
                <p class="control">
                    <input class="input" type="text" placeholder="New feed URL" v-model="url">
                </p>
                <p class="control">
                    <button v-on:click="addFeedUrl" type="submit" class="button is-primary">Add</button>
                </p>
            </div>
        </div>
    </div>
</template>

<script>
    import {connectWs, wsClient} from "@/websocket-common";

    export default {
        name: "FeedAdd",
        data: function () {
            return {
                shown: false,
                url: ""
            }
        },
        methods: {
            toggleAddForm: function () {
                this.shown = !this.shown;
            },
            addFeedUrl: function () {
                console.log(this.url);
                this.$store.dispatch("feeds/add", {url: this.url});
                this.url = "";
                this.toggleAddForm();
            }
        },
        mounted() {
            let thisStore = this.$store;
            connectWs(function (frame) {
                console.log('Connected: ' + frame);
                wsClient.subscribe('/topic/feeds', function (greeting) {
                    console.log(JSON.parse(greeting.body));
                    // reload feeds
                    thisStore.dispatch("feeds/getAll");
                });
            });
        }
    }
</script>

<style scoped>

</style>
