<template>
    <div class="container mx-auto">
        <div class="flex mb-6">
            <div class="md:w-1/3 flex-col">
                <label class="input-label">Username</label>
            </div>
            <div  class="md:w-2/3 flex-col">
                <input
                    class="input-text"
                    v-model="username"
                    v-on:change="validateUsername"
                    v-on:keyup.13="login"
                    v-on:keyup="validateUsernameAfterTimeout"
                    type="email"
                    placeholder="Email"
                />
                <p v-if="!validation.usernamePattern" class="error-text">Please enter an email address</p>
            </div>
        </div>
        <div class="flex mb-6">
            <div class="md:w-1/3 flex-col">
                <label class="input-label">Password</label>
            </div>
            <div class="md:w-2/3 flex-col">
                <input
                    class="input-text"
                    v-on:change="validatePassword"
                    v-on:keyup.13="login"
                    v-on:keyup="validatePasswordAfterTimeout"

                    type="password"
                    placeholder="Password"
                    v-model="password"
                />
                <p v-if="!validation.passwordLength" class="error-text">Password must have at least 6 characters</p>
            </div>
        </div>
        <div class="flex mb-6" >
            <div class="md:w-1/3 flex-col">
            </div>
            <div class="md:w-2/3 flex-col">
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
                            <button class="submit-button" v-on:click="login">Sign in</button>
                            <ForgotPassword />
                        </div>
                        <div v-else disabled>
                            <button class="submit-button-disabled">Sign in</button>
                            <ForgotPassword v-bind:username="username" v-bind:username-valid="this.validation.usernameValid"/>
                        </div>
                    </div>
                </span>
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
                },
                loggingIn: false
            }
        },
        computed: {
            submitAllowed: function () {
                return this.validation.usernameValid && this.validation.passwordValid;
            }
        },
        methods: {
            login: function() {
                this.validateUsername();
                this.validatePassword();
                if (!this.submitAllowed) {
                    return;
                }
                this.loggingIn = true;
                this.$store.dispatch("users/login", {username: this.username, password: this.password}).then(result => {
                    console.log("login success:" + result);
                    if (!result) {
                        this.password = "";
                    }
                    this.loggingIn = false;
                });
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
                }, 200);
            }
        }
    };
</script>

<style scoped>
    .input-label {
        @apply .block .text-gray-600 .font-bold .mb-1 pt-2 .pr-4;
    }
    @screen md {
        .input-label {
            @apply .text-right .mb-0;
        }
    }
    .input-text {
        @apply .appearance-none .bg-gray-200 .border-2 .border-gray-200 .rounded .w-full .py-2 .px-4 .text-gray-700 .leading-tight;
    }
    .input-text:focus {
        @apply .outline-none .bg-white .border-teal-500;
    }
    .error-text {
        @apply .text-red-600;
    }
    .submit-button {
        @apply .bg-blue-500 .text-white .font-bold .py-2 .px-4 .rounded .mr-4;
    }
    .submit-button:hover {
        @apply .bg-blue-700;
    }
    .submit-button:focus {
        @apply .outline-none .shadow-outline;
    }
    .submit-button-disabled {
        @apply .bg-blue-200 .text-white .font-bold .py-2 .px-4 .rounded .mr-4;
    }
</style>
