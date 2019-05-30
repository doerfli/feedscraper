<template>
    <li>
        <span v-on:click="showContent">{{data.title}}</span>
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
        },
        data: function () {
            return {
                isContentVisible: false
            };
        },
        methods: {
            showContent: function () {
                this.isContentVisible = !this.isContentVisible
            }
        }
    }
</script>

<style scoped>

</style>
