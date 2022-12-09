<template>
  <div class="cell">
    <!-- 电话部分 -->
    <div v-if="editable" class="cell-input-wrapper">
      <a-input :value="value" @change="changeInfo" @pressEnter="check" />
      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Confirm Modification</span>
        </template>
        <a-icon type="check" class="cell-icon-check" @click="check" />
      </a-tooltip>
    </div>
    <!-- 文字部分 -->
    <div v-else class="cell-text-wrapper">
      {{ value || " " }}
      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Modify information</span>
        </template>
        <a-icon type="edit" class="cell-icon" @click="edit" />
      </a-tooltip>
    </div>
  </div>
</template>

<script>
export default {
  name: "BasicCell",
  props: {
    text: String,
  },
  data() {
    return {
      value: this.text,
      editable: false,
    };
  },
  methods: {
    // 修改数据
    changeInfo(e) {
      const value = e.target.value;
      this.value = value;
    },
    // 数据传回父组件
    check() {
      this.editable = false;
      this.$emit("change", this.value);
    },
    // 修改数据
    edit() {
      this.editable = true;
      this.$notification.open({
        message: "Modify phone number",
        description: "Please fill in the new phone number in the phone input box, click the confirmation button to complete the change",
      });
    },
  },
};
</script>

<style scoped>
.cell {
  position: relative;
}
.cell-input-wrapper,
.cell-text-wrapper {
  padding-right: 24px;
}
.cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}
.cell-icon,
.cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}
.cell-icon {
  line-height: 18px;
  display: none;
}
.cell-icon-check {
  line-height: 28px;
}
.cell:hover .cell-icon {
  display: inline-block;
}
.cell-icon:hover,
.cell-icon-check:hover,
.cell-icon-upload:hover {
  color: #108ee9;
}
</style>