<template>
    <div class="betainfo">
        <h1 class="title">
            <span class="has-text-warning">&beta;&epsilon;&tau;&alpha;</span> - this service is still in development
        </h1>
        <div class="content is-medium">
            We are working continuously to improve it and add new features.
            If you miss something or found a bug, you are most welcome to drop us a line at <a href="mailto:feedscraper@bytes.li?subject=Feedback">feedscraper@bytes.li</a>
        </div>
        <div class="content is-medium">
            Of course you can also tweet, like us or say something nice about this site on the social media platform of you choice.
        </div>
    </div>
</template>

<script>
    export default {
        name: "BetaInfo"
    }
</script>

<style scoped lang="scss">
    .betainfo {
        margin-top: 100px;
    }
</style>
