<template>
    <div>
        <Navigation />
        <Messages />
    </div>
</template>

<script>
    import Navigation from "./Navigation";
    import Messages from "./Messages";

    export default {
        name: "Header",
        components: {
            Messages,
            Navigation
        }
    }
</script>

<style scoped>

</style>
