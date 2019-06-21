<template>
  <div id="app" class="rootfullheight">
    <Header />
    <router-view/>
  </div>
</template>

<style lang="scss">
  @import './../node_modules/bulma/bulma.sass';
  @import './../node_modules/@fortawesome/fontawesome-free/css/all.css';

  #app {

  }

  html,
  body {
    height: 100%;
  }

  .rootfullheight {
    display: flex;
    flex-flow: column;
    height: 100%;
  }

  .fullheight {
    height: 100%;
  }

  .remainingheight {
    flex: 1 1 auto;
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
