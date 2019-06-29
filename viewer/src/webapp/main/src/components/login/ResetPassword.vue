<template>
    <div>
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
                            v-on:keyup="validatePasswordAfterTimeout"
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
                            v-on:keyup="validatePasswordConfirmationAfterTimeout"
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
                        <button class="button is-primary" v-on:click="setPwd">Set password</button>
                    </div>
                    <fieldset v-else disabled>
                        <div class="control">
                            <button class="button is-primary">Set password</button>
                        </div>
                    </fieldset>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "ResetPassword",
        data() {
            return {
                password: "",
                passwordConfirmation: "",
                validation: {
                    passwordValid: false,
                    passwordConfirmationValid: false,
                    passwordLength: true,
                    passwordsMatch: true
                },
                timeouts: {
                    username: null,
                    password: null
                }
            }
        },
        computed: {
            submitAllowed: function() {
                return this.validation.passwordValid && this.validation.passwordConfirmationValid;
            }
        },
        methods: {
            setPwd: function() {
                let token = this.$route.params.token;
                this.$store.dispatch("users/resetPassword", {token: token, password: this.password});
                this.password = "";
                this.passwordConfirmation = "";
                this.validation.passwordValid = false;
                this.validation.passwordConfirmationValid = false;
            },
            validatePasswordAfterTimeout: function() {
                this.runAfterTimeout("timeouts.password", () => this.validatePassword());
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
                }, 500);
            }
        }
    }
</script>

<style scoped>

</style>
