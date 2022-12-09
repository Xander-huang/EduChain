<template>
  <a-layout>
    <a-layout-header
      id="head-layout-layout"
      v-if="
        $route.path == '/login' ||
        $route.path == '/forgot' ||
        $route.path == '/register'
      "
    >
      <div id="head-title">
        <img
          v-if="mobile"
          id="head-title-img"
          src="/icon/simple_favicon.png"
          alt=""
        />
        <img v-else src="/icon/full_favicon_en.png" style="height: 30px" alt="" />
      </div>
      <div v-if="!token">
        <a-dropdown v-if="mobile" placement="bottomCenter">
          <a>
            <a-icon
              type="menu"
              style="font-size: 20px; color: rgba(0, 0, 0, 0.65)"
            />
          </a>
          <a-menu slot="overlay">
            <a-menu-item @click="loginFrom" key="login">
              <a-icon type="login" />
              Login Page
            </a-menu-item>
            <a-menu-item @click="registFrom" key="register">
              <a-icon type="audit" />
              Affiliate Application
            </a-menu-item>
          </a-menu>
        </a-dropdown>
        <a-menu
          id="head-menu"
          v-else
          mode="horizontal"
          theme="light"
          v-model="current"
        >
          <a-menu-item @click="loginFrom" key="login">
            <a-icon type="login" />
            Login Page
          </a-menu-item>
          <a-menu-item @click="registFrom" key="register">
            <a-icon type="audit" />
            
Alliance application
          </a-menu-item>
        </a-menu>
      </div>
    </a-layout-header>
  </a-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "Header",
  mounted() {
    if (this.$route.path == "/login") {
      this.current = ["login"];
    } else if (this.$route.path == "/register") {
      this.current = ["register"];
    } else {
      this.current = [];
    }
    this.$bus.$on("switchLoginForgot", () => {
      let indexLogin = this.current.indexOf("login");
      if (indexLogin != -1) {
        this.current.splice(indexLogin, 1);
      }
    });
    this.$bus.$on("switchLoginRegister", (goFrom) => {
      let indexLogin = this.current.indexOf("login");
      let indexRegist = this.current.indexOf("register");
      if (goFrom == "register") {
        if (indexLogin != -1) {
          this.current.splice(indexLogin, 1);
        }
        if (indexRegist == -1) {
          this.current.push("register");
        }
      } else {
        if (indexRegist != -1) {
          this.current.splice(indexRegist, 1);
          console.log(indexRegist, this.current);
        }
        if (indexLogin == -1) {
          this.current.unshift("login");
        }
      }
    });
  },
  beforeDestroy() {
    this.$bus.$off("switchLoginForgot");
    this.$bus.$off("switchLoginRegister");
  },
  data() {
    return {
      current: [],
    };
  },
  computed: {
    ...mapState("user", ["token", "userInfo"]),
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    // 登录界面
    loginFrom() {
      this.$router.replace({
        name: "Login",
      });
    },
    // 联盟申请界面
    registFrom() {
      this.$router.replace({
        name: "Register",
      });
    },
  },
};
</script>

<style scoped>
#head-layout-layout {
  background: #fff;
  height: 51px;
  display: flex;
  overflow: hidden;
  flex-wrap: nowrap;
  justify-content: space-between;
  box-shadow: 0 2px 8px #f0f1f2;
  z-index: 10;
  padding: 0 50px;
}
#head-title {
  float: left;
  line-height: 51px;
  font-family: inherit;
}
#head-title-img {
  height: 30px;
}
#head-menu {
  font-size: 10px;
  margin-top: 3px;
}
</style>