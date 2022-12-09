<template>
  <div id="page-layout" @click="managerWindowBlur">
    <div id="manager-title-layout">
      <b>Alliance user management</b>
      <ul id="manager-menu-layout">
        <!-- <li>
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>批量添加用户</span>
            </template>
            <a-upload
              name="file"
              :multiple="false"
              :beforeUpload="fileUpload"
              :showUploadList="false"
            >
              <a-icon type="usergroup-add" class="icon-btn" />
            </a-upload>
          </a-tooltip>
        </li> -->
        <li id="manager-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Add User</span>
            </template>
            <a-icon
              type="user-add"
              class="icon-btn"
              @click="addManagerWindow"
            />
          </a-tooltip>
          <!-- 添加用户-窗口 -->
          <div id="manager-window" ref="idManagerWindow">
            <a-form
              :form="form"
              layout="horizontal"
              :label-col="{ span: 7, offset: 0 }"
              :wrapper-col="{ span: 17, offset: 0 }"
              @submit="handleAddManagerSubmit"
              @submit.native.prevent
            >
              <!-- 添加用户-姓名 -->
              <a-form-item label="Name">
                <a-input
                  v-decorator="[
                    'name',
                    {
                      rules: [{ required: true, message: 'please enter your name' }],
                    },
                  ]"
                  placeholder="name"
                ></a-input>
              </a-form-item>
              <!-- 添加用户-邮箱 -->
              <a-form-item label="Email">
                <a-input
                  v-decorator="[
                    'email',
                    {
                      rules: [
                        { required: true, message: 'Please enter  email' },
                        {
                          pattern: /^[\w-]+@[\w-]+(\.[\w-]+)+$/,
                          message: 'Please enter correct email',
                        },
                      ],
                    },
                  ]"
                  placeholder="email"
                >
                </a-input>
              </a-form-item>
              <!-- 添加用户-电话 -->
              <a-form-item label="Phone">
                <a-input
                  v-decorator="[
                    'phone',
                    {
                      rules: [
                        { required: true, message: 'Please enter the telephone number' },
                        {
                          pattern: /^\d{11}$/,
                          message: 'Please enter correct telephone number',
                        },
                      ],
                    },
                  ]"
                  placeholder="phone"
                >
                </a-input>
              </a-form-item>
              <!-- 添加用户-类型 -->
              <a-form-item label="Role">
                <a-select
                  v-decorator="[
                    'role',
                    {
                      rules: [{ required: true, message: 'please select your role' }],
                    },
                  ]"
                  placeholder="role"
                >
                  <a-select-option value="teacher"> teacher </a-select-option>
                  <a-select-option value="director"> director </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 添加用户-按钮 -->
              <a-form-item :wrapper-col="{ span: 24, offset: 0 }">
                <a-button type="primary" html-type="submit" :loading="isLoad">
                  Add
                </a-button>
                <a-button
                  id="manager-form-button"
                  @click="clear"
                  :loading="isLoad"
                >
                  Clear
                </a-button>
              </a-form-item>
            </a-form>
          </div>
        </li>
      </ul>
    </div>
    <div id="manager-main-layout">
      <a-table
        :columns="managerColumns"
        :data-source="managerList"
        :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
        :rowKey="(record) => record.account"
      >
        <!-- 筛选功能 -->
        <div
          slot="filterDropdown"
          slot-scope="{
            setSelectedKeys,
            selectedKeys,
            confirm,
            clearFilters,
            column,
          }"
          style="padding: 8px"
        >
          <a-input
            :placeholder="`Search ${column.title}`"
            :value="selectedKeys[0]"
            style="width: 188px; margin-bottom: 8px; display: block"
            @change="
              (e) => setSelectedKeys(e.target.value ? [e.target.value] : [])
            "
            @pressEnter="
              () => handleSearch(selectedKeys, confirm, column.dataIndex)
            "
          />
          <a-button
            type="primary"
            icon="search"
            size="small"
            style="width: 90px; margin-right: 8px"
            @click="() => handleSearch(selectedKeys, confirm, column.dataIndex)"
          >
            Search
          </a-button>
          <a-button
            size="small"
            style="width: 90px"
            @click="() => handleReset(clearFilters)"
          >
            Reset
          </a-button>
        </div>
        <!-- 名称-显示与修改 -->
        <template slot="name" slot-scope="text, record">
          <a-input
            v-if="record.editable"
            style="margin: -5px 0; width: 100%"
            :value="text"
            @change="
              (e) => changeManager(e.target.value, record.account, 'name')
            "
          />
          <template v-else>
            {{ text }}
          </template>
        </template>
        <!-- 邮箱-显示与修改 -->
        <template slot="email" slot-scope="text, record">
          <a-input
            v-if="record.editable"
            style="margin: -5px 0; width: 100%"
            :value="text"
            @change="
              (e) => changeManager(e.target.value, record.account, 'email')
            "
          />
          <template v-else>
            {{ text }}
          </template>
        </template>
        <!-- 电话-显示与修改 -->
        <template slot="phone" slot-scope="text, record">
          <a-input
            v-if="record.editable"
            style="margin: -5px 0; width: 100%"
            :value="text"
            @change="
              (e) => changeManager(e.target.value, record.account, 'phone')
            "
          />
          <template v-else>
            {{ text }}
          </template>
        </template>
        <!-- 类型-显示与修改 -->
        <template slot="role" slot-scope="text, record">
          <a-select
            v-if="record.editable && record.role != 'admin'"
            style="margin: -5px 0; width: 100%"
            :value="text"
            @change="(value) => changeManager(value, record.account, 'role')"
          >
            <a-select-option value="teacher"> teacher </a-select-option>
            <a-select-option value="director"> director </a-select-option>
          </a-select>
          <template v-else>
            <template v-if="text == 'admin'"> administrator </template>
            <template v-else-if="text == 'director'"> director </template>
            <template v-else> teacher </template>
          </template>
        </template>
        <!-- 操作-修改与删除 -->
        <template slot="operation" slot-scope="text, record">
          <span v-if="record.editable">
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="是否确认修改?"
              @confirm="() => save(record.account)"
            >
              <a style="margin-right: 8px">保存</a>
            </a-popconfirm>
            <a @click="() => cancel(record.account)">取消</a>
          </span>
          <span v-else>
            <!-- 编辑 -->
            <a
              style="margin-right: 8px"
              :disabled="editingKey !== ''"
              @click="() => edit(record.account)"
            >
              修改
            </a>
            <!-- 重置密码 -->
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="是否重置密码?"
              @confirm="() => handleResetPwd(record.account)"
            >
              <a style="margin-right: 8px" :disabled="editingKey !== ''">
                重置密码
              </a>
            </a-popconfirm>
            <!-- 删除 -->
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="是否确认删除?"
              @confirm="() => handleDelete(record.account)"
            >
              <a-icon slot="icon" type="warning" style="color: red" />
              <a :disabled="record.role == 'admin' || editingKey !== ''">
                删除
              </a>
            </a-popconfirm>
          </span>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
