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
                            v-on:change="validateUsername"
                            v-on:keyup="validateUsernameAfterTimeout"
                            v-bind:class="{input: true, 'is-success': this.validation.usernameValid}"
                            type="email"
                            placeholder="Email"
                        />
                        <span class="icon is-small is-left">
                            <i class="fas fa-envelope"></i>
                        </span>
                    </div>
                    <p v-if="!validation.usernamePattern" class="help is-danger">Please enter an email address</p>
                </div>
            </div>
        </div>
        <div class="field is-horizontal">
            <div class="field-label has-text-right">
                <label class="label">Password</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <div class="control has-icons-left has-icons-right">
                        <input
                            v-on:change="validatePassword"
                            v-on:keyup="validatePasswordAfterTimeout"
                            v-bind:class="{input: true, 'is-success': this.validation.passwordValid}"
                            type="password"
                            placeholder="Password"
                            v-model="password"
                        />
                        <span class="icon is-small is-left">
                            <i class="fas fa-lock"></i>
                        </span>
                    </div>
                    <p v-if="!validation.passwordLength" class="help is-danger">Password must have at least 6 characters</p>
                </div>
            </div>
        </div>
        <div class="field is-horizontal">
            <div class="field-label">
            </div>
            <div class="field-body">
                <div class="field">
                    <div v-if="submitAllowed" class="control">
                        <button class="button is-primary" v-on:click="login">Sign in</button>
                        <ForgotPassword />
                    </div>
                    <fieldset v-else disabled>
                        <div class="control">
                            <button class="button is-primary">Sign in</button>
                            <ForgotPassword v-bind:username="username" v-bind:username-valid="this.validation.usernameValid"/>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import ForgotPassword from "./ForgotPassword";

    export default {
        name: "Login",
        components: {ForgotPassword},
        data() {
            return {
                username: "",
                password: "",
                validation: {
                    usernameValid: false,
                    passwordValid: false,
                    usernamePattern: true,
                    passwordLength: true
                },
                timeouts: {
                    username: null,
                    password: null
                }
            }
        },
        computed: {
            submitAllowed: function () {
                return this.validation.usernameValid && this.validation.passwordValid;
            }
        },
        methods: {
            login: function() {
                this.$store.dispatch("users/login", {username: this.username, password: this.password})
            },
            validateUsernameAfterTimeout: function() {
                this.runAfterTimeout("timeouts.username", () => this.validateUsername());
            },
            validateUsername: function() {
                let emailRex = /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                this.validation.usernamePattern = this.username.match(emailRex) != null;
                this.validation.usernameValid = this.username.length > 0 && this.validation.usernamePattern;
            },
            validatePasswordAfterTimeout: function() {
                this.runAfterTimeout("timeouts.password", () => this.validatePassword());
            },
            validatePassword: function() {
                this.validation.passwordLength = this.password.length >= 6;
                this.validation.passwordValid = this.validation.passwordLength;
            },
            runAfterTimeout: function(timeoutVar, fct) {
                if (this[timeoutVar] != null ) {
                    clearTimeout(this[timeoutVar]);
                }
                this[timeoutVar] = setTimeout(() => {
                    fct()
                }, 500);
            }
        }
    };
</script>

<style scoped lang="scss">

</style>
