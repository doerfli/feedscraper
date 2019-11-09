<template>
    <section>
        <div>
            <div>
                <div >
                        <span>
                            <i class="fas fa-cog fa-8x fa-spin"></i>
                        </span>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    import AXIOS from "@/http-common";
    import router from "@/router";
    import {TIMEOUT} from "../../messages-common";

    export default {
        name: "Confirmation",
        mounted() {
            this.$store.dispatch('messages/clear');
            let token = this.$route.params.token;
            console.log("confirmation token: " + token);
            AXIOS.get(`/users/confirm/${token}`).then(async response => {
                console.log(response);
                if (response.status === 200) {
                    console.log("token confirmation successful");
                    this.$store.dispatch('session/setToken', { token: response.data.token, username: response.data.username});
                    this.$store.dispatch('messages/add', { text: "Sign up confirmed! Welcome to Feedscraper!", type: "notification", timeout: TIMEOUT});
                    console.log("redirecting to /");
                    router.push({name: 'home'});
                }
            })
            .catch(error => {
                if (error.response) {
                    // The request was made and the server responded with a status code
                    // that falls out of the range of 2xx
                    console.log(error.response);
                    if (error.response.status === 400) {
                        this.$store.dispatch('messages/add', { text: "The confirmation token was invalid. Make sure you clicked the right link or sign up again to receive a new link.", type: "error"});
                    } else {
                        this.$store.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                    }
                } else if (error.request) {
                    this.$store.dispatch('messages/add', { text: "Server could not be contacted. Please try again later", type: "error"});
                } else {
                    this.$store.dispatch('messages/add', { text: "An unexpected error occured. Please try again", type: "error"});
                }
                router.push({name: 'signup'});
            })
        }
    }
</script>

<style scoped>

</style>
