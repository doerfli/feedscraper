<template>
  <div id="app">
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
