<template>
    <a-layout>
      <a-layout-content id="login-layout">
        <div id="login-main-layout" :style="{ width: mobile ? '350px' : '472px' }">
          <a-form id="login-form-layout" layout="horizontal" :form="form" @submit="loginSubmit" @submit.native.prevent>
            <!-- 账号 -->
            <a-form-item>
              <a-input v-decorator="[
                'account',
                {
                  rules: [
                    { required: true, message: 'please enter your account' },
                    {
                      pattern:
                        /(^[\w-]+@[\w-]+(\.[\w-]+)+$)|(^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)/,
                      message: 'please enter correct account',
                    },
                  ],
                },
              ]" placeholder="Account">
                <a-icon slot="prefix" type="user" style="color: rgba(0, 0, 0, 0.25)" />
              </a-input>
            </a-form-item>
            <!-- 密码 -->
            <a-form-item>
              <a-input-password v-decorator="[
                'password',
                {
                  rules: [{ required: true, message: 'please enter your password' }],
                },
              ]" type="password" placeholder="Password">
                <a-icon slot="prefix" type="lock" style="color: rgba(0, 0, 0, 0.25)" />
              </a-input-password>
            </a-form-item>
  
            <!-- 验证码 -->
            <a-form-item>
              <a-input style="float: left; width: 60%" v-decorator="[
                'captcha',
                {
                  rules: [{ required: true, message: 'please enter your captcha' }],
                },
              ]" placeholder="captcha">
                <a-icon slot="prefix" type="safety" style="color: rgba(0, 0, 0, 0.25)" />
              </a-input>
              <img id="login-captchaImg" :src="img" @click="updateCaptchaImg" />
            </a-form-item>
            <!-- pem文件 -->
            <a-form-item>
              <!-- <a-upload name="file" :multiple="true" action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
              :headers="headers" @change="handleChange" v-model="file">
              <a-button >
                <a-icon type="upload" /> Click to Upload your KeyFile
              </a-button>
            </a-upload> -->
              <a-input style="border-style: hidden ;" v-decorator="[
                'file',
              ]" type="file" @change="fileChange">
                <!-- <a-icon slot="prefix" type="safety" style="color: rgba(0, 0, 0, 0.25)" /> -->
              </a-input>
            </a-form-item>
            <!-- 记住登录 忘记密码 登录按钮 -->
            <a-form-item>
              <a-checkbox style="float: left" v-decorator="[
                'remember',
                {
                  valuePropName: 'checked',
                  initialValue: true,
                },
              ]">
                Remember me
              </a-checkbox>
              <a id="login-forgot" style="float: right" href="javascript:;" @click="forgotFrom">
                Forget your password
              </a>
              <a-button id="login-form-button" type="primary" html-type="submit" :loading="isLoad">
                Login
              </a-button>
              Or
              <a href="javascript:;" @click="registFrom"> Register Now! </a>
            </a-form-item>
          </a-form>
        </div>
      </a-layout-content>
    </a-layout>
  </template>
  
  <script>
  import { mapState, mapActions } from "vuex";
  
  import { reqLogin } from "../api"
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
        account: localStorage.getItem("PersonAccount"),
        password: localStorage.getItem("PersonPassword"),
      });
    },
    data() {
      return {
        isLoad: false,
        headers: {
          authorization: 'authorization-text',
        },
        file: ''
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
      // 注册界面
      registFrom(e) {
        e.preventDefault();
        this.$bus.$emit("switchLoginRegister", "register");
        this.$router.replace({
          name: "Register",
        });
      },
      //文件上传
      handleChange(info) {
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList);
        }
        if (info.file.status === 'done') {
          this.$message.success(`${info.file.name} file uploaded successfully`);
          console.log(this.file);
        } else if (info.file.status === 'error') {
          this.$message.error(`${info.file.name} file upload failed.`);
        }
  
      },
      fileChange(e) {
        var reader = new FileReader()
        reader.readAsText(e.target.files[0])
        this.file = reader.result
        console.log(this.file)
      },
      // 登录
      loginSubmit(e) {
        e.preventDefault();
        // console.log(e)
        console.log(e.target[3].files[0])
        this.form.validateFields((err, values) => {
          console.log(values)
          values.type=0
          // values.append('type','0')
          console.log(values)
          if (!err) {
            // 验证码校验
            if (values.captcha != this.imgKey) {
              this.isLoad = true;
              var formData = new FormData();
  
  
              var reader = new FileReader()
              reader.readAsBinaryString(e.target[3].files[0])
              // console.log(reader)
  
              formData.append("account", values.account);
              formData.append("password", values.password);
  
              formData.append("file", e.target[3].files[0])
              // formData.append("file", e.target[3].files[0]);
              // console.log(formData.get('file'))
              // console.log(formData)
              let result = this.login(formData);
              // console.log(result)
              result
                .then((value) => {
                  this.$message.success("Success to login ");
                  this.$router.push({
                    name: "Home",
                  });
                  // console.log("123123123")
                  if (values.remember == true) {
                    localStorage.setItem("PersonAccount", values.account);
                    localStorage.setItem("PersonPassword", values.password);
                  } else {
                    localStorage.removeItem("PersonAccount");
                    localStorage.removeItem("PersonPassword");
                  }
                })
                .catch((error) => {
                  this.isLoad = false;
                });
            } else {
              this.$message.error("captcha error");
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