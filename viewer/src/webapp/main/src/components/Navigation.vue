<template>
  <nav class="flex items-center justify-between flex-wrap bg-teal-500 p-6">
    <div class="flex items-center flex-shrink-0 text-white mr-6">
      <i class="fas fa-stream fa-2x mr-6"></i>
      <span class="font-semibold text-xl tracking-tight">
        <router-link to="/">Feedscraper <span class="ml-2">&beta;&epsilon;&tau;&alpha;</span></router-link>
      </span>
    </div>
    <div class="block lg:hidden">
      <button class="flex items-center px-3 py-2 border rounded text-teal-200 border-teal-400 hover:text-white hover:border-white">
        <svg class="fill-current h-3 w-3" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><title>Menu</title><path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"/></svg>
      </button>
    </div>
    <div class="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
      <div class="text-sm lg:flex-grow">
        <!-- left menu -->
      </div>
      <div>
        <a class="inline-block mr-4 text-white hover:text-teal-200" href="mailto:feedscraper@bytes.li?subject=Feedback">Feedback</a>
        <div v-if="isAuthenticated" >
              <span>
                <i class="fas fa-user"></i>
              </span>&nbsp;
          {{username}}
        </div>
        <a           class="inline-block mr-4 text-white hover:text-teal-200" v-if="!isLoginPage && isAuthenticated" v-on:click="logout">Logout</a>
        <router-link class="inline-block mr-4 text-white hover:text-teal-200" v-if="!isLoginPage && !isAuthenticated" to="/login" >Sign in</router-link>
        <router-link class="inline-block mr-4 text-white hover:text-teal-200" v-if="isLoginPage" to="/signup" >Sign up</router-link>
      </div>
    </div>
  </nav>
</template>

<script>
    export default {
      name: "Navigation",
      computed: {
        isAuthenticated: function() {
          return this.$store.state.session.token != null;
        },
        isLoginPage: function () {
          // console.log(this.$route);
          return this.$route.name === "login";
        },
        username: function() {
          return this.$store.state.session.username;
        }
      },
      methods: {
        logout: function () {
          this.$store.dispatch("session/logout");
        },
      }
    }
</script>

<style scoped lang="scss">

</style>
