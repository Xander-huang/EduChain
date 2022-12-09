<template>
  <div id="page-layout" @click="achieveWindowBlur">
    <div id="achieve-title-layout">
      <b>Certification of Learning Experience</b>
      <ul id="achieve-menu-layout">
        <li id="achieve-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Add Student Achievement</span>
            </template>
            <a-icon type="plus" class="icon-btn" @click="addAchieveWindow" />
          </a-tooltip>
          <!-- 添加成就-窗口 -->
          <div id="achieve-window" ref="idAchieveWindow">
            <div v-show="studentIdList == null">
              <a-input
                placeholder="Please enter the ID card"
                v-model="personId"
              >
              </a-input>
              <a-button
                type="primary"
                :loading="isLoad"
                @click="handleStudentId"
              >
                Search
              </a-button>
              <a-button
                id="achieve-form-button"
                @click="clearPersonId"
                :loading="isLoad"
              >
                Clear
              </a-button>
            </div>
            <a-form
              v-show="studentIdList != null"
              :form="form"
              layout="horizontal"
              :label-col="{ span: 7, offset: 0 }"
              :wrapper-col="{ span: 17, offset: 0 }"
              @submit="addAchieveSubmit"
              @submit.native.prevent
            >
              <!-- 添加成就-教育经历id -->
              <a-form-item label="Achieve">
                <a-cascader
                  v-decorator="[
                    'eduId',
                    {
                      rules: [
                        { required: true, message: 'Please select the educational experience of the achievement' },
                      ],
                    },
                  ]"
                  :options="studentIdList"
                  placeholder="Please select the educational experience of the achievement"
                />
              </a-form-item>
              <!-- 添加成就-标题 -->
              <a-form-item label="Title">
                <a-input
                  v-decorator="[
                    'title',
                    {
                      rules: [{ required: true, message: 'Please enter a title' }],
                    },
                  ]"
                  placeholder="Please enter the title of the certification experience"
                />
              </a-form-item>
              <!-- 添加成就-获得时间 -->
              <a-form-item label="Date">
                <a-date-picker
                  style="width: 100%"
                  v-decorator="[
                    'acquireTime',
                    {
                      rules: [{ required: true, message: 'Please enter date' }],
                    },
                  ]"
                  placeholder="Please select date"
                  mode="date"
                  format="YYYY-MM-DD"
                />
              </a-form-item>
              <!-- 添加成就-类型 -->
              <a-form-item label="Type">
                <a-select
                  v-decorator="[
                    'type',
                    {
                      rules: [{ required: true, message: 'Please select an experience type' }],
                    },
                  ]"
                  placeholder="Please select an experience type"
                >
                  <a-select-option value="奖项"> awards </a-select-option>
                  <a-select-option value="活动"> activity </a-select-option>
                  <a-select-option value="专利"> patent </a-select-option>
                  <a-select-option value="论文"> paper </a-select-option>
                  <a-select-option value="著作"> book </a-select-option>
                  <a-select-option value="其他"> other </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 添加成就-按钮 -->
              <a-form-item
                :wrapper-col="{ span: 24, offset: 0 }"
                style="overflow: hidden"
              >
                <a-button type="primary" html-type="submit" :loading="isLoad">
                  Add
                </a-button>
                <a-button
                  id="achieve-form-button"
                  @click="clearForm"
                >
                  Clear
                </a-button>
                <a-button
                  id="achieve-form-button"
                  @click="back"
                >
                  Return
                </a-button>
              </a-form-item>
            </a-form>
          </div>
        </li>
      </ul>
    </div>
    <div id="achieve-main-layout">
      <a-table
        :columns="achieveColumns"
        :data-source="achieveList"
        :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
        :rowKey="(record) => record.id"
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
        <!-- 审核状态 -->
        <template slot="auditStatus" slot-scope="text">
          <div v-if="text == 1" style="color: green">Passed</div>
          <div v-else-if="text == 0" style="color: red">Refused</div>
          <div v-else style="color: #1890ff">Review</div>
        </template>
        <!-- 审核建议 -->
        <template slot="feedback" slot-scope="text, record">
          <div v-if="record.auditStatus == 2">
            <a-input
              placeholder="Please enter review comments"
              @change="(e) => changeAchieveText(record.id, e.target.value)"
            >
            </a-input>
          </div>
          <div v-else>
            {{ record.feedback }}
          </div>
        </template>
        <!-- 附件下载 -->
        <template slot="certifyUri" slot-scope="text, record">
          <a-tooltip placement="bottomRight" v-if="text != null">
            <template slot="title">
              <span>Download achievements attachment</span>
            </template>
            <a-icon
              type="download"
              class="icon-btn"
              style="margin-top: 0px"
              @click="downloadFile(record.personId, text)"
            />
          </a-tooltip>
        </template>
        <!-- 操作-通过，未通过 -->
        <template slot="operation" slot-scope="text, record">
          <div>
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Whether it is confirmed to pass?"
              @confirm="
                () =>
                  handleUpdateAchieve(
                    record.id,
                    record.eduId,
                    1,
                    record.feedback
                  )
              "
            >
              <a :disabled="record.auditStatus != 2" style="margin-right: 8px">
                agree
              </a>
            </a-popconfirm>
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Whether to confirm rejection?"
              @confirm="
                () =>
                  handleUpdateAchieve(
                    record.id,
                    record.eduId,
                    0,
                    record.feedback
                  )
              "
            >
              <a :disabled="record.auditStatus != 2"> reject </a>
            </a-popconfirm>
          </div>
        </template>
        <!-- 奖项审核-标题与备注 -->
        <div
          slot="expandedRowRender"
          slot-scope="text"
          style="background: #fff"
        >
          <a-descriptions title="Details"
            ><a-descriptions-item label="title">
              {{ text.title }}
            </a-descriptions-item>
            <a-descriptions-item label="time">
              {{ text.acquireTime }}
            </a-descriptions-item>
            <a-descriptions-item label="type">
              {{ text.type }}
            </a-descriptions-item>
            <a-descriptions-item label="remark">
              {{ text.remark }}
            </a-descriptions-item>
          </a-descriptions>
        </div>
      </a-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
