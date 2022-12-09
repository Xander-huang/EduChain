<template>
  <div id="app">
    <div id="app-layout">
      <h3>{{id}}</h3>
      <Header />
      <div class="agree">
<AgreeButton />
      </div>
       
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import Check from "@/components/Check";
import AgreeButton from "@/components/AgreeButton";
// import Authorize from "../components/Authorize";
export default {
  name: "Home",
  components: {
    Header,
    Footer,
    Check,
    AgreeButton,
    // Authorize
  },
    data() {
    return {
      id: "",
    };
  },
  created() {
    this.id = this.$route.params.id
    console.log(this.$route.params.id);
  },

};
// let id = this.$route.params.id
</script>

<style scoped>
#app-layout {
  text-align: center;
}

.register {
  margin-top: auto;
  text-decoration: underline;
}
.agree{
    
    display: flex;
    justify-content: center;/*垂直居中*/
    align-items: center;/*水平居中*/
}
</style>
