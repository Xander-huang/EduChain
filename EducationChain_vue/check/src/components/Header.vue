<template>
  <a-layout>
    <a-layout-header id="head-layout-layout">
      <div id="head-title">
        <img
          v-if="mobile"
          id="head-title-img"
          src="/icon/simple_favicon_old.png"
          alt=""
        />
        <img
          v-else
          src="/icon/full_favicon_check_en.png"
          style="height: 30px"
          alt=""
        />
      </div>
    </a-layout-header>
  </a-layout>
</template>

<script>
export default {
  name: "Header",
  computed: {
    mobile() {
      return this.$mobile;
    },
  },
};
</script>

<style scoped>
#head-layout-layout {
  background: #fff;
  height: 51px;
  display: flex;
  width: 100%;
  overflow: hidden;
  flex-wrap: nowrap;
  justify-content: space-between;
  box-shadow: 0 2px 8px #f0f1f2;
  z-index: 10;
  padding: 0 50px;
}
#head-title {
  line-height: 51px;
  font-family: inherit;
}
#head-title-img {
  height: 30px;
}
</style>