<template>
  <div id="page-layout" @click="certWindowBlur">
    <div id="achieve-title-layout">
      <b>Certification of Learning Experience</b>
      <ul id="achieve-menu-layout">
        <li id="cert-li">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Add achievement request</span>
            </template>
            <a-icon type="plus" class="icon-btn" @click="updateCertWindow" />
          </a-tooltip>
          <!-- 添加成就-窗口 -->
          <div id="cert-window" ref="idCertWindow">
            <a-form
              id="cert-form-layout"
              :form="form"
              layout="horizontal"
              :label-col="{ span: 6, offset: 1 }"
              :wrapper-col="{ span: 15 }"
              @submit="formSubmit"
              @submit.native.prevent
            >
              <!-- 标题 -->
              <a-form-item label="Title">
                <a-input
                  v-decorator="[
                    'title',
                    {
                      rules: [{ required: true, message: 'Please enter a title' }],
                    },
                  ]"
                  placeholder="Please enter the title of certification experience"
                />
              </a-form-item>
              <!-- 所属机构 -->
              <a-form-item label="Alliance">
                <a-select
                  v-decorator="[
                    'eduId',
                    {
                      rules: [{ required: true, message: 'Please select your Alliance' }],
                    },
                  ]"
                  placeholder="Please select your Alliance"
                >
                  <a-select-option
                    v-for="alliance in allianceList"
                    :key="alliance.eduId"
                    :value="alliance.eduId"
                  >
                    {{ alliance.nodeName }} / {{ alliance.type }} /
                    {{ alliance.majorName }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 经历类型 -->
              <a-form-item label="ExperienceType">
                <a-select
                  v-decorator="[
                    'type',
                    {
                      rules: [{ required: true, message: 'Please select your ExperienceType' }],
                    },
                  ]"
                  placeholder="Please select your ExperienceType"
                >
                  <a-select-option value="奖项"> awards </a-select-option>
                  <a-select-option value="活动"> activity </a-select-option>
                  <a-select-option value="专利"> patent </a-select-option>
                  <a-select-option value="论文"> paper </a-select-option>
                  <a-select-option value="著作"> book </a-select-option>
                  <a-select-option value="其他"> other </a-select-option>
                </a-select>
              </a-form-item>
              <!-- 时间 -->
              <a-form-item label="Date">
                <a-date-picker
                  style="width: 100%"
                  v-decorator="[
                    'acquireTime',
                    {
                      rules: [{ required: true, message: 'Please enter your AcquireTime' }],
                    },
                  ]"
                  placeholder="Please select your AcquireTime"
                  mode="date"
                  format="YYYY-MM-DD"
                />
              </a-form-item>
              <!-- 备注 -->
              <a-form-item label="Remark">
                <a-textarea
                  v-decorator="[
                    'remark',
                    {
                      rules: [{ required: false }],
                    },
                  ]"
                  placeholder="Please describe the learning experience to be certified"
                  :auto-size="{ minRows: 3, maxRows: 4 }"
                />
              </a-form-item>
              <!-- 提交功能 清空功能 -->
              <a-form-item :wrapper-col="{ span: 13, offset: mobile ? 0 : 6 }" style="overflow: hidden">
                <a-button type="primary" html-type="submit" :loading="isLoad">
                  Submit
                </a-button>
                <a-button id="cert-clear" @click="clear">
                  Clear
                </a-button>
              </a-form-item>
              <!-- 附件文件 -->
              <a-form-item :wrapper-col="{ span: 9, offset: mobile ? 0 : 7 }">
                <a-upload
                  v-decorator="[
                    'files',
                    {
                      rules: [{ required: false }],
                    },
                  ]"
                  :file-list="fileList"
                  :multiple="true"
                  :beforeUpload="beforeFileUpload"
                  :remove="fileRemove"
                  :disabled="isLoad"
                >
                  <a-button>
                    <a-icon type="upload" />
                    Upload Attachment
                  </a-button>
                </a-upload>
                <!-- :beforeUpload="beforeFileUpload"           // 上传前的回调，作用是判断文件格式、大小等等
                     :fileList="fileList"                       // 初始文件列表，用于回显
                     :headers="headers"                         // 上传请求的请求头，存着authorization值
                     :multiple="true"                           // 支持多选上传
                     :remove="fileRemove"                       // 上传列表删除回调，文件的删除需要自己实现
                     @change="handleChange"                     // 上传的回调，分为三种
                     action="/api/storage/upload"               // 对应后台api
                -->
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
        :loading="!achieveList"
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
        <template slot="auditStatus" slot-scope="text">
          <div v-if="text == 1" style="color: green">Passed</div>
          <div v-else-if="text == 0" style="color: red">Refused</div>
          <div v-else style="color: #1890ff">Review</div>
        </template>
        <!-- 附件下载 -->
        <template slot="certifyUri" slot-scope="text, record">
          <a-tooltip placement="bottomRight" v-if="text != null">
            <template slot="title">
              <span>Download achievement attachment</span>
            </template>
            <a-icon
              type="download"
              class="icon-btn"
              style="margin-top: 0px"
              @click="downloadFile(record.id, text)"
            />
          </a-tooltip>
        </template>
        <p slot="expandedRowRender" slot-scope="text" style="margin: 0">
          <b>Audit opinion: </b> {{ text.feedback }}
        </p>
      </a-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const achieveColumns = [
  {
    title: "Title",
    dataIndex: "title",
    width: "30%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.data.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Application time",
    dataIndex: "createTime",
    width: "20%",
    ellipsis: true,
  },
  {
    title: "Application type",
    dataIndex: "type",
    width: "20%",
    ellipsis: true,
    filters: [
      {
        text: "awards",
        value: "奖项",
      },
      {
        text: "activity",
        value: "活动",
      },
      {
        text: "patent",
        value: "专利",
      },
      {
        text: "paper",
        value: "论文",
      },
      {
        text: "book",
        value: "著作",
      },
      {
        text: "other",
        value: "其他",
      },
    ],
    filterMultiple: false,
    onFilter: (value, record) => record.type === value,
  },
  {
    title: "Adudit Status",
    dataIndex: "auditStatus",
    width: "20%",
    scopedSlots: { customRender: "auditStatus" },
    ellipsis: true,
    filters: [
      {
        text: "Refused",
        value: 0,
      },
      {
        text: "Passed",
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
    title: "Download Attachment",
    dataIndex: "certifyUri",
    className: "achieve-download",
    width: "20%",
    scopedSlots: { customRender: "certifyUri" },
  },
];
export default {
  name: "Achieve",
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "certificationForm" });
  },
  mounted() {
    this.$message.loading("Acquiring achievement experience......", 0);
    let resultAchieve = this.getAchieve();
    resultAchieve.then((value) => {
      if (value != "ok") {
        this.$message.info("You do not have an education record!");
      }
    });
    let resultAlliance = this.getAlliance();
    resultAlliance.then((value) => {
      if (value != "ok") {
        this.$message.info("You have not participated in institutional studies!");
      }
    });
  },
  data() {
    return {
      achieveColumns,
      isLoad: false,
      fileList: [],
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("achieve", ["achieveList", "allianceList"]),
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    ...mapActions("achieve", {
      getAchieve: "getAchieve",
      certificationForm: "certificationForm",
      getAlliance: "getAlliance",
    }),
    ...mapActions("education", {
      getDownload: "getDownload",
    }),
    // 打开成就窗口
    updateCertWindow() {
      if (this.$refs.idCertWindow.style.height != "550px") {
        this.$refs.idCertWindow.style.height = "550px";
        this.$refs.idCertWindow.style.padding = "10px";
      } else {
        this.closeCertWindow();
      }
    },
    // 成就窗口失去焦点, 关闭窗口
    certWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "cert-li") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closeCertWindow();
    },
    // 关闭成就窗口
    closeCertWindow() {
      this.$refs.idCertWindow.style.height = "0px";
      this.$refs.idCertWindow.style.padding = "0px";
    },
    // 发送成就表单
    formSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        // UI自带检测
        if (!err) {
          let formData = new FormData();
          formData.append("eduId", values.eduId);
          formData.append("title", values.title);
          formData.append("type", values.type);
          formData.append(
            "acquireTime",
            this.$moment(values.acquireTime._d).format("YYYY-MM-DD")
          );
          if (values.remark == undefined) {
            formData.append("remark", "Null");
          } else {
            formData.append("remark", values.remark);
          }
          const { fileList } = this;
          fileList.forEach((file) => {
            formData.append("files", file);
          });
          this.isLoad = true;
          this.$message.loading("Sending......", 0);
          let result = this.certificationForm(formData);
          result
            .then((value) => {
              this.$message.success("Send successfully");
              this.isLoad = false;
              this.getAchieve();
              this.closeCertWindow();
              this.clear();
            })
            .catch((error) => {
              this.isLoad = false;
            });
        }
      });
    },
    // 清空表单
    clear() {
      this.form.setFieldsValue({
        eduId: undefined,
        title: undefined,
        type: undefined,
        acquireTime: undefined,
        remark: undefined,
        files: undefined,
      });
      this.fileList = [];
    },
    // 文件-上传回调  *目前无用
    handleChange(info) {
      const file = info.file;
      console.log("handleChange", info.file);
      const status = info.file.status;
      if (status === "done") {
        // 完成
        this.$message.success("Image uploaded successfully...");
        const res = file.response.data; // 上传后台返回的结果
        const item = {
          uid: res.newFileName,
          name: res.filename,
          status: "done",
          url: res.url,
        };
        this.fileList.push(item);
      } else if (status === "error") {
        // 错误
        this.$message.error(`${info.file.name} File upload failed`);
      }
    },
    // 文件-删除回调
    fileRemove(file) {
      const index = this.fileList.indexOf(file);
      const newFileList = this.fileList.slice();
      newFileList.splice(index, 1);
      this.fileList = newFileList;
    },
    // 文件-上传前回调
    beforeFileUpload(file) {
      // const isIMG = file.type === "image/jpeg" || file.type === "image/png";
      // if (!isIMG) {
      //   this.$message.error("您只能上传jpg或png文件");
      // }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("The file size cannot be greater than 2MB");
      } else {
        this.fileList = [...this.fileList, file];
      }
      return false;
    },
    // 筛选成就功能
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
    // 下载成就附件
    downloadFile(id, files) {
      let fileList = files.split(";");
      for (let i = 0; i < fileList.length - 1; i++) {
        let fileType = fileList[i].split(".")[1];
        let result = this.getDownload(fileList[i]);
        result
          .then((file) => {
            let blob = new Blob([file]);
            let downloadElement = document.createElement("a");
            let href = window.URL.createObjectURL(blob); //创建下载的链接
            downloadElement.href = href;
            downloadElement.style.display = "none";
            downloadElement.download = `${id}(${i + 1}).${fileType}`; //下载后文件名
            document.body.appendChild(downloadElement);
            downloadElement.click(); //点击下载
            document.body.removeChild(downloadElement); //下载完成移除元素
            window.URL.revokeObjectURL(href); //释放掉blob对象
          })
          .catch((error) => {
            this.$message.error(`${fileList[i]}download failed`);
          });
      }
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
#achieve-main-layout {
  min-width: 1200px;
  height: 645px;
  overflow: auto;
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
#cert-window {
  text-align: center;
  position: absolute;
  overflow: auto;
  background: #fff;
  text-align: center;
  right: 0px;
  top: 55px;
  height: 0px;
  width: 750px;
  transition: 0.3s;
  // border-bottom-right-radius: 12px;
  border-bottom-left-radius: 12px;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
  z-index: 99;
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
#cert-form-layout {
  margin: 10px auto;
}
#cert-clear {
  margin-left: 10px;
}
</style>