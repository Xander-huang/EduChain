<template>
  <div id="page-layout" @click="passwordWindowBlur">
    <div id="basic-title-layout">
      <b>Basic Personal Information</b>
      <ul id="basic-menu-layout">
        <li id="password-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Change Password</span>
            </template>
            <a-icon type="key" class="icon-btn" @click="updatePasswordWindow" />
          </a-tooltip>
          <!-- 修改密码-窗口 -->
          <div id="password-window" ref="idPasswordWindow">
            <a-form
              :form="form"
              layout="horizontal"
              @submit="updatePasswordSubmit"
              @submit.native.prevent
            >
              <!-- 修改密码-原密码 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'originPwd',
                    {
                      rules: [{ required: true, message: 'Please enter the old password!' }],
                    },
                  ]"
                  type="password"
                  placeholder="the old password"
                ></a-input>
              </a-form-item>
              <!-- 修改密码-新密码 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'newpwd',
                    {
                      rules: [
                        { required: true, message: 'Please enter new password!' },
                        { min: 8, message: 'Please enter an 8-12 digit password!' },
                        { max: 12, message: 'Please enter an 8-12 digit password!' },
                      ],
                    },
                  ]"
                  type="password"
                  placeholder="new password"
                ></a-input>
              </a-form-item>
              <!-- 修改密码-新密码-二次确认 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'secondaryNewPWD',
                    {
                      rules: [
                        { required: true, message: 'Please confirm new password!' },
                        { min: 8, message: 'Please enter an 8-12 digit password!' },
                        { max: 12, message: 'Please enter an 8-12 digit password!' },
                      ],
                    },
                  ]"
                  type="password"
                  placeholder="confirm new password"
                ></a-input>
              </a-form-item>
              <!-- 修改密码-按钮 -->
              <a-form-item :wrapper-col="{ span: 24, offset: 0 }">
                <a-button type="primary" html-type="submit" :loading="isLoad">
                  Change
                </a-button>
                <a-button
                  id="basic-form-button"
                  @click="clear"
                >
                  Clear
                </a-button>
              </a-form-item>
            </a-form>
          </div>
        </li>
      </ul>
    </div>
    <div>
      <a-descriptions bordered>
        <a-descriptions-item label="Name">
          {{ userInfo.name }}
        </a-descriptions-item>
        <a-descriptions-item label="ID Card">
          {{ userInfo.personId }}
        </a-descriptions-item>
        <a-descriptions-item label="Gender">
          {{ userInfo.gender }}
        </a-descriptions-item>
        <a-descriptions-item label="Nation">
          {{ userInfo.nation }}
        </a-descriptions-item>
        <a-descriptions-item label="Birthday">
          {{ userInfo.birthday }}
        </a-descriptions-item>
        <a-descriptions-item label="Home Address">
          {{ userInfo.address }}
        </a-descriptions-item>
        <a-descriptions-item label="Phone number">
          <basic-cell
            :text="userInfo.phone"
            type="phone"
            @change="
              ($event, captcha) => updateUserInfo('phone', $event, captcha)
            "
          />
        </a-descriptions-item>
        <a-descriptions-item label="Email">
          <basic-cell
            type="email"
            :text="userInfo.email"
            @change="
              ($event, captcha) => updateUserInfo('email', $event, captcha)
            "
          />
        </a-descriptions-item>
      </a-descriptions>
    </div>
  </div>
</template>

<script>
import BasicCell from "../../components/BasicCell";
import { mapState, mapActions } from "vuex";
export default {
  name: "Basic",
  components: {
    BasicCell,
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "updatePassword" });
  },
  data() {
    return {
      isLoad: false,
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
  },
  methods: {
    ...mapActions("user", {
      updatePhone: "updatePhone",
      updateMail: "updateMail",
      updatePassword: "updatePassword",
    }),
    // 个人数据更改
    updateUserInfo(key, value, captcha) {
      let pattern_phone = /^\d{8,12}$/;
      let pattern_email = /^[\w-]+@[\w-]+(\.[\w-]+)+$/;
      if (key == "phone") {
        if (pattern_phone.test(value)) {
          let formData = new FormData();
          formData.append("phone", value);
          this.$message.loading("Changing data......", 0);
          let result = this.updatePhone(formData);
          result.then((value) => {
            this.$message.success("Change successfully");
          });
        } else {
          this.$message.error("Failed to modify, please check the phone format");
        }
      } else if (key == "email" && captcha.trim() != "") {
        if (pattern_email.test(value)) {
          let formData = new FormData();
          formData.append("email", value);
          formData.append("captcha", captcha);
          this.$message.loading("Changing data......", 0);
          let result = this.updateMail(formData);
          result.then((value) => {
            this.$message.success("Change successfully");
          });
        } else {
          this.$message.error("Failed to modify. Please check the email format");
        }
      }
    },
    // 打开修改密码窗口
    updatePasswordWindow() {
      if (this.$refs.idPasswordWindow.style.height != "300px") {
        this.$refs.idPasswordWindow.style.height = "300px";
        this.$refs.idPasswordWindow.style.padding = "40px";
      } else {
        this.closePasswordWindow();
      }
    },
    // 修改密码窗口失去焦点, 关闭窗口
    passwordWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "password-li") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closePasswordWindow();
    },
    // 关闭修改密码窗口
    closePasswordWindow() {
      this.$refs.idPasswordWindow.style.height = "0px";
      this.$refs.idPasswordWindow.style.padding = "0px";
    },
    // 修改密码
    updatePasswordSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          if (values.newpwd == values.secondaryNewPWD) {
            let formData = new FormData();
            formData.append("originPwd", values.originPwd);
            formData.append("newPwd", values.newpwd);
            this.isLoad = true;
            this.$message.loading("Changing the Password......", 0);
            let result = this.updatePassword(formData);
            result
              .then((value) => {
                this.$message.success("Change successfully");
                this.closePasswordWindow();
                this.isLoad = false;
                if (localStorage.getItem("PersonAccount")) {
                  localStorage.setItem("PersonPassword", values.newpwd);
                }
              })
              .catch((error) => {
                this.isLoad = false;
              });
          } else {
            this.$message.error("Two new passwords are different");
          }
        }
      });
    },
    // 清空修改密码列表
    clear() {
      this.form.setFieldsValue({
        originPwd: undefined,
        newpwd: undefined,
        secondaryNewPWD: undefined,
      });
    },
  },
};
</script>

<style scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
  background: #f5f6fa;
}
#basic-title-layout {
  text-align: left !important;
  position: relative !important;
  height: 55px;
  line-height: 55px;
  padding-left: 25px;
  text-align: center;
  border-bottom: 1px solid #e8e8e8;
  background: #fff;
}
#basic-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#basic-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#password-window {
  text-align: center;
  position: absolute;
  overflow: hidden;
  background: #fff;
  right: 0px;
  top: 55px;
  height: 0px;
  width: 300px;
  transition: 0.3s;
  /* border-bottom-right-radius: 12px; */
  border-bottom-left-radius: 12px;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
  z-index: 99;
}
.icon-btn {
  margin-top: 20px;
  font-size: 18px;
  cursor: pointer;
}
.icon-btn:hover {
  color: #108ee9;
}
#basic-form-button {
  margin-left: 10px;
}
td.column-division {
  border-left: 1px solid #e8e8e8;
}
</style>