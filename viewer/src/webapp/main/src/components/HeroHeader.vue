<template>
  <section>
    <div>
      <h1>{{title}}</h1>
      <h2 v-if="subtitle">
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
