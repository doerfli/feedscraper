<template>
  <div id="app">
    <Header />
    <router-view/>
  </div>
</template>

<style lang="scss">
  @import "~tailwindcss/dist/base";
  @import "~tailwindcss/dist/components";
  @import "~tailwindcss/dist/utilities";
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
