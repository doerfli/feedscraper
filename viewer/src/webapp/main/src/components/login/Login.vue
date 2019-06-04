<template>
    <div>
        <div class="field">
            <p class="control has-icons-left has-icons-right">
                <input class="input" type="email" placeholder="Email" v-model="username"/>
                <span class="icon is-small is-left">
                    <i class="fas fa-envelope"></i>
                </span>
                <span class="icon is-small is-right">
                    <i class="fas fa-check"></i>
                </span>
            </p>
        </div>
        <div class="field">
            <p class="control has-icons-left">
                <input class="input" type="password" placeholder="Password" v-model="password"/>
                <span class="icon is-small is-left">
                    <i class="fas fa-lock"></i>
                </span>
            </p>
        </div>
        <div class="control">
            <button class="button is-primary" v-on:click="login">Login</button>
        </div>
    </div>
</template>

<script>
    import AXIOS from "@/http-common";

    export default {
        name: "Login",
        data() {
            return {
                username: "",
                password: ""
            }
        },
        methods: {
            login: function() {
                // TODO security - do this somewhere else ... maybe store
                AXIOS.post(`/users/signin`, {
                    username: this.username,
                    password: this.password
                }).then(async response => {
                    console.log(response);
                    if (response.status === 200) {
                        console.log("login successful");
                        this.$store.dispatch('session/setToken', { token: response.data.token});
                        this.$router.push({name: 'home'});
                        console.log("redirect to /")
                    }
                    // TODO handle login failure
                })
                .catch(e => {
                    console.log(e)
                })
            }
        }
    };
</script>

<style scoped>

</style>
