<template>
  <a-layout>
    <a-layout-content id="forgot-layout">
      <div
        id="forgot-main-layout"
        :style="{ width: mobile ? '350px' : '500px' }"
      >
        <a-form
          id="forgot-form-layout"
          :style="{ width: mobile ? '250px' : '500px' }"
          :form="form"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 15 }"
          @submit="forgotSubmit"
          @submit.native.prevent
        >
          <!-- 账号 -->
          <a-form-item label="Email">
            <a-input
              v-decorator="[
                'email',
                {
                  rules: [
                    { required: true, message: 'Please enter the email ' },
                    {
                      pattern: /(^[\w-]+@[\w-]+(\.[\w-]+)+$)/,
                      message: 'Please enter the correct email',
                    },
                  ],
                },
              ]"
              placeholder="Please enter the email"
            >
              <a-icon
                slot="prefix"
                type="user"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
          </a-form-item>
          <!-- 重置验证码 -->
          <a-form-item label="Captcha">
            <a-input
              style="float: left; width: 55%"
              v-decorator="[
                'captcha',
                {
                  rules: [{ required: true, message: 'please enter captcha' }],
                },
              ]"
              placeholder="please enter captcha"
            >
              <a-icon
                slot="prefix"
                type="safety"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
            <a-button
              id="forgot-captcha-button"
              html-type="button"
              :disabled="isCaptcha"
              @click="resetEmailCaptcha"
            >
              {{ captcha }}
            </a-button>
          </a-form-item>
          <!-- 重置密码 -->
          <a-form-item label="Reset Password">
            <a-input-password
              v-decorator="[
                'password',
                {
                  rules: [
                    { required: true, message: 'Please enter the reset password' },
                    { min: 8, message: 'Enter a password of 8 to 12 characters' },
                    { max: 12, message: 'Enter a password of 8 to 12 characters' },
                  ],
                },
              ]"
              type="password"
              placeholder="Please enter the reset password"
            >
              <a-icon
                slot="prefix"
                type="lock"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input-password>
          </a-form-item>
          <!-- 二次确认 -->
          <a-form-item label="Confirm password">
            <a-input-password
              v-decorator="[
                'secondaryPassword',
                {
                  rules: [
                    { required: true, message: 'please confirm password' },
                    { min: 8, message: 'Enter a password of 8 to 12 characters' },
                    { max: 12, message: 'Enter a password of 8 to 12 characters' },
                  ],
                },
              ]"
              type="password"
              placeholder="please confirm password"
            >
              <a-icon
                slot="prefix"
                type="lock"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input-password>
          </a-form-item>
          <!-- 重置密码按钮 -->
          <a-form-item :wrapper-col="{ span: 12, offset: mobile ? 0 : 6 }">
            <a-button type="primary" html-type="submit" :loading="isLoad">
              Reset
            </a-button>
            <a-button
              id="forgot-clear"
              :loading="isLoad"
              style=""
              @click="clear"
            >
              clear
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
  name: "Forgot",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "Forgot" });
  },
  data() {
    return {
      captcha: "Get code",
      isCaptcha: false,
      isLoad: false,
    };
  },
  computed: {
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    ...mapActions("user", {
      emailCaptcha: "emailCaptcha",
      reset: "reset",
    }),
    // 发送重置邮箱验证码
    resetEmailCaptcha(e) {
      e.preventDefault();
      let email = this.form.getFieldValue("email");
      // 验证账号部分
      if (email == undefined) {
        this.$message.error("please enter email");
      } else {
        // 60秒处理时间
        this.isCaptcha = true;
        this.$message.loading("Sending captcha......", 0);
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
                _this.captcha = "Get code";
              }
            }, 1000);
            this.$notification.open({
              message: "Success to send",
            });
          })
          .catch((error) => {
            this.isCaptcha = false;
            this.$message.error("Fail to send");
          });
      }
    },
    // 发送重置表单
    forgotSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          // 密码二次校验
          if (values.secondaryPassword == values.password) {
            let formData = new FormData();
            formData.append("email", values.email);
            formData.append("password", values.password);
            formData.append("captcha", values.captcha);
            this.isLoad = true;
            this.$message.loading("Reseting password......", 0);
            let result = this.reset(formData);
            result
              .then((value) => {
                this.$message.success("Success to reset");
                this.$bus.$emit("switchLoginRegister", "login");
                this.$router.replace({
                  name: "Login",
                });
              })
              .catch((error) => {
                this.isLoad = false;
              });
          } else {
            this.$message.error("Two passwords are different");
          }
        }
      });
    },
    // 清空表单
    clear() {
      this.form.setFieldsValue({
        email: undefined,
        password: undefined,
        secondaryPassword: undefined,
        captcha: undefined,
      });
    },
  },
};
</script>

<style scoped>
#forgot-layout {
  background: #fff;
}
#forgot-main-layout {
  margin: 80px auto;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
}
#forgot-form-layout {
  margin: 70px auto;
  max-width: 400px;
  min-width: 25px;
}
#forgot-captcha-button {
  width: 41%;
  float: right;
  text-align: center;
}
#forgot-clear {
  margin-left: 10px;
}
</style>