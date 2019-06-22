<template>
    <div>
        <div class="field is-horizontal">
            <div class="field-label has-text-right">
                <label class="label">Username</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <div class="control has-icons-left has-icons-right">
                        <input
                            v-model="username"
                            v-bind:class="{input: true,  'is-danger': !validation.username_valid}"
                            v-on:change="validateUsername"
                            type="email"
                            placeholder="name@example.com"
                        />
                        <span class="icon is-small is-left">
                            <i class="fas fa-envelope"></i>
                        </span>
                    </div>
                    <p v-if="!validation.username_valid" class="help is-danger">Please enter a valid email address</p>
                    <p class="help is-info">You will receive an email with a confirmation code to validate your account</p>
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
                passwordConfirmation: "",
                validation: {
                    username_valid: true
                }
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
            },
            validateUsername: function() {
                if (this.username === "") {
                    this.validation.username_valid = false;
                } else {
                    let emailRex = /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                    this.validation.username_valid = this.username.match(emailRex) != null;
                }
            }
        }
    }
</script>

<style scoped>

</style>
