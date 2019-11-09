<template>
    <div>
        <div v-bind:class="[readState]">
            <span ><i v-bind:class="[iconPrefixClass,'fa-envelope']"></i></span>
            <span v-on:click="$emit('showContent')">{{item.title}}</span>&nbsp;
            <span>{{updatedTime()}}</span>
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

</style>
