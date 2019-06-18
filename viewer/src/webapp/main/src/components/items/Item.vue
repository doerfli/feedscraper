<template>
    <li>
        <div v-if="!isRead" class="unread">
            <span class="icon fa-li" ><i class="fas fa-envelope"></i></span>
            <span v-on:click="showContent">{{item.title}}</span>
        </div>
        <div v-else class="read">
            <span class="icon fa-li" ><i class="far fa-envelope"></i></span>
            <span v-on:click="showContent">{{item.title}}</span>
        </div>
        <div v-if="isContentVisible" class="content" >
            <div v-html="item.summary">
            </div>
            <a v-bind:href="item.link" target="_blank">Open full version</a> | <a v-if="item.read" v-on:click="markAsUnread" target="_blank">Mark as unread</a>
        </div>
    </li>
</template>

<script>
    export default {
        name: "Item",
        props: {
            item: {},
            index: Number
        },
        data: function () {
            return {
                isContentVisible: false,
                isRead: this.item.read
            };
        },
        methods: {
            showContent: function () {
                let wasVisibleBefore = this.isContentVisible;
                this.isContentVisible = !this.isContentVisible;
                if (!wasVisibleBefore && !this.item.read) {
                    this.$store.dispatch("items/markAsRead", { feedPkey: this.item.feedPkey, itemPkey: this.item.pkey, index: this.index});
                    this.isRead = true;
                }
            },
            markAsUnread: function() {
                if (this.item.read) {
                    this.$store.dispatch("items/markAsUnread", { feedPkey: this.item.feedPkey, itemPkey: this.item.pkey, index: this.index});
                    this.isRead = false;
                }
            }
        }
    }
</script>

<style scoped lang="scss">
    li {
        border-bottom: 1px solid $grey-lighter;


        .unread span {
            font-weight: 600;
            i {
                color: $primary;
            }
        }
    }
</style>
