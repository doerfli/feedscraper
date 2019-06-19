<template>
    <div>
        <HeroHeader title="Signup" subtitle="to view content from many sources in one place"/>
        <section>
            <div class="container">
                <div class="columns">
                    <div class="column is-half">
                        <Signup />
                    </div>
                    <div class="column">
                    </div>
                </div>
            </div>
        </section>
    </div>
</template>

<script>
    import Signup from "../components/login/Signup";
    import HeroHeader from "../components/HeroHeader";

    export default {
        name: "SignupPage",
        components: {
            HeroHeader,
            Signup
        }
    }
</script>

<style scoped>

</style>
