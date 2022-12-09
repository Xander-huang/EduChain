<template>
  <a-layout>
    <a-layout-sider
      id="person-menu-layout"
      v-model="collapsed"
      :trigger="null"
      collapsible
    >
      <div id="user-info">
        <a-avatar style="margin-top: -5px; background: #f56a00" size="small">
          U
        </a-avatar>
        <span style="margin-left: 5px" v-if="!collapsed">
          {{ userInfo.name }}
        </span>
      </div>
      <a-menu
        theme="dark"
        mode="inline"
        :default-selected-keys="[$route.path]"
        @click="getMenuItem"
      >
        <a-menu-item key="/home">
          <a-icon type="home" />
          <span>Home&ensp;&ensp;&ensp;</span>
        </a-menu-item>
        <a-menu-item key="/user">
          <a-icon type="user" />
          <span>Person&ensp;&ensp;&ensp;</span>
        </a-menu-item>
        <a-sub-menu>
          <span slot="title">
            <a-icon type="appstore" />
            <span>Function</span>
          </span>
          <a-menu-item key="/person/education">
            <a-icon type="database" />
            <span> Education</span>
          </a-menu-item>
          <a-menu-item key="/person/graduation">
            <a-icon type="link" />
            <span> UpChain</span>
          </a-menu-item>
          <a-menu-item key="/person/achieve">
            <a-icon type="trophy" />
            <span> Achieve</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content id="person-main-layout">
      <Navigation :collapsed="collapsed" :isCollapsed="isCollapsed" />
      <keep-alive>
        <router-view v-if="$route.meta.keepAlive"></router-view>
      </keep-alive>
      <router-view v-if="!$route.meta.keepAlive"></router-view>
    </a-layout-content>
  </a-layout>
</template>
<script>
import Navigation from "../../components/Navigation";
import { mapState } from "vuex";
export default {
  name: "Menu",
  components: {
    Navigation,
  },
  mounted() {
    if (this.$route.path == "/home") {
      this.$router.replace({
        name: "Home",
      });
    }
  },
  data() {
    return {
      collapsed: false,
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
  },
  methods: {
    isCollapsed() {
      if (this.collapsed) {
        this.collapsed = false;
      } else {
        this.collapsed = true;
      }
    },
    getMenuItem(e) {
      let key = e.key;
      if (key == "/home") {
        this.$router.replace({
          name: "Home",
        });
      } else if (key == "/user") {
        this.$router.replace({
          name: "Basic",
        });
      } else if (key == "/person/education") {
        this.$router.replace({
          name: "Education",
        });
      } else if (key == "/person/graduation") {
        this.$router.replace({
          name: "Graduation",
        });
      } else if (key == "/person/achieve") {
        this.$router.replace({
          name: "Achieve",
        });
      }
    },
  },
};
</script>

<style>
#person-menu-layout {
  height: 764px;
}
#user-info {
  overflow: hidden;
  white-space: nowrap;
  cursor: pointer;
  height: 64px;
  margin-bottom: 15px;
  padding: 20px 0;
  color: rgba(255, 255, 255, 0.65);
  transition: 0.3s;
  background: rgba(255, 255, 255, 0.12);
}
#user-info:hover {
  color: #fff;
}
#person-main-layout {
  background: #fff;
  height: 764px;
  overflow: auto;
}
</style>