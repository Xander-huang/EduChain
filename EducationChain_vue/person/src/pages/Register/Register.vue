<template>
  <a-layout>
    <a-layout-content id="register-layout">
      <div id="register-main-layout" :style="{ width: mobile ? '350px' : '500px' }">
        <a-form id="register-form-layout" :style="{ width: mobile ? '250px' : '500px' }" :form="form"
          :label-col="{ span: 8, offset: 1 }" :wrapper-col="{ span: 12 }" @submit="registSubmit" @submit.native.prevent>
          <!-- 身份证号码 -->
          <a-form-item label="ID card">
            <a-input v-decorator="[
              'personId',
              {
                rules: [
                  { required: true, message: 'please enter your ID card number' },
                  {
                    pattern:
                      /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                    message: 'please enter valid ID card number',
                  },
                ],
              },
            ]" placeholder="please enter your ID card number">
            </a-input>
          </a-form-item>
          <!-- 真实姓名 -->
          <a-form-item label="Real Name">
            <a-input v-decorator="[
              'name',
              {
                rules: [{ required: true, message: 'please enter your real name' }],
              },
            ]" placeholder="please enter your real name">
            </a-input>
          </a-form-item>
          <!-- 性别 -->
          <a-form-item label="Gender">
            <a-select v-decorator="[
              'gender',
              {
                rules: [{ required: true, message: 'please choose your gender' }],
              },
            ]" placeholder="please choose your gender">
              <a-select-option value="男"> male </a-select-option>
              <a-select-option value="女"> female</a-select-option>
            </a-select>
          </a-form-item>
          <!-- 民族 -->
          <a-form-item label="Nation">
            <a-select show-search option-filter-prop="children" :filter-option="filterOption" v-decorator="[
              'nation',
              {
                rules: [{ required: true, message: 'please choose your nation' }],
              },
            ]" placeholder="please choose your nation">
              <a-select-option v-for="nation in nationData" :key="nation.id" :value="nation.name">
                {{ nation.name }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <!-- 住址 -->
          <a-form-item label="Address">
            <a-input v-decorator="[
              'address',
              {
                rules: [{ required: true, message: 'please enter your address ' }],
              },
            ]" placeholder="please enter your address">
            </a-input>
          </a-form-item>
          <!-- 联系电话 -->
          <a-form-item label="Phone Number">
            <a-input v-decorator="[
              'phone',
              {
                rules: [
                  { required: true, message: 'please enter your phone number' },
                  {
                    pattern: /^\d{11}$/,
                    message: 'please enter correct phone number',
                  },
                ],
              },
            ]" placeholder="please enter correct phone number">
            </a-input>
          </a-form-item>
          <!-- 邮箱 -->
          <a-form-item label="Email">
            <a-input v-decorator="[
              'email',
              {
                rules: [
                  { required: true, message: 'please enter your email' },
                  {
                    pattern: /^[\w-]+@[\w-]+(\.[\w-]+)+$/,
                    message: 'please enter correct email',
                  },
                ],
              },
            ]" placeholder="please enter your email">
            </a-input>
          </a-form-item>
          <!-- 验证码 -->
          <a-form-item label="Email CAPTCHA">
            <a-input style="float: left; width: 55%; margin-top: 3px;" v-decorator="[
              'captcha',
              {
                rules: [{ required: true, message: 'please enter captcha' }],
              },
            ]" placeholder="please enter captcha">
            </a-input>
            <a-button id="register-captcha-button" html-type="button" :disabled="isCaptcha"
              @click="registerEmailCaptcha">
              {{ captcha }}
            </a-button>
          </a-form-item>
          <!-- 登录密码 -->
          <a-form-item label="Password">
            <a-input-password v-decorator="[
              'password',
              {
                rules: [
                  { required: true, message: 'please enter password' },
                  { min: 8, message: 'Enter a password of 8 to 12 characters' },
                  { max: 12, message: 'Enter a password of 8 to 12 characters' },
                ],
              },
            ]" type="password" placeholder="please enter password">
            </a-input-password>
          </a-form-item>
          <!-- 二次确认 -->
          <a-form-item label="Confirm Password">
            <a-input-password v-decorator="[
              'secondaryPassword',
              {
                rules: [
                  { required: true, message: 'please confirm your password' },
                  { min: 8, message: 'Enter a password of 8 to 12 characters' },
                  { max: 12, message: 'Enter a password of 8 to 12 characters' },
                ],
              },
            ]" type="password" placeholder="please confirm your password">
            </a-input-password>
          </a-form-item>
          <!-- 注册按钮 -->
          <a-form-item :wrapper-col="{ span: 12, offset: mobile ? 0 : 6 }">
            <a-button style="width: 100%; margin-bottom: 13px" type="primary" html-type="submit" :loading="isLoad">
              Register Now
            </a-button>
            Or
            <a @click="loginFrom"> Login Now! </a>
          </a-form-item>
        </a-form>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script>
