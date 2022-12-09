<template>
  <a-layout>
    <a-layout-content id="login-layout">
      <div
        id="login-main-layout"
        :style="{ width: mobile ? '350px' : '472px' }"
      >
        <a-form
          id="login-form-layout"
          layout="horizontal"
          :form="form"
          @submit="loginSubmit"
          @submit.native.prevent
        >
          <!-- 账号 -->
          <a-form-item>
            <a-input
              v-decorator="[
                'account',
                {
                  rules: [
                    { required: true, message: 'Please enter your account ' },
                    {
                      pattern: /(^[\w-]+@[\w-]+(\.[\w-]+)+$)|(^\d{7}$)/,
                      message: 'Please enter the correct format of account',
                    },
                  ],
                },
              ]"
              placeholder="Account"
            >
              <a-icon
                slot="prefix"
                type="user"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
          </a-form-item>
          <!-- 密码 -->
          <a-form-item>
            <a-input-password
              v-decorator="[
                'password',
                {
                  rules: [{ required: true, message: 'Please enter your password' }],
                },
              ]"
              type="password"
              placeholder="Password"
            >
              <a-icon
                slot="prefix"
                type="lock"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input-password>
          </a-form-item>
          <!-- 验证码 -->
          <a-form-item>
            <a-input
              style="float: left; width: 60%"
              v-decorator="[
                'captcha',
                {
                  rules: [{ required: true, message: 'Please enter the verification code' }],
                },
              ]"
              placeholder="CAPTCHA"
            >
              <a-icon
                slot="prefix"
                type="safety"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
            <img id="login-captchaImg" :src="img" @click="updateCaptchaImg" />
          </a-form-item>
          <!-- 记住登录 忘记密码 登录按钮 -->
          <a-form-item>
            <a-checkbox
              style="float: left"
              v-decorator="[
                'remember',
                {
                  valuePropName: 'checked',
                  initialValue: true,
                },
              ]"
            >
            Remember me
            </a-checkbox>
            <a
              id="login-forgot"
              style="float: right"
              href="javascript:;"
              @click="forgotFrom"
            >
            forget your password
            </a>
            <a-button
              id="login-form-button"
              type="primary"
              html-type="submit"
              :loading="isLoad"
            >
            Login
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "Login",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "login" });
  },
  created() {
    this.throttle_updateCaptchaImg = this._.throttle(
      this.logic_updateCaptchaImg,
      1000,
      false
    );
    this.logic_updateCaptchaImg();
  },
  mounted() {
    this.form.setFieldsValue({
      account: localStorage.getItem("AllianceAccount"),
      password: localStorage.getItem("AlliancePassword"),
    });
  },
  data() {
    return {
      isLoad: false,
    };
  },
  computed: {
    ...mapState("user", ["img", "imgKey"]),
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    ...mapActions("user", {
      captchaImg: "captchaImg",
      login: "login",
    }),
    // 改变登录验证码
    updateCaptchaImg() {
      this.throttle_updateCaptchaImg();
    },
    // 获取登录验证码
    logic_updateCaptchaImg() {
      this.captchaImg();
    },
    // 忘记密码界面
    forgotFrom(e) {
      e.preventDefault();
      this.$bus.$emit("switchLoginForgot");
      this.$router.replace({
        name: "Forgot",
      });
    },
    // 登录
    loginSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          // 验证码校验
          if (values.captcha == this.imgKey) {
            this.isLoad = true;
            let formData = new FormData();
            formData.append("account", values.account);
            formData.append("password", values.password);
            let result = this.login(formData);
          
            result
              .then((value) => {
                this.$message.success("Login successful");
                this.$router.push({
                  name: "Alliance",
                });
                if (values.remember == true) {
                  localStorage.setItem("AllianceAccount", values.account);
                  localStorage.setItem("AlliancePassword", values.password);
                } else {
                  localStorage.removeItem("AllianceAccount");
                  localStorage.removeItem("AlliancePassword");
                }
              })
              .catch((error) => {
                this.isLoad = false;
              });
          } else {
            this.$message.error("Captcha error");
            this.isLoad = false;
            this.updateCaptchaImg();
          }
        }
      });
    },
  },
};
</script>

<style scoped>
#login-layout {
  background: #fff;
}
#login-main-layout {
  margin: 75px auto;
  background: #fff;
  border-radius: 12px;
  overflow: auto;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
}
#login-form-layout {
  margin: 70px auto;
  width: 300px;
}
#login-captchaImg {
  width: 35%;
  margin-top: -10px;
}
#login-forgot {
  float: right;
}
#login-form-button {
  width: 100%;
}
</style>