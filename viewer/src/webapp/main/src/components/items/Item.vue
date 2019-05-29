<template>
  <li>
    {{data.title}}
  </li>
</template>

<script>
    export default {
        name: "Item",
        props: {
            data: {}
        }
    }
</script>

<style scoped>

</style>
