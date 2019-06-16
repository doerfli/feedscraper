<template>
  <section class="section">
    <div class="container">
      <h1 class="title">{{title}}</h1>
      <h2 class="subtitle" v-if="subtitle">
        {{subtitle}}
      </h2>
    </div>
  </section>
</template>

<script>
    export default {
        name: "HeroHeader",
        props: ["title", "subtitle"]
    }
</script>

<style scoped>

</style>
