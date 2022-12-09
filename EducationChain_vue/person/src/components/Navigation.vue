<template>
  <a-layout>
    <a-layout-header>
      <a-icon
        class="trigger"
        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
        @click="isCollapsed"
      />
      <a-breadcrumb id="breadcrumb-layout">
        <a-breadcrumb-item v-for="(path, index) in paths" :key="index">
          <a-icon style="font-size: 16px" v-if="path == 'Home'" type="home" />
          <a-icon
            style="font-size: 16px"
            v-else-if="path == 'Person'"
            type="user"
          />
          <a-icon
            style="font-size: 16px"
            v-else-if="path == 'Function'"
            type="appstore"
          />
          {{ path }}
        </a-breadcrumb-item>
      </a-breadcrumb>
      <div id="head-menu" v-if="userInfo.name">
        <a class="icon-btn" @click="showModal">
          <a-icon type="logout" /> <span v-if="!mobile">Exit</span>
        </a>
        <a-modal
          title="Tip"
          :visible="visible"
          :confirm-loading="confirmLoading"
          @ok="logoutSubmit"
          @cancel="handleCancel"
        >
          <p>Whether to exit the system?</p>
        </a-modal>
      </div>
    </a-layout-header>
  </a-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "Navigation",
  props: ["collapsed", "isCollapsed"],
  data() {
    return {
      visible: false,
      confirmLoading: false,
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    mobile() {
      return this.$mobile;
    },
    paths() {
      let pathList = this.$route.path.split("/");
      for (let i = 0; i < pathList.length; i++) {
        if (pathList[i] == "home") {
          pathList.splice(i, 1, "Home");
        } else if (pathList[i] == "user") {
          pathList.splice(i, 1, "Person");
        } else if (pathList[i] == "person") {
          pathList.splice(i, 1, "Function");
        } else if (pathList[i] == "education") {
          pathList.splice(i, 1, "Education");
        } else if (pathList[i] == "graduation") {
          pathList.splice(i, 1, "UpChain");
        } else if (pathList[i] == "achieve") {
          pathList.splice(i, 1, "Achieve");
        }
      }
      return pathList;
    },
  },
  methods: {
    ...mapActions("user", {
      logout: "logout",
    }),
    // 退出
    logoutSubmit() {
      this.confirmLoading = true;
      let result = this.logout();
      result
        .then((value) => {
          this.$message.success("success to exit!");
          this.$router.replace({
            name: "Login",
          });
          this.$bus.$emit("switchLoginRegister", "login");
          this.visible = false;
          this.confirmLoading = false;
        })
        .catch((error) => {
          this.$message.error("fail to exit!");
          this.visible = false;
          this.confirmLoading = false;
        });
    },
    // 退出提示
    showModal() {
      this.visible = true;
    },
    // 取消退出
    handleCancel(e) {
      this.visible = false;
    },
  },
};
</script>

<style scoped>
.ant-layout-header {
  position: relative;
  padding-left: 0 !important;
  background: #fff !important;
  overflow: hidden;
  box-shadow: 0 2px 8px #f0f1f2;
  z-index: 10;
}
.trigger {
  float: left;
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
}
.trigger:hover {
  color: #1890ff;
}
#breadcrumb-layout {
  float: left;
  margin-top: 18px;
  font-size: 16px;
}
#head-menu {
  position: absolute;
  right: 45px;
  top: 0px;
}
.icon-btn {
  color: rgba(0, 0, 0, 0.65);
  cursor: pointer;
}
.icon-btn:hover {
  color: #108ee9;
}
</style>