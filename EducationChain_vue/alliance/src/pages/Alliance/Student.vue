<template>
  <div id="page-layout" @click="studentWindowBlur">
    <div id="student-title-layout">
      <b>Alliance Student Management</b>
      <ul id="student-menu-layout">
        <li>
          <a-popconfirm
            placement="bottomRight"
            ok-text="Yes"
            title="Whether to select student data in uplink (irrevocable)?"
            @confirm="() => handleUplink()"
          >
            <a-icon slot="icon" type="warning" style="color: red" />
            <a-tooltip placement="bottomRight">
              <template slot="title">
                <span>Student data upChain</span>
              </template>
              <a-icon type="link" class="icon-btn" />
            </a-tooltip>
          </a-popconfirm>
        </li>
        <li>
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Batch add students</span>
            </template>
            <a-upload
              name="file"
              :multiple="false"
              :beforeUpload="fileUploadStudent"
              :showUploadList="false"
            >
              <a-icon type="usergroup-add" class="icon-btn" />
            </a-upload>
          </a-tooltip>
        </li>
        <li id="student-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Add Students</span>
            </template>
            <a-icon
              type="user-add"
              class="icon-btn"
              @click="addStudentWindow"
            />
          </a-tooltip>
          <!-- 添加学生-窗口 -->
          <div id="student-window" ref="idStudentWindow">
            <a-form
              :form="form"
              layout="horizontal"
              @submit="addStudentSubmit"
              @submit.native.prevent
            >
              <!-- 添加学生-学生身份证 -->
              <a-form-item>
                <a-input
                  v-decorator="[
                    'personId',
                    {
                      rules: [
                        { required: true, message: 'Please enter the student ID number number' },
                      ],
                    },
                  ]"
                  placeholder="Student ID"
                ></a-input>
              </a-form-item>
              <!-- 添加学生-教育类型 -->
              <a-form-item>
                <a-select
                  v-decorator="[
                    'eduType',
                    {
                      rules: [{ required: true, message: 'Please select an education type' }],
                    },
                  ]"
                  placeholder="Type of education"
                  @change="addMajorByType"
                >
                  <a-select-option v-if="userInfo.type == 0" value="本科">
                    Bachelor
                  </a-select-option>
                  <a-select-option v-if="userInfo.type == 0" value="硕士">
                    Master
                  </a-select-option>
                  <a-select-option v-if="userInfo.type == 0" value="博士">
                    Doctor
                  </a-select-option>
                  <a-select-option v-if="userInfo.type == 1" value="职业教育">
                    Vocational education
                  </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 添加学生-专业 -->
              <a-form-item>
                <a-select
                  show-search
                  option-filter-prop="children"
                  :filter-option="filterOption"
                  v-decorator="[
                    'majorId',
                    {
                      rules: [
                        { required: true, message: 'select the major ' },
                      ],
                    },
                  ]"
                  placeholder="Major"
                >
                  <a-select-option
                    v-for="major in addMajorList"
                    :key="major.majorId"
                    :value="major.majorId"
                  >
                    {{ major.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 添加学生-按钮 -->
              <a-form-item :wrapper-col="{ span: 24, offset: 0 }">
                <a-button type="primary" html-type="submit" :loading="isLoad">
                  Add
                </a-button>
                <a-button
                  id="student-form-button"
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
    <div id="student-main-layout">
      <a-table
        :columns="studentColumns"
        :data-source="studentList"
        :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
        :rowKey="(record) => record.stuId"
        :row-selection="rowSelection"
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
        <!-- 教育类型 -->
        <!-- <template slot="eduType" slot-scope="text, record">
          <a-select
            v-if="record.editable"
            style="margin: -5px 0; width: 100%"
            :value="text"
            @change="(value) => changeStudent(value, record.stuId, 'eduType')"
          >
            <a-select-option v-if="userInfo.type == 0" value="本科">
              本科
            </a-select-option>
            <a-select-option v-if="userInfo.type == 0" value="硕士">
              硕士
            </a-select-option>
            <a-select-option v-if="userInfo.type == 0" value="博士">
              博士
            </a-select-option>
            <a-select-option v-if="userInfo.type == 1" value="职业教育">
              职业教育
            </a-select-option>
          </a-select>
          <template v-else>
            {{ text }}
          </template>
        </template> -->
        <!-- 专业 -->
        <template slot="majorName" slot-scope="text, record">
          <a-select
            v-if="record.editable"
            style="margin: -5px 0; width: 100%"
            show-search
            option-filter-prop="children"
            :filter-option="filterOption"
            :value="text"
            @change="(value) => changeStudent(value, record.stuId, 'majorName')"
            placeholder="Please select your major"
          >
            <a-select-option
              v-for="major in updateMajorList"
              :key="major.majorId"
              :value="major.name"
            >
              {{ major.name }}
            </a-select-option>
          </a-select>
          <template v-else>
            {{ text }}
          </template>
        </template>
        <!-- 操作-修改与删除 -->
        <template slot="operation" slot-scope="text, record">
          <span v-if="record.editable">
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Are you sure to modify?"
              @confirm="() => save(record.stuId)"
            >
              <a style="margin-right: 8px">Preserve</a>
            </a-popconfirm>
            <a @click="() => cancel(record.stuId)">Cancel</a>
          </span>
          <span v-else>
            <a
              style="margin-right: 8px"
              :disabled="editingKey != '' || record.uplinked == 1"
              @click="() => edit(record.stuId)"
            >
              Change
            </a>
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Are you sure to delete?"
              @confirm="() => handleDelStudent(record.stuId)"
            >
              <a-icon slot="icon" type="warning" style="color: red" />
              <a
                style="margin-right: 8px"
                :disabled="editingKey != '' || record.uplinked == 1"
              >
              delete
              </a>
            </a-popconfirm>

            <a-upload
              name="file"
              :multiple="false"
              :beforeUpload="
                (file) =>
                  fileUploadCertificate(file, record.stuId, record.stuId)
              "
              :showUploadList="false"
            >
              <a :disabled="editingKey != '' || record.uplinked == 1">
                Upload graduation certificate
              </a>
            </a-upload>
          </span>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
const studentColumns = [
  {
    title: "ID Card",
    dataIndex: "personId",
    width: "20%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.personId.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Name",
    dataIndex: "name",
    width: "15%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.name.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Education Type",
    dataIndex: "eduType",
    width: "15%",
    ellipsis: true,
    // scopedSlots: {
    //   customRender: "eduType",
    // },
    filters: [
      {
        text: "Bachelor",
        value: "本科",
      },
      {
        text: "Master",
        value: "硕士",
      },
      {
        text: "Doctor",
        value: "博士",
      },
      {
        text: "Vocational education",
        value: "职业教育",
      },
    ],
    filterMultiple: false,
    onFilter: (value, record) => record.eduType === value,
  },
  {
    title: "Major",
    dataIndex: "majorName",
    width: "30%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "majorName",
    },
    onFilter: (value, record) =>
      record.majorName.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Operation",
    width: "20%",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "Student",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "addStudent" });
  },
  mounted() {
    this.$message.loading("Geting the league students......", 0);
    let result = this.getStudent();
    result.then((value) => {
      if (value != "ok") {
        this.$message.info("The institution has not registered students");
      } else {
        this.$message.success("Get succeeded")
        this.cacheData = this.studentList.map((item) => ({ ...item }));
      }
    });
    // this.cacheData = this.studentList.map((item) => ({ ...item }));
  },
  data() {
    return {
      selectedRowKeys: [],
      addMajorList: undefined,
      updateMajorList: undefined,
      updateMajorId: "",
      // flag
      isLoad: false,
      editingKey: "",
      // rule
      studentColumns,
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    ...mapState("student", ["studentList"]),
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedRowKeys = selectedRowKeys;
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: record.uplinked == 1,
          },
        }),
      };
    },
  },
  methods: {
    ...mapMutations("student", {
      STUDENT_INFO: "STUDENT_INFO",
      GET_EDITABLE: "GET_EDITABLE",
      DEL_EDITABLE: "DEL_EDITABLE",
    }),
    ...mapActions("student", {
      getStudent: "getStudent",
      addStudent: "addStudent",
      delStudent: "delStudent",
      updateStudent: "updateStudent",
      addStudents: "addStudents",
      addCertificate: "addCertificate",
      uplinkStudent: "uplinkStudent",
    }),
    ...mapActions("major", {
      getMajorByType: "getMajorByType",
    }),
    // 打开添加学生窗口
    addStudentWindow() {
      if (this.$refs.idStudentWindow.style.height != "300px") {
        this.$refs.idStudentWindow.style.height = "300px";
        this.$refs.idStudentWindow.style.padding = "40px";
      } else {
        this.closeStudentWindow();
      }
    },
    // 添加学生窗口失去焦点, 关闭窗口
    studentWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "student-li") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closeStudentWindow();
    },
    // 关闭添加学生窗口
    closeStudentWindow() {
      this.$refs.idStudentWindow.style.height = "0px";
      this.$refs.idStudentWindow.style.padding = "0px";
    },
    // 根据所选教育类型返回专业列表
    addMajorByType(majorType) {
      let type = null;
      if (majorType == "本科") {
        type = 0;
      } else if (majorType == "硕士") {
        type = 1;
      } else if (majorType == "博士") {
        type = 2;
      } else if (majorType == "职业教育") {
        type = 3;
      }
      let result = this.getMajorByType(type);
      result.then((value) => {
        this.addMajorList = value;
      });
    },
    // 查询专业列表
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
    // 添加学生
    addStudentSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          let formData = new FormData();
          formData.append("personId", values.personId);
          formData.append("majorId", values.majorId);
          this.isLoad = true;
          this.$message.loading("Adding students......", 0);
          let result = this.addStudent(formData);
          result
            .then((value) => {
              this.$message.success("Successfully added");
              let result = this.getStudent();
              result.then((value) => {
                if (value != "ok") {
                  this.$message.info("The institution has not registered students");
                } else {
                  this.cacheData = this.studentList.map((item) => ({
                    ...item,
                  }));
                }
              });
              this.clear();
              this.closeStudentWindow();
              this.isLoad = false;
            })
            .catch((error) => {
              this.isLoad = false;
            });
        }
      });
    },
    // 清空添加学生列表
    clear() {
      this.form.setFieldsValue({
        personId: undefined,
        eduType: undefined,
        majorId: undefined,
      });
      this.addMajorList = undefined;
    },
    // 删除学生
    handleDelStudent(stuId) {
      let formData = new FormData();
      formData.append("stuId", stuId);
      this.$message.loading("Deleting students......");
      let result = this.delStudent(formData);
      result.then((value) => {
        this.$message.success("Deletion succeeded");
      });
    },
    // 修改学生数据，本地
    changeStudent(value, key, column) {
      console.log(value, key, column);
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      if (column == "majorName") {
        this.$nextTick(() => {
          this.updateMajorId = this.updateMajorList.filter(
            (item) => target.majorName == item.name
          )[0].majorId;
        });
      } else if (column == "eduType" && target.eduType != value) {
        let type = null;
        if (value == "本科") {
          type = 0;
        } else if (value == "硕士") {
          type = 1;
        } else if (value == "博士") {
          type = 2;
        } else if (value == "职业教育") {
          type = 3;
        }
        let result = this.getMajorByType(type);
        result.then((value) => {
          this.updateMajorList = value;
          this.updateMajorId = "";
          target.majorName = undefined;
        });
      }
      if (target) {
        target[column] = value;
        this.STUDENT_INFO(newData);
      }
    },
    // 编辑学生数据
    edit(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.STUDENT_INFO(newData);
      }
      let type = null;
      if (target.eduType == "本科") {
        type = 0;
      } else if (target.eduType == "硕士") {
        type = 1;
      } else if (target.eduType == "博士") {
        type = 2;
      } else if (target.eduType == "职业教育") {
        type = 3;
      }
      let result = this.getMajorByType(type);
      result.then((value) => {
        this.updateMajorList = value;
        this.updateMajorId = this.updateMajorList.filter(
          (item) => target.majorName == item.name
        )[0].majorId;
      });
    },
    // 修改学生信息，上传
    save(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      if (target.majorName != undefined) {
        let formData = new FormData();
        formData.append("stuId", target.stuId);
        formData.append("eduType", target.eduType);
        formData.append("majorId", this.updateMajorId);
        // 发送更改数据
        this.$message.loading("Editing student information......", 0);
        let result = this.updateStudent(formData);
        result.then((value) => {
          const newCacheData = [...this.cacheData];
          const targetCache = newCacheData.filter(
            (item) => key === item.stuId
          )[0];
          this.STUDENT_INFO(newData);
          this.DEL_EDITABLE(key);
          Object.assign(targetCache, target);
          this.cacheData = newCacheData;
          this.editingKey = "";
          this.updateMajorList = undefined;
          this.updateMajorId = "";
          this.$message.success("Modification succeeded!");
        });
      } else {
        this.$message.error("Please select a major to modify");
      }
    },
    // 取消编辑
    cancel(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      this.editingKey = "";
      this.updateMajorList = undefined;
      this.updateMajorId = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.stuId)[0]
        );
        this.STUDENT_INFO(newData);
        this.DEL_EDITABLE(key);
      }
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
    // 批量添加学生文件-上传前回调
    fileUploadStudent(file) {
      const isSHEET =
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
        file.type === "application/vnd.ms-excel";
      if (!isSHEET) {
        this.$message.error("You can only upload xlsx or csv files");
      } else {
        let formData = new FormData();
        formData.append("file", file);
        this.$message.loading("Adding students......");
        let result = this.addStudents(formData);
        result.then((value) => {
          this.$message.success("Successfully added");
          this.getStudent();
        });
      }
      return false;
    },
    // 添加学生毕业证书文件-上传前回调
    fileUploadCertificate(file, id, eduId) {
      let formData = new FormData();
      formData.append("eduId", eduId);
      formData.append("id", id);
      formData.append("file", file);
      this.$message.loading("Uploading certificate......");
      let result = this.addCertificate(formData);
      result.then((value) => {
        this.$message.success("Upload succeeded");
      });
      return false;
    },
    // 学生数据上链
    handleUplink() {
      if (this.selectedRowKeys.length != 0) {
        let formData = new FormData();
        formData.append("studentList", this.selectedRowKeys);
        this.$message.loading("Data on the chain......");
        let result = this.uplinkStudent(formData);
        result.then((value) => {
          this.$message.success("Succeeded in linking");
        });
      } else {
        this.$message.error("Please select an online student")
      }
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
#student-title-layout {
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
#student-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#student-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#student-window {
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
#student-form-button {
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
#student-main-layout {
  min-width: 1200px;
  height: 645px;
}
</style>