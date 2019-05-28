<template>
  <div id="app">
    <Navigation />
    <router-view/>
  </div>
</template>

<style lang="less">
#app {

}

</style>
<script>
    import Navigation from "./components/Navigation";
    export default {
        components: {Navigation}
    }
</script>
