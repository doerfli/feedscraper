<template>
    <div>
        <div v-bind:class="[readState]">
            <span class="icon fa-li" ><i v-bind:class="[iconPrefixClass,'fa-envelope']"></i></span>
            <span v-on:click="$emit('showContent')">{{item.title}}</span>&nbsp;
            <span class="is-size-7 time">{{updatedTime()}}</span>
        </div>
    </div>
</template>

<script>
    import {formatDateTimeFromNow} from "../../time-common";

    export default {
        name: "ItemClosed",
        props: {
            item: {}
        },
        methods: {
            updatedTime: function() {
                return formatDateTimeFromNow(this.item.updated);
            }
        },
        computed: {
            readState: function() {
                if (this.item.read) {
                    return "read";
                }
                return "unread";
            },
            iconPrefixClass: function() {
                if (this.item.read) {
                    return "far"
                } else {
                    return "fas"
                }
            },

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
        span.time {
            font-weight: 400;
        }
    }
</style>
