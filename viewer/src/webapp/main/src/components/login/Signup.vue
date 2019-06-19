<template>
    <div>
        <div class="field is-horizontal">
            <div class="field-label has-text-right">
                <label class="label">Username</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <p class="control has-icons-left has-icons-right">
                        <input class="input" type="email" placeholder="Email" v-model="username"/>
                        <span class="icon is-small is-left">
                            <i class="fas fa-envelope"></i>
                        </span>
                    </p>
                </div>
            </div>
        </div>
        <div class="field is-horizontal">
            <div class="field-label has-text-right">
                <label class="label">Password</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <p class="control has-icons-left has-icons-right">
                        <input class="input" type="password" placeholder="Password" v-model="password"/>
                        <span class="icon is-small is-left">
                            <i class="fas fa-lock"></i>
                        </span>
                    </p>
                </div>
            </div>
        </div>
        <div class="field is-horizontal">
            <div class="field-label has-text-right">
                <label class="label">Confirm</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <p class="control has-icons-left has-icons-right">
                        <input class="input" type="password" placeholder="Repeat password" v-model="passwordConfirmation"/>
                        <span class="icon is-small is-left">
                            <i class="fas fa-lock"></i>
                        </span>
                    </p>
                </div>
            </div>
        </div>
        <div class="field is-horizontal">
            <div class="field-label">
            </div>
            <div class="field-body">
                <div class="field">
                    <div v-if="submitAllowed" class="control">
                        <button class="button is-primary" v-on:click="signup">Sign me up</button>
                    </div>
                    <fieldset v-else disabled>
                        <div class="control">
                            <button class="button is-primary">Sign me up</button>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

    export default {
        name: "Signup",
        data() {
            return {
                username: "",
                password: "",
                passwordConfirmation: ""
            }
        },
        computed: {
            submitAllowed: function() {
                return this.password.length >= 6 && ( this.password === this.passwordConfirmation );
            }
        },
        methods: {
            signup: function() {
                this.$store.dispatch("users/signup", {username: this.username, password: this.password})
            }
        }
    }
</script>

<style scoped>

</style>
