<template>
    <span>
        <a v-on:click="sendResetLink" >Forgot password?</a>&nbsp;
        <span v-if="showHelp">Enter your email address above and click again</span>
        <span v-if="mailSent">Password reset link sent - check your email</span>
    </span>
</template>

<script>
    import AXIOS from "@/http-common";
    import {TIMEOUT} from "../../messages-common";

    export default {
        name: "ForgotPassword",
        props: {
            username: String,
            usernameValid: Boolean
        },
        data: function() {
            return {
                showHelp: false,
                mailSent: false
            }
        },
        methods: {
            sendResetLink: function() {
                if (this.usernameValid) {
                    this.showHelp = false;
                    this.sendPasswordResetLink();
                } else {
                    this.showHelp = true
                }
            },
            sendPasswordResetLink() {
                AXIOS.post(`/users/passwordReset`, {
                    username: this.username
                }).then(response => {
                    console.log(response);
                    if (response.status === 200) {
                        console.log("password reset request successful");
                        this.$store.dispatch('messages/add', { text: "Password reset mail sent - check your email", type: "notification", timeout: TIMEOUT});
                    }
                }).catch(error => {
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        console.log(error.response);
                        this.$store.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                    } else if (error.request) {
                        this.$store.dispatch('messages/add', { text: "Server could not be contacted. Please try again later", type: "error"});
                    } else {
                        this.$store.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                    }
                })
            }
        }
    }
</script>

<style scoped lang="scss">

</style>
