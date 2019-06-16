<template>
  <div class="container is-fluid has-background-primary nomargin linebottom">
    <div class="container is-fullhd">
      <nav class="navbar is-primary" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
          <a class="navbar-item" href="https://bulma.io">
            <img src="../assets/logo.png">
          </a>

          <a role="button" class="navbar-burger burger" aria-label="menu" aria-expanded="false" data-target="navbarBasicExample">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
          </a>
        </div>

        <div id="navbarBasicExample" class="navbar-menu">
          <div class="navbar-start">

            <router-link to="/" class="navbar-item">Home</router-link>
            <router-link to="/about" class="navbar-item">About</router-link>
          </div>

          <div class="navbar-end">
            <a           v-if="!isLoginPage && isAuthenticated" class="navbar-item" v-on:click="logout">Logout</a>
            <router-link v-if="!isLoginPage && !isAuthenticated" to="/login" class="navbar-item">Sign in</router-link>
            <router-link v-if="isLoginPage" to="/signup" class="navbar-item">Signup</router-link>
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
  .container.nomargin {
    margin-left: 0;
    margin-right: 0;
  }

  .linebottom {
    border-bottom: 2px $fs_main_dark solid;
  }
</style>
