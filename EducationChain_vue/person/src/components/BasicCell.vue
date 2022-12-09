<template>
  <div class="cell">
    <!-- 电话部分 -->
    <div v-if="editable && type == 'phone'" class="cell-input-wrapper">
      <a-input :value="value" @change="changeInfo" @pressEnter="check" />
      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Confirm Change</span>
        </template>
        <a-icon type="check" class="cell-icon-check" @click="check" />
      </a-tooltip>
    </div>
    <!-- 邮箱部分 -->
    <div v-else-if="editable && type == 'email'" class="cell-input-wrapper">
      <a-input class="cell-input-email" :value="value" @change="changeInfo" />

      <a-input
        class="cell-input-captcha"
        :value="captcha"
        placeholder="Enter captcha"
        @change="changeCaptcha"
      />
      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Send captcha</span>
        </template>
        <a-icon
          class="cell-icon-upload"
          type="upload"
          style=""
          @click="getCaptcha"
        />
      </a-tooltip>
      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Confirm Change</span>
        </template>
        <a-icon type="check" class="cell-icon-check" @click="check" />
      </a-tooltip>
    </div>
    <!-- 文字部分 -->
    <div v-else class="cell-text-wrapper">
      {{ value || " " }}

      <a-tooltip placement="bottomRight">
        <template slot="title">
          <span>Change Information</span>
        </template>
        <a-icon type="edit" class="cell-icon" @click="edit" />
      </a-tooltip>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "BasicCell",
  props: {
    text: String,
    type: String,
  },
  data() {
    return {
      value: this.text,
      editable: false,
      captcha: "",
    };
  },
  methods: {
    ...mapActions("user", {
      emailCaptcha: "emailCaptcha",
    }),
    // 修改数据
    changeInfo(e) {
      const value = e.target.value;
      this.value = value;
    },
    // 修改验证码
    changeCaptcha(e) {
      const value = e.target.value;
      this.captcha = value;
    },
    // 数据传回父组件
    check() {
      this.editable = false;
      this.$emit("change", this.value, this.captcha);
      this.captcha = "";
    },
    // 修改数据
    edit() {
      this.editable = true;
      if (this.type == "phone") {
        this.$notification.open({
          message: "Change the phone number",
          description: "Please fill in the new phone number in the phone input box, click the confirmation button to complete the change",
        });
      } else {
        this.$notification.open({
          message: "Change Email",
          description:
            "Please fill in the new mailbox in the mailbox input box, click the send button and receive the verification code in the new mailbox, fill in the verification code and click the confirm button to finish the modification",
        });
      }
    },
    // 获取邮箱验证码
    getCaptcha() {
      this.$message.loading("Sending captcha to the email......", 0);
      let result = this.emailCaptcha(this.value);
      result.then((value) => {
        this.$message.destroy();
        this.$notification.open({
          message: "Have sended captcha to the email!",
        });
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
.cell-input-email {
  width: 40%;
}
.cell-input-captcha {
  width: 30%;
  margin-left: 5px;
}
.cell-icon,
.cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}
.cell-icon-upload {
  position: absolute;
  right: 25px;
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
.cell-icon-upload {
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