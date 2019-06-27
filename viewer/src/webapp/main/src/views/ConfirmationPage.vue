<template>
    <div>
        <HeroHeader title="Please wait a moment" subtitle="we are confirmating the validity of the token" />
        <Confirmation />
    </div>
</template>

<script>
    import HeroHeader from "../components/HeroHeader";
    import Confirmation from "../components/login/Confirmation";

    export default {
        name: "ConfirmationPage",
        components: {
            HeroHeader,
            Confirmation
        },

    }
</script>

<style scoped>

</style>
