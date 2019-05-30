<template>
    <li>
        <span v-on:click="showContent">{{data.title}}</span>
        <div v-if="isContentVisible" class="content" v-html="data.summary">
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
