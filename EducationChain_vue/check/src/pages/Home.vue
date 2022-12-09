<template>
  <div class="app" >
    <div id="app-layout">

      <Header />


        
      <Check />
      
    </div>
    <div style="height:275px"></div>
     <div class="footer">
      <Footer style="bottom: 0;"/>
     </div>
  </div>
</template>
  
<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import Check from "@/components/Check";
// import Authorize from "../components/Authorize";
export default {
  name: "Home",
  components: {
    Header,
    Footer,
    Check,
    // Authorize
  },
};
</script>
  
<style scoped>
#app-layout {
  text-align: center;
 height: 100%
}
.app{
  height: 100%;
}
.register {
  margin-top: auto;
  text-decoration: underline;
}
.footer{
  background: red;
  width: 100%;
  height: 100px;
  text-align: center;
}
</style>
  