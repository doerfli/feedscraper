<template>
  <section class="container mx-auto mt-5 mb-6">
    <div>
      <h1 class="antialiased font-bold capitalize tracking-wide text-3xl inline-block text-teal-700">{{title}}</h1>
      <h2 class="antialiased font-medium text-1xl pl-3 inline-block" v-if="subtitle">
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
