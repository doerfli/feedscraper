<template>
    <div class="content">
        <div v-bind:class="['headline', headlineStateClass]" v-on:click="$emit('showContent')">
            <span class="icon fa-li" ><i v-bind:class="[headlineIconPrefixClass, 'fa-envelope']"></i></span>
            <span class="is-size-4">{{item.title}}</span>
            <br/>
            <span class="is-size-7 topactions">
                Published: {{updatedTime}}
                | <a v-if="item.read" v-on:click="$emit('markAsUnread')" target="_blank"><span class="icon" ><i class="fas fa-envelope"></i></span> Mark as unread</a>
            </span>
        </div>
        <div v-html="item.summary">
        </div>
        <div class="actions">
            <a v-if="summaryHasNoLink" v-bind:href="item.link" target="_blank"><span class="icon" ><i class="fas fa-atlas"></i></span> View full article</a>
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
    .unread {
        span {
            font-weight: 600;
            i {
                color: $primary;
            }
        }
    }

    .headline {
        margin-bottom: 8px;
    }

    .topactions,
    .actions {
        i {
            color: $primary;
        }
    }

    .actions {
        margin-top: 8px;
        margin-bottom: 8px;
    }
</style>
