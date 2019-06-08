<template>
    <div class="container">
        <article :class="classType(message.type)" v-for="message in messages" v-bind:key="message.id" >
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
        },
        methods: {
            classType(msgType) {
                let cls = "message is-";
                if(msgType === 'notification') {
                    cls += 'info';
                } else if (msgType === 'warning') {
                    cls +='warning';
                } else if (msgType === 'error') {
                    cls += 'danger';
                } else {
                    cls += 'info';
                }
                return cls;
            }
        }
    }
</script>

<style scoped>

</style>
