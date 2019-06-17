<template>
  <div id="app" class="fullheight">
    <Navigation />
    <Messages />
    <router-view/>
  </div>
</template>

<style lang="scss">
  @import './../node_modules/bulma/bulma.sass';
  @import './../node_modules/@fortawesome/fontawesome-free/css/all.css';

  #app {

  }

  html,
  body,
  .fullheight {
    height: 100%;
  }
</style>
<script>
  import Navigation from "./components/Navigation";
  import Messages from "./components/Messages";

  export default {
      components: {
        Navigation,
        Messages
      }
    }
</script>
