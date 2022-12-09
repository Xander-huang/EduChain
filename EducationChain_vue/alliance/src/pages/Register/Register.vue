<template>
  <a-layout>
    <a-layout-content id="register-layout">
      <div
        id="register-main-layout"
        :style="{ width: mobile ? '350px' : '500px' }"
      >
        <a-form
          id="register-form-layout"
          :style="{ width: mobile ? '250px' : '500px' }"
          :form="form"
          :label-col="{ span: 8, offset: 1 }"
          :wrapper-col="{ span: 12 }"
          @submit="registSubmit"
          @submit.native.prevent
        >
          <!-- 联盟名称 -->
          <a-form-item label="Alliance Name">
            <a-input
              v-decorator="[
                'nodeName',
                {
                  rules: [
                    { required: true, message: 'Please enter the Name' },
                  ],
                },
              ]"
              placeholder="Please enter the name"
            >
            </a-input>
          </a-form-item>
          <!-- 联盟类型 -->
          <a-form-item label="Alliance Type">
            <a-select
              v-decorator="[
                'type',
                {
                  rules: [{ required: true, message: 'Please select the type' }],
                },
              ]"
              placeholder="Please select the type"
            >
              <a-select-option :value="0"> college </a-select-option>
              <a-select-option :value="1"> training organization </a-select-option>
            </a-select>
          </a-form-item>
          <!-- 管理员名称 -->
          <a-form-item label="Admin Name">
            <a-input
              v-decorator="[
                'userName',
                {
                  rules: [
                    { required: true, message: 'Please enter Admin Name ' },
                  ],
                },
              ]"
              placeholder="Please enter Admin Name "
            >
            </a-input>
          </a-form-item>
          <!-- 联系电话 -->
          <a-form-item label="Admin phone number">
            <a-input
              v-decorator="[
                'phone',
                {
                  rules: [
                    { required: true, message: 'Please enter phone number' },
                    {
                      pattern: /^\d{11}$/,
                      message: 'Please enter the valid phone number',
                    },
                  ],
                },
              ]"
              placeholder="Please enter phone number"
            >
            </a-input>
          </a-form-item>
          <!-- 邮箱 -->
          <a-form-item label="Admin Email">
            <a-input
              v-decorator="[
                'email',
                {
                  rules: [
                    { required: true, message: 'Please enter the Email' },
                    {
                      pattern: /^[\w-]+@[\w-]+(\.[\w-]+)+$/,
                      message: 'Please enter the valid Email',
                    },
                  ],
                },
              ]"
              placeholder="Please enter the Email"
            >
            </a-input>
          </a-form-item>
          <!-- 验证码 -->
          <a-form-item label="Email CAPTCHA">
            <a-input
              style="float: left; width: 55%; margin-top: 3px;"
              v-decorator="[
                'captcha',
                {
                  rules: [{ required: true, message: 'please enter the captcha' }],
                },
              ]"
              placeholder="please enter the captcha"
            >
            </a-input>
            <a-button
              id="register-captcha-button"
              html-type="button"
              :disabled="isCaptcha"
              @click="registerEmailCaptcha"
            >
              {{ captcha }}
            </a-button>
          </a-form-item>
          <!-- 登录密码 -->
          <a-form-item label="Admin Password">
            <a-input-password
              v-decorator="[
                'password',
                {
                  rules: [
                    { required: true, message: 'Confirm Password' },
                    { min: 8, message: 'Please enter 8~12 digit password' },
                    { max: 12, message: 'Please enter 8~12 digit password' },
                  ],
                },
              ]"
              type="password"
              placeholder="Please enter the login Password"
            >
            </a-input-password>
          </a-form-item>
          <!-- 二次确认 -->
          <a-form-item label="Comfirm Password">
            <a-input-password
              v-decorator="[
                'secondaryPassword',
                {
                  rules: [
                    { required: true, message: 'Please confirm the password' },
                    { min: 8, message: 'Please enter 8~12 digit password' },
                    { max: 12, message: 'Please enter 8~12 digit password' },
                  ],
                },
              ]"
              type="password"
              placeholder="Please confirm the password"
            >
            </a-input-password>
          </a-form-item>
          <!-- 注册按钮 -->
          <a-form-item :wrapper-col="{ span: 12, offset: mobile ? 0 : 6 }">
            <a-button
              style="width: 100%; margin-bottom: 13px"
              type="primary"
              html-type="submit"
              :loading="isLoad"
            >
              Register
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
import { mapActions } from "vuex";
export default {
  name: "Register",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "register" });
  },
  data() {
    return {
      captcha: "Get Code",
      isLoad: false,
      isCaptcha: false,
    };
  },
  computed: {
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    ...mapActions("user", {
      register: "register",
      emailCaptcha: "emailCaptcha",
    }),
    // 发送邮箱验证码
    registerEmailCaptcha(e) {
      e.preventDefault();
      let email = this.form.getFieldValue("email");
      // 验证邮箱部分
      if (email == undefined) {
        this.$message.error("please enter the email");
      } else {
        // 60秒处理时间
        this.isCaptcha = true;
        this.$message.loading("Sending captcha to your email......", 0);
        let result = this.emailCaptcha(email);
        result
          .then((value) => {
            let time60 = 60;
            let _this = this;
            this.captcha = time60;
            let count = setInterval(function () {
              if (time60 != 0) {
                time60 -= 1;
                _this.captcha = time60;
              } else {
                clearInterval(count);
                _this.isCaptcha = false;
                _this.captcha = "Get Code";
              }
            }, 1000);
            this.$notification.open({
              message: "Captcha has been sent to the Email",
            });
          })
          .catch((error) => {
            this.$message.error("Failed to send");
          });
      }
    },
    // 注册表单
    registSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          // 密码二次校验
          if (values.password == values.secondaryPassword) {
            this.isLoad = true;
            this.$message.loading("applying......", 0);
            let result = this.register(values);
            result
              .then((value) => {
                this.$message.success("apply successfully");
                this.$bus.$emit("switchLoginRegister", "login");
                this.$router.replace({
                  name: "Login",
                });
              })
              .catch((error) => {
                this.isLoad = false;
              });
          } else {
            this.$message.error("The two passwords do not match");
          }
        }
      });
    },
  },
};
</script>

<style scoped>
#register-layout {
  background: #fff;
}
#register-main-layout {
  margin: 30px auto;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
}
#register-form-layout {
  margin: 30px auto;
}
#register-captcha-button {
  width: 41%;
  float: right;
  text-align: center;
  margin-top: 3px;
}
</style>