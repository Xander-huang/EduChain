<template>
  <div id="page-layout">
    <a-row class="home-info-layout" type="flex" justify="space-around">
      <a-col
        class="info-card"
        style="background: #33cabb"
        :xs="22"
        :sm="22"
        :md="10"
        :lg="11"
        :xl="11"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="book" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Education Experience</p>
          <p style="font-size: 24px">{{ education }}</p>
        </div>
      </a-col>
      <a-col
        class="info-card"
        style="background: #f96868"
        :xs="22"
        :sm="22"
        :md="10"
        :lg="11"
        :xl="11"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="trophy" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Authentication Information</p>
          <p style="font-size: 24px">{{ achieve }}</p>
        </div>
      </a-col>
      <a-col
        style="background: #fff; text-align: left"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="23"
        :xl="23"
      >
        <a-card title="Home Card">
          <p>Welcome to use the personal end of Lifetime Teaching service platform</p>
          <p>You can view your own education information, submit your learning experience certification, and export your own information</p>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "PersonHome",
  mounted() {
    let result = this.getHomeInfo();
    result.then((value) => {
      this.education = value.education
      this.achieve = value.achieve
    });
  },
  data() {
    return {
      education: 0,
      achieve: 0,
    };
  },
  methods: {
    ...mapActions("user", {
      getHomeInfo: "getHomeInfo",
    }),
  },
};
</script>

<style scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
  padding: 30px;
  background: #f5f6fa;
}
.home-info-layout {
  margin-bottom: 24px;
}
.info-card {
  height: 106px;
  padding: 24px 24px;
  margin-bottom: 24px;
  color: #fff;
  overflow: hidden;
}
.info-card-icon {
  float: left;
  margin-left: 5px;
}
.info-card-text {
  float: right;
  margin-right: 5px;
  margin-top: 4px;
}
</style>