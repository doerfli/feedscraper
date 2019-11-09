<template>
  <div id="app">
    <Header />
    <router-view/>
  </div>
</template>

<style lang="scss">
  @import "~tailwindcss/base";
  @import "~tailwindcss/components";
  @import "~tailwindcss/utilities";

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
