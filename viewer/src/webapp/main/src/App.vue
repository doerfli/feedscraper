<template>
  <div id="app">
    <Navigation />
    <Messages />
    <router-view/>
  </div>
</template>

<style lang="less">
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
