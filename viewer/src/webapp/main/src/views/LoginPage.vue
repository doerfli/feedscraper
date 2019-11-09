<template>
    <div>
        <HeroHeader title="Sign in" subtitle="to view your feeds" />
        <section>
            <div>
                <div>
                    <di>
                        <Login />
                    </di>
                    <div>
                    </div>
                </div>
            </div>
        </section>
        <BetaInfo />
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
