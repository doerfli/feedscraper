<template>
    <div>
        <ul>
            <li>
                <span>
                    <i class="fas fa-plus"></i>
                </span>
                <a v-on:click="toggleAddForm">Add new feed</a>
            </li>
        </ul>


        <div v-if="shown">
            <div>
                <p>
                    <inputtype="text" placeholder="New feed URL" v-model="url">
                </p>
                <p>
                    <button v-on:click="addFeedUrl" type="submit">Add</button>
                </p>
            </div>
        </div>
    </div>
</template>

<script>

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
                this.$store.dispatch("feeds/pushFeedToServer", {url: this.url});
                this.url = "";
                this.toggleAddForm();
            }
        },
    }
</script>

<style scoped lang="scss">

</style>