import { mapActions } from "vuex";
import { reqRegister } from "../../api";
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
      nationData: [
        { id: 1, name: "Han" },
        { id: 2, name: "Mongol" },
        { id: 3, name: "Hui" },
        { id: 4, name: "Tibetan" },
        { id: 5, name: "Uyghur" },
        { id: 6, name: "Miao" },
        { id: 7, name: "Yi" },
        { id: 8, name: "Zhuang" },
        { id: 9, name: "Buyi" },
        { id: 10, name: "Korean" },
        { id: 11, name: "Man" },
        { id: 12, name: "Dong" },
        { id: 13, name: "Yao" },
        { id: 14, name: "Bai" },
        { id: 15, name: "Tujia" },
        { id: 16, name: "Hani" },
        { id: 17, name: "Kazakh" },
        { id: 18, name: "Dai" },
        { id: 19, name: "Li" },
        { id: 20, name: "Lisu" },
        { id: 21, name: "Wa" },
        { id: 22, name: "She" },
        { id: 23, name: "Gaoshan " },
        { id: 24, name: "Lahu " },
        { id: 25, name: "Shui " },
        { id: 26, name: "Dongxiang " },
        { id: 27, name: "Naxi " },
        { id: 28, name: "Jingpo " },
        { id: 29, name: "Kirghiz " },
        { id: 30, name: "Du " },
        { id: 31, name: "Daur " },
        { id: 32, name: "Mulam" },
        { id: 33, name: "Qiang" },
        { id: 34, name: "Blang " },
        { id: 35, name: "Salar " },
        { id: 36, name: "Maonan" },
        { id: 37, name: "Gelao " },
        { id: 38, name: "Xibe" },
        { id: 39, name: "Achang" },
        { id: 40, name: "Pumi" },
        { id: 41, name: "Tajik " },
        { id: 42, name: "Nu " },
        { id: 43, name: "Uzbek " },
        { id: 44, name: "Russian " },
        { id: 45, name: "Evenki " },
        { id: 46, name: "De'ang" },
        { id: 47, name: "Bonan " },
        { id: 48, name: "Yugur " },
        { id: 49, name: "Gin " },
        { id: 50, name: "Tatar" },
        { id: 51, name: "Drung " },
        { id: 52, name: "Oroqin " },
        { id: 53, name: "Hezhen " },
        { id: 54, name: "Menba " },
        { id: 55, name: "Lhoba " },
        { id: 56, name: "Jino " },
      ],
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
        this.$message.error("please enter email");
      } else {
        // 60秒处理时间
        this.isCaptcha = true;
        this.$message.loading("Sending email......", 0);
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
              message: "Success to send ",
            });
          })
          .catch((error) => {
            this.$message.error("Fail to send");
          });
      }
    },
    // 查询民族列表
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
    // 注册表单
    registSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          // 密码二次校验
          if (values.password == values.secondaryPassword) {
            delete values.secondaryPassword;
            values.birthday =
              values.personId.slice(6, 10) +
              "-" +
              values.personId.slice(10, 12) +
              "-" +
              values.personId.slice(12, 14);
            this.isLoad = true;
            this.$message.loading("Registering......", 0);
            reqRegister(values)
              .then((value) => {

              
                console.log("-=-=-=-=-=-")
                console.log(value)
                let blob =new Blob([value],{type:'zip'})
                var url = window.URL.createObjectURL(blob);
                var a = document.createElement("a");
                a.download='KeyFile.zip'
                a.href = url;
                a.click();
                this.$message.success("Success to register");
                this.$bus.$emit("switchLoginRegister", "login");
                this.$router.replace({
                  name: "Login",
                });
              })
              .catch((error) => {
                console.log("error" + error)
                this.isLoad = false;
              });
          } else {
            this.$message.error("Two passwords are different");
          }
        }
      });
    },
    // 返回登录界面
    loginFrom(e) {
      e.preventDefault();
      this.$bus.$emit("switchLoginRegister", "login");
      this.$router.replace({
        name: "Login",
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
  margin: 55px auto;
}

#register-captcha-button {
  width: 41%;
  float: right;
  text-align: center;
  margin-top: 3px;
}
</style>