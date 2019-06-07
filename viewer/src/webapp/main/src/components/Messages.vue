<template>
    <div class="container">
        <article class="message is-info" v-for="message in messages" v-bind:key="message.id" >
            <div class="message-body">
                {{message.text}}
            </div>
        </article>
    </div>
</template>

<script>
    export default {
        name: "Messages",
        data: function() {
            return {
                msgId: 0
            }
        },
        computed: {
            messages: function () {
                return this.$store.state.messages.messages;
            }
        }
    }
</script>

<style scoped>

</style>
