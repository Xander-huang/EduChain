<template>
  <div id="app">
    <div id="main-layout">
      <Header />
      <router-view></router-view>
      <Footer />
    </div>
  </div>
</template>

<script>
import Header from "./components/Header";
import Footer from "./components/Footer";
export default {
  name: "App",
  components: {
    Header,
    Footer,
  },
};
</script>

<style scoped>
#main-layout {
  text-align: center;
}
</style>
