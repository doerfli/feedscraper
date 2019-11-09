<template>
    <div class="content">
        <div v-on:click="$emit('showContent')">
            <span class="icon fa-li" ><i v-bind:class="[headlineIconPrefixClass, 'fa-envelope']"></i></span>
            <span >{{item.title}}</span>
            <br/>
            <span >
                Published: {{updatedTime}}
                | <a v-if="item.read" v-on:click="$emit('markAsUnread')" target="_blank"><span><i></i></span> Mark as unread</a>
            </span>
        </div>
        <div v-html="item.summary">
        </div>
        <div>
            <a v-if="summaryHasNoLink" v-bind:href="item.link" target="_blank"><span><i></i></span> View full article</a>
        </div>
    </div>
</template>

<script>
    import {formatDateTimeLong} from "../../time-common";

    export default {
        name: "ItemOpened",
        props: {
            item: {}
        },
        computed: {
            headlineStateClass: function() {
                if (this.item.read) {
                    return "read "
                } else {
                    return "unread "
                }
            },
            headlineIconPrefixClass: function() {
                if (this.item.read) {
                    return "far"
                } else {
                    return "fas"
                }
            },
            updatedTime: function() {
                return formatDateTimeLong(this.item.updated);
            },
            summaryHasNoLink: function() {
                return !this.item.summary.includes(this.item.link)
            }
        }
    }
</script>

<style scoped lang="scss">

</style>
