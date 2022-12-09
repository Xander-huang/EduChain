<template>
  <div id="page-layout">
    <div id="education-title-layout">
      <b>Personal Education Information</b>
      <ul id="education-menu-layout">
        <li></li>
      </ul>
    </div>
    <div id="education-main-layout">
      <a-table
        :columns="educationColumns"
        :data-source="educationList"
        :pagination="{ hideOnSinglePage: true, pageSize: 8 }"
        :expanded-row-keys.sync="expandedRowKeys"
        :rowKey="(record) => record.eduId"
        :loading="!educationList"
      >
        <!-- 课程信息显示 -->
        <div slot="expandedRowRender" slot-scope="text">
          <a-table
            :columns="coursesColumns"
            :data-source="text.course"
            :pagination="{ hideOnSinglePage: true, pageSize: 8 }"
            :rowKey="(record) => record.courseId"
          >
            <template slot="certifyUri" slot-scope="text">
              <a>
                {{ text }}
              </a>
            </template>
          </a-table>
        </div>
        <template slot="date" slot-scope="text, record">
          {{ record.beginTime }} ~ {{ record.endTime }}
        </template>
        <template slot="credit" slot-scope="text, record">
          {{ record.acquireCredit }} / {{ record.graduateCredit }}
        </template>
      </a-table>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const educationColumns = [
  {
    title: "Alliance Name",
    dataIndex: "nodeName",
    width: "30%",
    ellipsis: true,
  },
  {
    title: "Type",
    dataIndex: "type",
    width: "20%",
    ellipsis: true,
  },
  {
    title: "Date",
    dataIndex: "date",
    width: "20%",
    scopedSlots: { customRender: "date" },
    ellipsis: true,
  },
  {
    title: "Major",
    dataIndex: "majorName",
    width: "30%",
    ellipsis: true,
  },
  {
    title: "Taken/Due Credits ",
    dataIndex: "credit",
    width: "15%",
    scopedSlots: { customRender: "credit" },
    ellipsis: true,
  },
];
const coursesColumns = [
  {
    title: "Course",
    dataIndex: "name",
    width: "30%",
    ellipsis: true,
  },
  {
    title: "Credit",
    dataIndex: "credit",
    width: "20%",
    ellipsis: true,
  },
  {
    title: "Certification",
    dataIndex: "certifyUri",
    width: "50%",
    scopedSlots: { customRender: "certifyUri" },
    ellipsis: true,
  },
];
export default {
  name: "Education",
  beforeCreate() {
    this.form = this.$form.createForm(this);
  },
  mounted() {
    this.$message.loading("Geting individual results......", 0);
    let result = this.getEducation();
    result.then((value) => {
      if (value != "ok") {
        this.$message.info("You do not yet have an education record");
      }
    });
  },
  data() {
    return {
      isLoad: false,
      educationColumns,
      coursesColumns,
      expandedRowKeys: [],
    };
  },
  computed: {
    ...mapState("education", ["educationList"]),
    ...mapState("user", ["userInfo"]),
  },
  methods: {
    ...mapActions("education", {
      getEducation: "getEducation",
      getEduId: "getEduId",
      infoKey: "infoKey",
    }),
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
#education-title-layout {
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
#education-main-layout {
  min-width: 1200px;
  height: 645px;
  overflow: auto;
}
#education-menu-layout {
  position: absolute;
  right: 0;
  top: 0;
}
#education-menu-layout li {
  width: 45px;
  overflow: hidden;
  float: right;
}
#education-window {
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