const managerColumns = [
  {
    title: "Account",
    dataIndex: "account",
    width: "15%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.account.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Name",
    dataIndex: "name",
    width: "10%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "name",
    },
    onFilter: (value, record) =>
      record.name.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Email",
    dataIndex: "email",
    width: "20%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "email",
    },
    onFilter: (value, record) =>
      record.email.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Phone",
    dataIndex: "phone",
    width: "15%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "phone",
    },
    onFilter: (value, record) =>
      record.phone.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Role",
    dataIndex: "role",
    width: "15%",
    ellipsis: true,
    scopedSlots: {
      customRender: "role",
    },
    filters: [
      {
        text: "teacher",
        value: "教师",
      },
      {
        text: "director",
        value: "教导主任",
      },
      {
        text: "admin",
        value: "管理员",
      },
    ],
    filterMultiple: false,
    onFilter: (value, record) => record.role === value,
  },
  {
    title: "Operation",
    width: "",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "Manager",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "addManager" });
  },
  mounted() {
    this.$message.loading("获取联盟用户信息中......", 0);
    let result = this.getManager();
    result.then((value) => {
      this.cacheData = this.managerList.map((item) => ({ ...item }));
      this.$message.success("获取成功!");
    });
    // this.cacheData = this.managerList.map((item) => ({ ...item }));
  },
  data() {
    return {
      // flag
      isLoad: false,
      editingKey: "",
      // rule
      managerColumns,
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("manager", ["managerList"]),
  },
  methods: {
    ...mapMutations("manager", {
      MANAGER_INFO: "MANAGER_INFO",
      GET_EDITABLE: "GET_EDITABLE",
      DEL_EDITABLE: "DEL_EDITABLE",
    }),
    ...mapActions("manager", {
      getManager: "getManager",
      addManager: "addManager",
      delManager: "delManager",
      updateManager: "updateManager",
      addManagers: "addManagers",
      resetManager: "resetManager",
    }),
    // 打开添加用户窗口
    addManagerWindow() {
      if (this.$refs.idManagerWindow.style.height != "380px") {
        this.$refs.idManagerWindow.style.height = "380px";
        this.$refs.idManagerWindow.style.padding = "40px";
      } else {
        this.closeManagerWindow();
      }
    },
    // 添加用户窗口失去焦点, 关闭窗口
    managerWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "manager-li") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closeManagerWindow();
    },
    // 关闭添加用户窗口
    closeManagerWindow() {
      this.$refs.idManagerWindow.style.height = "0px";
      this.$refs.idManagerWindow.style.padding = "0px";
    },
    // 添加用户
    handleAddManagerSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          let formData = new FormData();
          formData.append("name", values.name);
          formData.append("email", values.email);
          formData.append("phone", values.phone);
          formData.append("role", values.role);
          this.isLoad = true;
          this.$message.loading("Adding user......", 0);
          let result = this.addManager(formData);
          result
            .then((value) => {
              this.$message.success("add successful");
              this.getManager();
              result.then((value) => {
                this.cacheData = this.managerList.map((item) => ({ ...item }));
              });
              this.clear();
              this.closeManagerWindow();
              this.isLoad = false;
            })
            .catch((error) => {
              this.isLoad = false;
            });
        }
      });
    },
    // 清空添加用户列表
    clear() {
      this.form.setFieldsValue({
        name: undefined,
        email: undefined,
        phone: undefined,
        role: undefined,
      });
    },
    // 修改用户信息，本地
    changeManager(value, key, column) {
      const newData = [...this.managerList];
      const target = newData.filter((item) => key === item.account)[0];
      if (target) {
        target[column] = value;
        this.MANAGER_INFO(newData);
      }
    },
    // 编辑用户数据
    edit(key) {
      const newData = [...this.managerList];
      const target = newData.filter((item) => key === item.account)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.MANAGER_INFO(newData);
      }
    },
    // 修改用户信息，上传
    save(key) {
      const newData = [...this.managerList];
      const target = newData.filter((item) => key === item.account)[0];
      let pattern_phone = /^\d{11}$/;
      let pattern_email = /^[\w-]+@[\w-]+(\.[\w-]+)+$/;
      if (
        pattern_email.test(target.email) &&
        pattern_phone.test(target.phone)
      ) {
        let formData = new FormData();
        formData.append("account", target.account);
        formData.append("name", target.name);
        formData.append("email", target.email);
        formData.append("phone", target.phone);
        formData.append("role", target.role);
        this.$message.loading("Modifying user information......", 0);
        let result = this.updateManager(formData);
        result.then((value) => {
          const newCacheData = [...this.cacheData];
          const targetCache = newCacheData.filter(
            (item) => key === item.account
          )[0];
          this.MANAGER_INFO(newData);
          this.DEL_EDITABLE(key);
          Object.assign(targetCache, target);
          this.cacheData = newCacheData;
          this.editingKey = "";
          this.$message.success("Change successful!");
        });
      } else {
        this.$message.error("Incorrect data format!");
      }
    },
    // 取消操作
    cancel(key) {
      const newData = [...this.managerList];
      const target = newData.filter((item) => key === item.account)[0];
      this.editingKey = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.account)[0]
        );
        this.MANAGER_INFO(newData);
        this.DEL_EDITABLE(key);
      }
    },
    // 重置密码
    handleResetPwd(key) {
      let formData = new FormData();
      formData.append("account", key);
      let result = this.resetManager(formData);
      result.then((value) => {
        this.$message.success("Reset succeeded!");
      });
    },
    // 删除用户
    handleDelete(key) {
      let formData = new FormData();
      formData.append("account", key);
      let result = this.delManager(formData);
      result.then((value) => {
        this.$message.success("Deletion succeeded!");
        this.getManager();
      });
    },
    // 筛选学生功能
    handleSearch(selectedKeys, confirm, dataIndex) {
      confirm();
      this.searchText = selectedKeys[0];
      this.searchedColumn = dataIndex;
    },
    // 重置筛选
    handleReset(clearFilters) {
      clearFilters();
      this.searchText = "";
    },
    // 文件-上传前回调
    fileUpload(file) {
      const isSHEET =
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
        file.type === "application/vnd.ms-excel";
      if (!isSHEET) {
        this.$message.error("You can only upload xlsx or csv files");
      } else {
        let formData = new FormData();
        formData.append("file", file);
        this.$message.loading("Adding Users......");
        let result = this.addManagers(formData);
        result.then((value) => {
          this.$message.success("Successfully added");
          this.getManager();
        });
      }
      return false;
    },
  },
};
</script>

<style scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
}
#manager-title-layout {
  text-align: left !important;
  position: relative !important;
  min-width: 1200px;
  height: 55px;
  line-height: 55px;
  padding-left: 25px;
  text-align: center;
  border-bottom: 1px solid #e8e8e8;
  background: #fafafa;
}
#manager-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#manager-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#manager-window {
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
#manager-form-button {
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
#manager-main-layout {
  height: 645px;
  min-width: 1200px;
  overflow: auto;
}
</style>