<template>
    <li>
        <ItemClosed
            v-if="!isContentVisible"
            v-bind:item="item"
            v-on:showContent="showContent"
        />
        <ItemOpened
            v-else
            v-bind:item="item"
            v-on:showContent="showContent"
            v-on:markAsUnread="markAsUnread"
        />
    </li>
</template>

<script>
    import ItemOpened from "./ItemOpened";
    import ItemClosed from "./ItemClosed";

    export default {
        name: "Item",
        props: {
            item: {},
            index: Number
        },
        components: {
            ItemOpened,
            ItemClosed
        },
        data: function () {
            return {
                isContentVisible: false
            };
        },
        methods: {
            showContent: function () {
                let wasVisibleBefore = this.isContentVisible;
                this.isContentVisible = !this.isContentVisible;
                if (!wasVisibleBefore && !this.item.read) {
                    this.$store.dispatch("items/markAsRead", { feedPkey: this.item.feedPkey, itemPkey: this.item.pkey, index: this.index});
                }
            },
            markAsUnread: function() {
                if (this.item.read) {
                    this.$store.dispatch("items/markAsUnread", { feedPkey: this.item.feedPkey, itemPkey: this.item.pkey, index: this.index});
                    this.isContentVisible = !this.isContentVisible;
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    li {
        border-bottom: 1px solid $grey-lighter;
    }
</style>
