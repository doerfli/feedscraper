<template>
  <div class="home fullheight">
    <Feeds />
  </div>
</template>

<script>

  import Feeds from "../components/feed/Feeds";

  export default {
  name: 'home',
  components: {
    Feeds
  }
}
</script>
