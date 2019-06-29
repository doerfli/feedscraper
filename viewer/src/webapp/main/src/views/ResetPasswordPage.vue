<template>
    <div>
        <HeroHeader title="Reset your password"/>
        <section>
            <div class="container">
                <div class="columns">
                    <div class="column is-half">
                        <ResetPassword />
                    </div>
                    <div class="column">
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import HeroHeader from "../components/HeroHeader";
    import ResetPassword from "../components/login/ResetPassword";

    export default {
        name: "ResetPasswordPage",
        components: {
            HeroHeader,
            ResetPassword
        }
    }
</script>

<style scoped>

</style>
