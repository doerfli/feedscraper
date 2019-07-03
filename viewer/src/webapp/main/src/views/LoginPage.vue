<template>
    <div>
        <HeroHeader title="Sign in" subtitle="to view your feeds" />
        <section>
            <div class="container">
                <div class="columns">
                    <div class="column is-half">
                        <Login />
                    </div>
                    <div class="column">
                    </div>
                </div>
            </div>
        </section>
        <section>
            <div class="container">
                <BetaInfo />
            </div>
        </section>
    </div>
</template>

<script>
    import Login from "../components/login/Login";
    import HeroHeader from "../components/HeroHeader";
    import BetaInfo from "../components/login/BetaInfo";

    export default {
        name: "LoginPage",
        components: {
            HeroHeader,
            Login,
            BetaInfo
        }
    }
</script>

<style scoped>

</style>
