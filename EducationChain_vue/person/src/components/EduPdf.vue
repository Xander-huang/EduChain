<template>
  <div>
    <div class="pdf-title">
      <img src="../../public/icon/simple_favicon.png" alt="" />
      <b> EducationChain-Person Experience Report Card </b>
    </div>
    <div class="pdf-info-layout">
      <a-descriptions
        title="基本信息"
        style="text-align: left; margin-left: 15px"
        :column="4"
        size="small"
      >
        <a-descriptions-item label="身份证">
          {{ userInfo.personId }}
        </a-descriptions-item>
        <a-descriptions-item label="姓名">
          {{ userInfo.name }}
        </a-descriptions-item>
        <a-descriptions-item label="性别">
          {{ userInfo.gender }}
        </a-descriptions-item>
        <a-descriptions-item label="民族">
          {{ userInfo.nation }}
        </a-descriptions-item>
      </a-descriptions>
    </div>
    <div
      v-for="(edu, index) in educationList"
      :key="edu.eduId"
      class="pdf-info-layout"
    >
      <!-- 课程信息 -->
      <a-table
        :columns="coursesColumns"
        :data-source="edu.course"
        :pagination="false"
        :rowKey="(record) => record.courseId"
        bordered
      >
        <template #title>
          <a-descriptions
            title="教育经历"
            style="text-align: left"
            :column="4"
            size="small"
          >
            <a-descriptions-item label="名称">
              {{ edu.nodeName }}
            </a-descriptions-item>
            <a-descriptions-item label="类型">
              {{ edu.type }}
            </a-descriptions-item>
            <a-descriptions-item label="专业">
              {{ edu.majorName }}
            </a-descriptions-item>
            <a-descriptions-item label="时间">
              {{ edu.beginTime }} ~
              {{ edu.endTime }}
            </a-descriptions-item>
          </a-descriptions>
        </template>
      </a-table>
      <!-- 成就信息 -->
      <a-table
        v-if="achieveList != null"
        :columns="achieveColumns"
        :data-source="achieveList[index].achieve"
        :pagination="false"
        :rowKey="(record) => record.id"
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
            成就信息
          </div>
        </template>
      </a-table>
    </div>
    <div class="pdf-info-layout" style="text-align: left;">
      <b>文件校验码</b>: {{ achieveObj.captcha }}
    </div>
  </div>
</template>

<script>
const coursesColumns = [
  {
    title: "课程编号",
    dataIndex: "courseId",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "课程名称",
    dataIndex: "name",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "学分",
    dataIndex: "credit",
    width: "20%",
    ellipsis: true,
  },
];
const achieveColumns = [
  {
    title: "成就",
    dataIndex: "title",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "获得时间",
    dataIndex: "acquireTime",
    width: "40%",
    ellipsis: true,
  },
  {
    title: "类型",
    dataIndex: "type",
    width: "20%",
    ellipsis: true,
  },
];
export default {
  name: "EduPdf",
  props: ["userInfo", "educationList", "achieveObj"],
  mounted() {
    this.achieveList = this.achieveObj.achieveList;
  },
  data() {
    return {
      coursesColumns,
      achieveColumns,
      achieveList: null,
    };
  },
};
</script>

<style>
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