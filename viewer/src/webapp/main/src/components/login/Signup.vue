<template>
    <div>
        <div >
            <div >
                <label >Username</label>
            </div>
            <div >
                <div >
                    <div >
                        <input
                            v-model="username"
                            v-on:change="validateUsername"
                            v-on:keyup.13="signup"
                            v-on:keyup="validateUsernameAfterTimeout"
                            type="email"
                            placeholder="name@example.com"
                        />
                        <span >
                            <i ></i>
                        </span>
                    </div>
                    <p v-if="!validation.usernamePattern" >Please enter a valid email address</p>
                </div>
            </div>
        </div>
        <div >
            <div >
                <label >Password</label>
            </div>
            <div >
                <div >
                    <div >
                        <input
                            v-model="password"
                            v-on:change="validatePassword"
                            v-on:keyup.13="signup"
                            v-on:keyup="validatePasswordAfterTimeout"
                            type="password"
                            placeholder="Enter your password"
                        />
                        <span >
                            <i ></i>
                        </span>
                    </div>
                    <p v-if="!validation.passwordLength" >Password must have at least 6 characters</p>
                </div>
            </div>
        </div>
        <div >
            <div >
                <label >Confirm</label>
            </div>
            <div >
                <div >
                    <div >
                        <input
                            v-model="passwordConfirmation"
                            v-on:change="validatePasswordConfirmation"
                            v-on:keyup.13="signup"
                            v-on:keyup="validatePasswordConfirmationAfterTimeout"
                            type="password"
                            placeholder="Repeat password to confirm"
                        />
                        <span >
                            <i ></i>
                        </span>
                    </div>
                    <p v-if="!validation.passwordsMatch" >Entered passwords do not match</p>
                </div>
            </div>
        </div>
        <div >
            <div >
            </div>
            <div >
                <span v-if="loggingIn">
                    <span class="spinner">
                        <span class="icon has-text-primary">
                            <i class="fas fa-circle-notch fa-2x fa-spin"></i>
                        </span>&nbsp;
                    </span>
                </span>
                <span v-else>
                    <div >
                        <div v-if="submitAllowed" >
                            <button v-on:click="signup">Sign me up</button>
                        </div>
                        <fieldset v-else disabled>
                            <div >
                                <button >Sign me up</button>
                            </div>
                        </fieldset>
                    </div>
                </span>
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
                },
                timeouts: {
                    username: null,
                    password: null,
                    passwordConfirmation: null
                },
                loggingIn: false
            }
        },
        computed: {
            submitAllowed: function() {
                return this.validation.usernameValid && this.validation.passwordValid && this.validation.passwordConfirmationValid;
            }
        },
        methods: {
            signup: function() {
                this.validateUsername();
                this.validatePassword();
                this.validatePasswordConfirmation();
                if (!this.submitAllowed) {
                    return;
                }
                this.loggingIn = true;
                this.$store.dispatch("users/signup", {username: this.username, password: this.password}).then(() => {
                    this.loggingIn = false;
                    this.username = "";
                    this.password = "";
                    this.passwordConfirmation = "";
                    this.validation.usernameValid = false;
                    this.validation.passwordValid = false;
                    this.validation.passwordConfirmationValid = false;
                });

            },
            validateUsernameAfterTimeout: function() {
                this.runAfterTimeout("timeouts.username", () => this.validateUsername())
            },
            validateUsername: function() {
                let emailRex = /^(([^<>()\\[\]\\.,;:\s@"]+(\.[^<>()\\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                this.validation.usernamePattern = this.username.match(emailRex) != null;
                this.validation.usernameValid = this.username.length > 0 && this.validation.usernamePattern;
            },
            validatePasswordAfterTimeout: function() {
                this.runAfterTimeout("timeouts.password", () => this.validatePassword())
            },
            validatePassword: function() {
                this.validation.passwordLength = this.password.length >= 6;
                this.validation.passwordValid = this.validation.passwordLength;
            },
            validatePasswordConfirmationAfterTimeout: function() {
                this.runAfterTimeout("timeouts.passwordConfirmation", () => this.validatePasswordConfirmation())
            },
            validatePasswordConfirmation: function() {
                this.validation.passwordsMatch = this.password === this.passwordConfirmation;
                this.validation.passwordConfirmationValid = this.passwordConfirmation.length > 0 && this.validation.passwordsMatch
            },
            runAfterTimeout: function(timeoutVar, fct) {
                if (this[timeoutVar] != null ) {
                    clearTimeout(this[timeoutVar]);
                }
                this[timeoutVar] = setTimeout(() => {
                    fct()
                }, 200);
            }
        }
    }
</script>

<style scoped>
    .spinner {
        padding-left: 32px;
    }
</style>
