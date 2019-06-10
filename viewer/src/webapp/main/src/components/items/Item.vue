<template>
    <li>
        <div v-if="!this.data.read" class="unread">
            <span v-on:click="showContent">{{data.title}}</span>
        </div>
        <div v-else class="read">
            <span v-on:click="showContent">{{data.title}}</span>
        </div>
        <div v-if="isContentVisible" class="content" >
            <div v-html="data.summary">
            </div>
            <a v-bind:href="data.link" target="_blank">Open full version</a>
        </div>
    </li>
</template>

<script>
    export default {
        name: "Item",
        props: {
            data: {},
            index: Number
        },
        data: function () {
            return {
                isContentVisible: false
            };
        },
        methods: {
            showContent: function () {
                this.isContentVisible = !this.isContentVisible;
                if (!this.data.read) {
                    this.$store.dispatch("items/markAsRead", { feedPkey: this.data.feedPkey, itemPkey: this.data.pkey, index: this.index});
                }
            }
        }
    }
</script>

<style scoped>
    .unread span {
        font-weight: bold;
    }
</style>
