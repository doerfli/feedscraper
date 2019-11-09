<template>
  <div >
    <div >
      <nav  role="navigation" aria-label="main navigation">
        <div >
          <a  href="/">
            <i class="fas fa-stream fa-2x"></i>
          </a>

          <a role="button" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
          </a>
        </div>

        <div id="navbarBasicExample" >
          <div >

            <router-link to="/">Feedscraper &nbsp;&nbsp; <span >&beta;&epsilon;&tau;&alpha;</span></router-link>
          </div>

          <div >
            <a  href="mailto:feedscraper@bytes.li?subject=Feedback">Feedback</a>
            <div v-if="isAuthenticated" >
              <span>
                <i class="fas fa-user"></i>
              </span>&nbsp;
              {{username}}
            </div>
            <a           v-if="!isLoginPage && isAuthenticated" v-on:click="logout">Logout</a>
            <router-link v-if="!isLoginPage && !isAuthenticated" to="/login" >Sign in</router-link>
            <router-link v-if="isLoginPage" to="/signup" >Sign up</router-link>
          </div>
        </div>
      </nav>
    </div>
  </div>
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
