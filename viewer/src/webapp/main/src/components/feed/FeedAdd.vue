<template>
    <div class="content">
        <ul class="fa-ul">
            <li>
                <span class="icon fa-li">
                    <i class="fas fa-plus"></i>
                </span>
                <a v-on:click="toggleAddForm">Add new feed</a>
            </li>
        </ul>


        <div v-if="shown">
            <div class="field has-addons">
                <p class="control is-expanded">
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
    }
</script>

<style scoped lang="scss">
    .icon {
        color: $primary;
    }
    li {
        list-style-type: none;
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
