// vue.config.js
module.exports = {
    // options...
    outputDir: "../generated/static",
    css: {
        loaderOptions: {
            sass: {
                data: `@import "@/styles/global.scss";`
            }
        }
    }
}
