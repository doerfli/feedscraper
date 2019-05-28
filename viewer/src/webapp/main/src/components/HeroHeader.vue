<template>
  <section class="hero">
    <div class="hero-body">
      <div class="container">
        <h1 class="title">
          {{title}}
        </h1>
        <!--
        <h2 class="subtitle">
          Hero subtitle
        </h2>
        -->
      </div>
    </div>
  </section>
</template>

<script>
    export default {
        name: "HeroHeader",
        props: ["title"]
    }
</script>

<style scoped>

</style>