const achieveColumns = [
  {
    title: "ID Card",
    dataIndex: "personId",
    width: "15%",
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
    width: "8%",
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
    filters: [
      {
        text: "Bachelor",
        value: "本科",
      },
      {
        text: "Master ",
        value: "硕士",
      },
      {
        text: "Doctor ",
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
    width: "15%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.majorName.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Status",
    dataIndex: "auditStatus",
    width: "8%",
    ellipsis: true,
    scopedSlots: {
      customRender: "auditStatus",
    },
    filters: [
      {
        text: "Refused",
        value: 0,
      },
      {
        text: "Pass",
        value: 1,
      },
      {
        text: "Review",
        value: 2,
      },
    ],
    filterMultiple: false,
    onFilter: (value, record) => record.auditStatus === value,
  },
  {
    title: "Review comments",
    dataIndex: "feedback",
    width: "25%",
    scopedSlots: { customRender: "feedback" },
  },
  {
    title: "Attachment download",
    dataIndex: "certifyUri",
    className: "achieve-download",
    width: "10%",
    scopedSlots: { customRender: "certifyUri" },
  },
  {
    title: "Operation",
    dataIndex: "operation",
    width: "10%",
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "Achieve",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "addAchieve" });
  },
  mounted() {
    this.$message.loading("Obtaining student experience certification information......", 0);
    let result = this.getStudentAchieve();
    result.then((value) => {
      this.$message.success("Get succeeded!");
    });
  },
  data() {
    return {
      personId: undefined,
      studentIdList: null,
      // flag
      isLoad: false,
      // role
      achieveColumns,
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("achieve", ["achieveList"]),
  },
  methods: {
    ...mapMutations("achieve", {
      CHANGE_ACHIEVE_TEXT: "CHANGE_ACHIEVE_TEXT",
    }),
    ...mapActions("achieve", {
      getStudentAchieve: "getStudentAchieve",
      updateAchieve: "updateAchieve",
      addAchieve: "addAchieve",
      downloadAchieve: "downloadAchieve",
    }),
    ...mapActions("student", {
      getStudentId: "getStudentId",
    }),
    // 下载成就附件
    downloadFile(personId, files) {
      let fileList = files.split(";");
      for (let i = 0; i < fileList.length - 1; i++) {
        let fileType = fileList[i].split(".")[1];
        let result = this.downloadAchieve(fileList[i]);
        result
          .then((file) => {
            let blob = new Blob([file]);
            let downloadElement = document.createElement("a");
            let href = window.URL.createObjectURL(blob); //创建下载的链接
            downloadElement.href = href;
            downloadElement.style.display = "none";
            downloadElement.download = `${personId}(${i + 1}).${fileType}`; //下载后文件名
            document.body.appendChild(downloadElement);
            downloadElement.click(); //点击下载
            document.body.removeChild(downloadElement); //下载完成移除元素
            window.URL.revokeObjectURL(href); //释放掉blob对象
          })
          .catch((error) => {
            this.$message.error(`${fileList[i]}Download failed`);
          });
      }
    },
    // 修改成就状态
    handleUpdateAchieve(id, eduId, auditStatus, feedback) {
      let formData = new FormData();
      formData.append("id", id);
      formData.append("eduId", eduId);
      formData.append("auditStatus", auditStatus);
      if (feedback == null) {
        formData.append("feedback", "无");
      } else {
        formData.append("feedback", feedback);
      }
      this.$message.loading("Modify the student experience certification information......", 0);
      let result = this.updateAchieve({
        id,
        auditStatus,
        formData,
      });
      result.then((value) => {
        this.$message.success("Modification succeeded!");
      });
    },
    // 修改成就审核信息
    changeAchieveText(id, e) {
      this.CHANGE_ACHIEVE_TEXT({ id, e });
    },
    // 打开添加学生窗口
    addAchieveWindow() {
      if (this.studentIdList == null) {
        this.$refs.idAchieveWindow.style.height = "170px";
        this.$refs.idAchieveWindow.style.padding = "40px";
      } else if (this.studentIdList != null) {
        this.$refs.idAchieveWindow.style.height = "370px";
        this.$refs.idAchieveWindow.style.padding = "40px";
      } else {
        this.closeAchieveWindow();
      }
    },
    // 添加学生窗口失去焦点, 关闭窗口
    achieveWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "achieve-li") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closeAchieveWindow();
    },
    // 关闭添加学生窗口
    closeAchieveWindow() {
      this.$refs.idAchieveWindow.style.height = "0px";
      this.$refs.idAchieveWindow.style.padding = "0px";
    },
    // 发送personId获取教育id
    handleStudentId() {
      let pattern_id =
        /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/;
      if (this.personId == undefined) {
        this.$message.error("Please enter your ID card");
      } else if (!pattern_id.test(this.personId)) {
        this.$message.error("Please enter the ID card in the correct format");
      } else {
        this.isLoad = true;
        let result = this.getStudentId(this.personId);

        result
          .then((value) => {
            this.studentIdList = [];
            for (let i = 0; i < value.length; i++) {
              let cacheChildren = {};
              cacheChildren.label = value[i].major;
              cacheChildren.value = value[i].eduId;
              let cacheChildrenList = [];
              cacheChildrenList.push(cacheChildren);
              let cacheEduId = {};
              cacheEduId.label = value[i].type;
              cacheEduId.value = value[i].eduId;
              cacheEduId.children = cacheChildrenList;
              this.studentIdList.push(cacheEduId);
            }
            this.isLoad = false;
            this.$refs.idAchieveWindow.style.height = "370px";
            this.$refs.idAchieveWindow.style.padding = "40px";
          })
          .catch((error) => {
            this.isLoad = false;
          });
      }
    },
    // 清空personId
    clearPersonId() {
      this.personId = undefined;
    },
    // 添加成就
    addAchieveSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        // UI自带检测
        if (!err) {
          let formData = new FormData();
          formData.append("eduId", values.eduId[1]);
          formData.append("title", values.title);
          formData.append("type", values.type);
          formData.append(
            "acquireTime",
            this.$moment(values.acquireTime._d).format("YYYY-MM-DD")
          );
          this.isLoad = true;
          this.$message.loading("Adding achievements......", 0);
          let result = this.addAchieve(formData);
          result
            .then((value) => {
              this.$message.success("Successfully added");
              this.isLoad = false;
              this.personId = undefined;
              this.studentIdList = null;
              this.closeAchieveWindow();
              this.getStudentAchieve();
            })
            .catch((error) => {
              this.isLoad = false;
            });
        }
      });
    },
    // 清空成就表单
    clearForm() {
      this.form.setFieldsValue({
        eduId: undefined,
        title: undefined,
        type: undefined,
        acquireTime: undefined,
      });
    },
    // 返回输入personId处理
    back() {
      this.studentIdList = null;
      this.personId = undefined;
      this.$refs.idAchieveWindow.style.height = "170px";
      this.$refs.idAchieveWindow.style.padding = "40px";
    },
    // 筛选功能
    handleSearch(id, selectedKeys, confirm, dataIndex) {
      confirm();
      this.searchText = selectedKeys[0];
      this.searchedColumn = dataIndex;
    },
    // 重置筛选
    handleReset(clearFilters) {
      clearFilters();
      this.searchText = "";
    },
  },
};
</script>

<style lang="less" scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
}
#achieve-title-layout {
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
#achieve-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#achieve-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#achieve-window {
  text-align: center;
  position: absolute;
  overflow: hidden;
  background: #fff;
  text-align: center;
  right: 0px;
  top: 55px;
  height: 0px;
  width: 350px;
  transition: 0.3s;
  // border-bottom-right-radius: 12px;
  border-bottom-left-radius: 12px;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
  z-index: 99;
}
#achieve-form-button {
  margin-left: 10px;
}
#achieve-main-layout {
  min-width: 1200px;
  height: 645px;
  overflow: auto;
}
/deep/th.achieve-download,
/deep/td.achieve-download {
  text-align: center;
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