<template>
    <div>
        <HeroHeader title="Login" />
        <section>
            <div class="container">
                <div class="columns">
                    <div class="column is-one-fifth">
                        <Login />
                    </div>
                    <div class="column">
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import Login from "../components/login/Login";
    import HeroHeader from "../components/HeroHeader";

    export default {
        name: "LoginPage",
        components: {
            HeroHeader,
            Login
        }
    }
</script>

<style scoped>

</style>
