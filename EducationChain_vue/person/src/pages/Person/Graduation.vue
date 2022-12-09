<template>
  <div id="page-layout" @click="eduWindowBlur">
    <div id="graduation-title-layout">
      <b>Education UpChain Information</b>
      <ul id="graduation-menu-layout">
        <li>
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Generate personal data authentication code</span>
            </template>
            <a-icon type="link" class="icon-btn" @click="eduWindow()" />
          </a-tooltip>
        </li>
        <li>
          <a-popconfirm
            placement="bottomRight"
            ok-text="Yes"
            title="Whether to download personal data reports?"
            @confirm="() => pdfDowdload()"
          >
            <a-tooltip placement="bottomRight">
              <template slot="title">
                <span>Generate personal data reports</span>
              </template>
              <a-icon type="file-pdf" class="icon-btn" />
            </a-tooltip>
          </a-popconfirm>
        </li>
      </ul>
      <!-- 教育信息生成-窗口 -->
      <div id="graduation-window" ref="idEduWindow">
        <a-form
          :form="form"
          layout="horizontal"
          @submit="eduSubmit"
          @submit.native.prevent
        >
          <!-- 教育窗口-教育经历选择 -->
          <a-form-item label=" select the data you want to display">
            <a-checkbox-group
              v-decorator="[
                'eduidList',
                {
                  rules: [{ required: true, message: 'Please select the data you want to display' }],
                },
              ]"
            >
              <a-checkbox
                style="margin-left: 0"
                v-for="edu in linkData.education"
                :key="edu.eduId"
                :value="edu.eduId"
              >
                {{ edu.eduType }} / {{ edu.nodeName }}
              </a-checkbox>
            </a-checkbox-group>
          </a-form-item>
          <!-- 教育窗口-按钮 -->
          <a-form-item :wrapper-col="{ span: 24, offset: 0 }">
            <a-button type="primary" html-type="submit" :loading="isLoad">
              Submit
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
    <div id="graduation-main-layout">
      <a-table
        :columns="educationColumns"
        :data-source="linkData.education"
        :pagination="{ hideOnSinglePage: true, pageSize: 8 }"
        :expanded-row-keys.sync="expandedRowKeys"
        :rowKey="(record) => record.eduId"
        :loading="!linkData"
      >
        <template slot="date" slot-scope="text, record">
          {{ record.beginTime }} ~ {{ record.endTime }}
        </template>
        <template slot="credit" slot-scope="text, record">
          {{ record.acquireCredit }} / {{ record.graduateCredit }}
        </template>
        <template slot="certifyFile" slot-scope="text, record">
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Download graduation certificate documents</span>
            </template>
            <a-icon
              v-if="text != 'null'"
              type="download"
              class="icon-btn"
              style="margin-top: 0px"
              @click="downloadFile(record.eduId, text)"
            />
          </a-tooltip>
        </template>

        <div slot="expandedRowRender" slot-scope="text">
          <!-- 课程信息显示 -->
          <a-table
            :columns="coursesColumns"
            :data-source="text.course"
            :pagination="{ hideOnSinglePage: true, pageSize: 5 }"
            :rowKey="(record) => record.name"
          >
            <template slot="certifyFile" slot-scope="text, record">
              <a-tooltip placement="bottomRight" v-if="text != null">
                <template slot="title">
                  <span>Download Course Certification Document</span>
                </template>
                <a-icon
                  v-if="text != 'null'"
                  type="download"
                  class="icon-btn"
                  style="margin-top: 0px"
                  @click="downloadFile(record.name, text)"
                />
              </a-tooltip>
            </template>
          </a-table>
          <br>
          <!-- 成就信息显示 -->
          <a-table
            :columns="achievesColumns"
            :data-source="text.achieve"
            :pagination="{ hideOnSinglePage: true, pageSize: 5 }"
            :rowKey="(record) => `${record.title}${record.acquireTime}`"
          >
            <template slot="certifyFile" slot-scope="text, record">
              <a-tooltip placement="bottomRight" v-if="text != null">
                <template slot="title">
                  <span>Download Course Certification Document</span>
                </template>
                <a-icon
                  v-if="text != 'null'"
                  type="download"
                  class="icon-btn"
                  style="margin-top: 0px"
                  @click="downloadFile(record.title, text)"
                />
              </a-tooltip>
            </template>
          </a-table>
        </div>
      </a-table>
    </div>
    <div id="pdf-layout" ref="exportPdf">
      <div class="pdf-title">
        <img src="../../../public/icon/simple_favicon.png" alt="" />
        <b> EducationChain-Person Experience Report Card </b>
      </div>
      <div class="pdf-info-layout">
        <a-descriptions
          title="Basic Information"
          style="text-align: left; margin-left: 15px; width: 100%"
          :column="5"
          size="small"
        >
          <a-descriptions-item label="ID Card" :span="2">
            {{ linkData.basic.personId }}
          </a-descriptions-item>
          <a-descriptions-item label="Name">
            {{ linkData.basic.name }}
          </a-descriptions-item>
          <a-descriptions-item label="Gender">
            {{ linkData.basic.gender }}
          </a-descriptions-item>
          <a-descriptions-item label="Nation">
            {{ linkData.basic.nation }}
          </a-descriptions-item>
          <a-descriptions-item label="TransactionHash" :span="3">
            {{ linkData.basic.transactionHash }}
          </a-descriptions-item>
          <a-descriptions-item label="TransactionIndex" :span="2">
            {{ linkData.basic.transactionIndex }}
          </a-descriptions-item>
          <a-descriptions-item label="BlockHash" :span="3">
            {{ linkData.basic.blockHash }}
          </a-descriptions-item>
          <a-descriptions-item label="BlockNumber" :span="2">
            {{ linkData.basic.blockNumber }}
          </a-descriptions-item>
        </a-descriptions>
      </div>
      <div
        v-for="(edu, index) in linkData.education"
        :key="edu.eduId"
        class="pdf-info-layout"
      >
        <a-descriptions
          title="Education Experience"
          style="
            text-align: left;
            padding-top: 16px;
            padding-left: 16px;
            border: 1px solid #e8e8e8;
          "
          :column="4"
          size="small"
        >
          <a-descriptions-item label="Name">
            {{ edu.nodeName }}
          </a-descriptions-item>
          <a-descriptions-item label="Education Type">
            {{ edu.eduType }}
          </a-descriptions-item>
          <a-descriptions-item label="Major">
            {{ edu.majorName }}
          </a-descriptions-item>
          <a-descriptions-item label="Date">
            {{ edu.beginTime }} ~
            {{ edu.endTime }}
          </a-descriptions-item>
        </a-descriptions>
        <!-- 课程信息 -->
        <a-table
          v-if="edu.course.length != 0"
          :columns="pdfCoursesColumns"
          :data-source="edu.course"
          :pagination="false"
          :rowKey="(record) => `${record.name}${index}`"
          bordered
        >
        </a-table>
        <!-- 成就信息 -->
        <a-table
          v-if="edu.achieve.length != 0"
          :columns="pdfAchieveColumns"
          :data-source="edu.achieve"
          :pagination="false"
          :rowKey="(record) => `${record.title}${index}`"
          bordered
        >
          <template #title>
            <div
              style="
                text-align: left;
                color: rgba(0, 0, 0, 0.85);
                font-weight: bold;
                font-size: 16px;
                line-height: 1.5;
              "
            >
            Achievement Info
            </div>
          </template>
        </a-table>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
