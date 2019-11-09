<template>
    <div>
        <HeroHeader title="Reset your password"/>
        <section>
            <div>
                <div>
                    <div>
                        <ResetPassword />
                    </div>
                    <div>
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
