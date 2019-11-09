<template>
  <div id="app">
    <Header />
    <router-view/>
  </div>
</template>

<style lang="scss">
  @import "../node_modules/tailwindcss/dist/base.css";
  @import "../node_modules/tailwindcss/dist/components.css";
  @import "../node_modules/tailwindcss/dist/utilities.css";

  @import './../node_modules/@fortawesome/fontawesome-free/css/all.css';

  #app {

  }
</style>
<script>
  import Header from "./components/Header";

  export default {
      components: {
        Header
      }
    }
</script>