const educationColumns = [
  {
    title: "Alliance Name",
    dataIndex: "nodeName",
    width: "15%",
    ellipsis: true,
  },
  {
    title: "EduType",
    dataIndex: "eduType",
    width: "10%",
    ellipsis: true,
  },
  {
    title: "Major",
    dataIndex: "majorName",
    width: "15%",
    ellipsis: true,
  },
  {
    title: "Date",
    dataIndex: "date",
    width: "10%",
    scopedSlots: { customRender: "date" },
    ellipsis: true,
  },
  {
    title: "Taken/Due Credits",
    dataIndex: "credit",
    width: "15%",
    scopedSlots: { customRender: "credit" },
    ellipsis: true,
  },
  {
    title: "BlockHash",
    dataIndex: "blockHash",
    width: "25%",
    ellipsis: true,
  },
  {
    title: "Graduation Certificate",
    dataIndex: "certifyFile",
    className: "achieve-download",
    width: "20%",
    scopedSlots: { customRender: "certifyFile" },
    ellipsis: true,
  },
];
const coursesColumns = [
  {
    title: "Coures",
    dataIndex: "name",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Credit",
    dataIndex: "credit",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Certification File",
    dataIndex: "certifyFile",
    className: "achieve-download",
    width: "20%",
    scopedSlots: { customRender: "certifyFile" },
    ellipsis: true,
  },
];
const achievesColumns = [
  {
    title: "Achievement",
    dataIndex: "title",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Acquire Time",
    dataIndex: "acquireTime",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Certification File",
    dataIndex: "certifyFile",
    className: "achieve-download",
    width: "20%",
    scopedSlots: { customRender: "certifyFile" },
    ellipsis: true,
  },
];
const pdfCoursesColumns = [
  {
    title: "Course",
    dataIndex: "name",
    className: "pdf-info-table",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Credit",
    dataIndex: "credit",
    className: "pdf-info-table",
    width: "20%",
    ellipsis: true,
  },
  {
    title: "Certification ID",
    dataIndex: "certifyFile",
    className: "pdf-info-table",
    width: "40%",
    ellipsis: true,
  },
];
const pdfAchieveColumns = [
  {
    title: "Achievement",
    dataIndex: "title",
    className: "pdf-info-table",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "Acquire Time",
    dataIndex: "acquireTime",
    className: "pdf-info-table",
    width: "20%",
    ellipsis: true,
  },
  {
    title: "Certification ID",
    dataIndex: "certifyFile",
    className: "pdf-info-table",
    width: "40%",
    ellipsis: true,
  },
];
export default {
  name: "Graduation",
  beforeCreate() {
    this.form = this.$form.createForm(this);
  },
  mounted() {
    this.$message.loading("Obtaining personal on-chain information......", 0);
    let result = this.getUpLinkEducation();
    result.then((value) => {
      if (value == "noData") {
        this.$message.info("You don't have an on-chain record");
      } else {
        this.linkData = value;
      }
    });
  },
  data() {
    return {
      isLoad: false,
      educationColumns,
      coursesColumns,
      achievesColumns,
      pdfCoursesColumns,
      pdfAchieveColumns,
      expandedRowKeys: [],
      // data
      linkData: {
        basic: {
          personId: "",
          name: "",
          gender: "",
          nation: "",
          birthday: "",
        },
        // education: [
        //   {
        //     eduId: "100000001",
        //     nodeName: "桂林电子科技大学",
        //     allianceType: 0,
        //     eduType: "本科",
        //     beginTime: "2018",
        //     endTime: "2022",
        //     majorName: "计算机科学与技术",
        //     acquireCredit: 50,
        //     graduateCredit: 170,
        //     certifyUri: "www.4399.com",
        //     course: [
        //       {
        //         courseId: "0001",
        //         name: "计算机组成原理",
        //         credit: 3,
        //         certifyUri: "www.hao123.com",
        //       },
        //       {
        //         courseId: "0002",
        //         name: "操作系统",
        //         credit: 3,
        //         certifyUri: "www.hao123.com",
        //       },
        //     ],
        //     achieve: [
        //       // {
        //       //   id: "00001",
        //       //   acquireTime: "2022.1.1",
        //       //   title: "achieve text1",
        //       //   certifyUri: "www.bilibili.com",
        //       // },
        //       // {
        //       //   id: "00002",
        //       //   acquireTime: "2022.1.2",
        //       //   title: "achieve text2",
        //       //   certifyUri: "www.bilibili.com",
        //       // },
        //     ],
        //   },
        //   {
        //     eduId: "100000002",
        //     nodeName: "桂林电子科技大学",
        //     allianceType: 0,
        //     eduType: "硕士",
        //     beginTime: "2023",
        //     endTime: "2025",
        //     majorName: "计算机科学与技术",
        //     acquireCredit: 25,
        //     graduateCredit: 50,
        //     certifyUri: "www.4399.com",
        //     course: [
        //       {
        //         courseId: "1001",
        //         name: "人工智能",
        //         credit: 1,
        //         certifyUri: "www.hao123.com",
        //       },
        //       {
        //         courseId: "1002",
        //         name: "实验室项目",
        //         credit: 1,
        //         certifyUri: "www.hao123.com",
        //       },
        //     ],
        //     achieve: [
        //       {
        //         id: "10001",
        //         acquireTime: "2023.1.1",
        //         title: "achieve text1",
        //         certifyUri: "www.bilibili.com",
        //       },
        //       {
        //         id: "10002",
        //         acquireTime: "2023.1.2",
        //         title: "achieve text2",
        //         certifyUri: "www.bilibili.com",
        //       },
        //     ],
        //   },
        // ],
      },
    };
  },
  methods: {
    ...mapActions("education", {
      getUpLinkEducation: "getUpLinkEducation",
      getDownload: "getDownload",
      getEduId: "getEduId",
      infoKey: "infoKey",
    }),
    // 打开教育信息生成窗口
    eduWindow() {
      if (this.$refs.idEduWindow.style.height != "250px") {
        this.$refs.idEduWindow.style.height = "250px";
        this.$refs.idEduWindow.style.padding = "40px";
      } else {
        this.closeEduWindow();
      }
    },
    // 窗口失去焦点, 关闭窗口
    eduWindowBlur(event) {
      let e = event || window.event;
      let elem = e.target || e.srcElement;
      while (elem) {
        //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && elem.id == "graduation-title-layout") {
          return;
        }
        elem = elem.parentNode;
      }
      this.closeEduWindow();
    },
    // 关闭窗口
    closeEduWindow() {
      this.$refs.idEduWindow.style.height = "0px";
      this.$refs.idEduWindow.style.padding = "0px";
    },
    // 教育信息窗口逻辑处理
    eduSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          let result = null;
          let formData = new FormData();
          console.log(values.eduidList);
          formData.append("eduIdList", values.eduidList);
          this.isLoad = true;
          this.$message.loading("Generating information captcha......", 0);
          // 获取验证个人成绩验证码 *接口未实现
          result = this.infoKey(formData);
          result.then((value) => {
            this.$notification.open({
              message: " data has been sent to the mailbox",
            });
            this.isLoad = false;
          });
          result.catch((error) => {
            this.isLoad = false;
          });
        }
      });
    },
    // 下载毕业证明附件
    downloadFile(id, files) {
      console.log(files)
      let fileList = files.split(";");
      for (let i = 0; i < fileList.length; i++) {
        let fileType = fileList[i].split(".")[1];
        let result = this.getDownload(fileList[i]);
        result
          .then((file) => {
            console.log(file);
            let blob = new Blob([file]);
            let downloadElement = document.createElement("a");
            let href = window.URL.createObjectURL(blob); //创建下载的链接
            downloadElement.href = href;
            downloadElement.style.display = "none";
            downloadElement.download = `${id}(${i}).${fileType}`; //下载后文件名
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
    // pdf下载
    pdfDowdload() {
      this.$PDFSave(this.$refs.exportPdf, "Educational experience report");
      // window.open("url").print();
    },
  },
};
</script>

<style lang="less" scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  // overflow-y: auto;
  height: 700px;
}
#graduation-title-layout {
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
#graduation-main-layout {
  min-width: 1200px;
  height: 645px;
  overflow: auto;
}
#graduation-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#graduation-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#graduation-window {
  text-align: center;
  position: absolute;
  overflow: auto;
  background: #fff;
  right: 0px;
  top: 55px;
  height: 0px;
  width: 300px;
  transition: 0.3s;
  // border-bottom-right-radius: 12px;
  border-bottom-left-radius: 12px;
  box-shadow: 0 0 6px 0 rgb(0 0 0 / 20%);
  z-index: 999;
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
#pdf-layout {
  width: 1270px;
}
/deep/th.pdf-info-table,
/deep/td.pdf-info-table {
  border: 1px solid #ededed;
}
.pdf-title {
  font-size: 30px;
  padding-top: 60px;
  margin-bottom: 80px;
}
.pdf-title img {
  height: 35px;
  margin-right: 15px;
  margin-top: -5px;
  display: inline;
}
.pdf-info-layout {
  width: 80%;
  margin: 40px auto;
}
</style>