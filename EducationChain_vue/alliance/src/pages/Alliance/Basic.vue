<template>
  <div id="page-layout" @click="passwordWindowBlur">
    <div id="basic-title-layout">
      <b>Basic user information</b>
      <ul id="basic-menu-layout">
        <li id="password-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>change Password</span>
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
                      rules: [{ required: true, message: 'Please enter the original account password' }],
                    },
                  ]"
                  type="password"
                  placeholder="Account original password"
                ></a-input>
              </a-form-item>
              <!-- 修改密码-新密码 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'newpwd',
                    {
                      rules: [
                        { required: true, message: 'Please enter a new password' },
                        { min: 8, message: 'Please enter 8~12 digit password' },
                        { max: 12, message: 'Please enter 8~12 digit password' },
                      ],
                    },
                  ]"
                  type="password"
                  placeholder="account new password"
                ></a-input>
              </a-form-item>
              <!-- 修改密码-新密码-二次确认 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'secondaryNewPWD',
                    {
                      rules: [
                        { required: true, message: 'Please enter the second confirmation password' },
                        { min: 8, message: 'Please enter 8~12 digit password' },
                        { max: 12, message: 'Please enter 8~12 digit password' },
                      ],
                    },
                  ]"
                  type="password"
                  placeholder="Second confirmation password"
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
        <a-descriptions-item label="Alliance">
          {{ userInfo.nodeName }}
        </a-descriptions-item>
        <a-descriptions-item label="Alliance Type">
          <template v-if="userInfo.type == 0"> college </template>
          <template v-else> training organization </template>
        </a-descriptions-item>
        <a-descriptions-item label="user type">
          <template v-if="userInfo.role == 'admin'"> administrator </template>
          <template v-else-if="userInfo.role == 'director'">
            director
          </template>
          <template v-else> teacher </template>
        </a-descriptions-item>
        <a-descriptions-item label="Email">
          {{ userInfo.email }}
        </a-descriptions-item>
        <a-descriptions-item label="Phone number">
          <basic-cell
            :text="userInfo.phone"
            @change="($event) => updateUserInfo($event)"
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
      updatePassword: "updatePassword",
    }),
    // 个人数据更改
    updateUserInfo(value) {
      let pattern_phone = /^\d{8,12}$/;
      if (pattern_phone.test(value)) {
        let formData = new FormData();
        formData.append("phone", value);
        this.$message.loading("modifying data......", 0);
        let result = this.updatePhone(formData);
        result.then((value) => {
          this.$message.success("Successfully modified");
        });
      } else {
        this.$message.error("Modification failed, please check the phone format");
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
            this.$message.loading("changing password......", 0);
            let result = this.updatePassword(formData);
            result
              .then((value) => {
                this.$message.success("Successfully changed");
                this.closePasswordWindow();
                this.isLoad = false;
                if (localStorage.getItem("AllianceAccount")) {
                  localStorage.setItem("AlliancePassword", values.newpwd);
                }
              })
              .catch((error) => {
                this.isLoad = false;
              });
          } else {
            this.$message.error("The two new passwords do not match");
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
  z-index: 999;
}
#basic-form-button {
  margin-left: 10px;
}
.icon-btn {
  margin-top: 20px;
  font-size: 18px;
  cursor: pointer;
}
.icon-btn:hover {
  color: #108ee9;
}
</style>