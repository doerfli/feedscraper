<template>
    <div>
        <HeroHeader title="Signup" />
        <section>
            <div class="container">
                <div class="columns">
                    <div class="column is-one-fifth">
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

    export default {
        name: "SignupPage",
        components: {
            Signup
        }
    }
</script>

<style scoped>

</style>
