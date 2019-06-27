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
                            v-bind:class="{input: true, 'is-danger': !validation.usernamePattern, 'is-success': this.validation.usernameValid}"
                            v-on:change="validateUsername"
                            type="email"
                            placeholder="name@example.com"
                        />
                        <span class="icon is-small is-left">
                            <i class="fas fa-envelope"></i>
                        </span>
                    </div>
                    <p v-if="!validation.usernamePattern" class="help is-danger">Please enter a valid email address</p>
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
                            v-model="password"
                            v-on:change="validatePassword"
                            v-bind:class="{input: true, 'is-success': this.validation.passwordValid}"
                            type="password"
                            placeholder="Enter your password"
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
            <div class="field-label has-text-right">
                <label class="label">Confirm</label>
            </div>
            <div class="field-body">
                <div class="field">
                    <div class="control has-icons-left has-icons-right">
                        <input
                            v-model="passwordConfirmation"
                            v-on:change="validatePasswordConfirmation"
                            v-bind:class="{input: true, 'is-success': this.validation.passwordConfirmationValid}"
                            type="password"
                            placeholder="Repeat password to confirm"
                        />
                        <span class="icon is-small is-left">
                            <i class="fas fa-lock"></i>
                        </span>
                    </div>
                    <p v-if="!validation.passwordsMatch" class="help is-danger">Entered passwords do not match</p>
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
                    usernameValid: false,
                    passwordValid: false,
                    passwordConfirmationValid: false,
                    usernamePattern: true,
                    passwordLength: true,
                    passwordsMatch: true
                }
            }
        },
        computed: {
            submitAllowed: function() {
                return this.validation.usernameValid && this.validation.passwordValid && this.validation.passwordConfirmationValid;
            }
        },
        methods: {
            signup: function() {
                this.$store.dispatch("users/signup", {username: this.username, password: this.password});
                this.username = "";
                this.password = "";
                this.passwordConfirmation = "";
                this.validation.usernameValid = false;
                this.validation.passwordValid = false;
                this.validation.passwordConfirmationValid = false;
            },
            validateUsername: function() {
                let emailRex = /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                this.validation.usernamePattern = this.username.match(emailRex) != null;
                this.validation.usernameValid = this.username.length > 0 && this.validation.usernamePattern;
            },
            validatePassword: function() {
                this.validation.passwordLength = this.password.length >= 6;
                this.validation.passwordValid = this.validation.passwordLength;
            },
            validatePasswordConfirmation: function() {
                this.validation.passwordsMatch = this.password === this.passwordConfirmation;
                this.validation.passwordConfirmationValid = this.passwordConfirmation.length > 0 && this.validation.passwordsMatch
            }
        }
    }
</script>

<style scoped>

</style>
