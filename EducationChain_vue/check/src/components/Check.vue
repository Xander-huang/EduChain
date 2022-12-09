<template>
  <div id="check-layout">
    <a-form-model layout="inline" :model="form">
      <a-form-model-item label="Check Code">
        <a-input v-model="form.CheckCode" placeholder="input Check Code" />
      </a-form-model-item>
      <a-form-model-item label="Check Id">
        <a-input v-model="form.CheckID" placeholder="input Check Id" />
      </a-form-model-item>
      <a-form-model-item>
        <a-button type="primary" @click="ApplyAuthor"> Apply </a-button>
      </a-form-model-item>
    </a-form-model>

    <div itemid="check-layout-title">
      <a-row class="searchpart">
        <a-col :span="16">
          <a-input
            style="margin-top: 8px; width: 466px"
            placeholder="Please enter the data verification code"
            v-model="key"
          />
          <a-button-group style="margin-left: 1%">
            <a-button style="width: 50%" type="primary" @click="search">
              Search
            </a-button>
            <a-button style="width: 50%" @click="clear"> Clear </a-button>
          </a-button-group>
        </a-col>
      </a-row>

      <input
          type="file"
          ref="clearFile"
          id="filePath"
          name="file"
          @change="getFile()"
          multiple="multiplt"
          class="add-file-right-input"
          style="margin-top: 10px"
        />
     
    </div>
    <div v-if="checkInfo.basic == null" id="check-empty">
      <!-- <a-skeleton />
      <a-skeleton />
      <a-skeleton /> -->
    </div>
    <div id="check-layout" v-else>
      <a-collapse :ghost="true">
        <a-collapse-panel key="basic-title" header="Basic Information">
          <a-descriptions
            bordered
            :column="{ xxl: 4, xl: 4, lg: 3, md: 3, sm: 2, xs: 1 }"
          >
            <a-descriptions-item label="PersonId">
              {{ checkInfo.basic.personId }}
            </a-descriptions-item>
            <a-descriptions-item label="Name">
              {{ checkInfo.basic.name }}
            </a-descriptions-item>
            <a-descriptions-item label="Gender">
              {{ checkInfo.basic.gender }}
            </a-descriptions-item>
            <a-descriptions-item label="Nation">
              {{ checkInfo.basic.nation }}
            </a-descriptions-item>
            <a-descriptions-item label="Birthday">
              {{ checkInfo.basic.birthday }}
            </a-descriptions-item>
            <a-descriptions-item label="CreateTime" :span="3">
              {{ checkInfo.basic.createTime }}
            </a-descriptions-item>
            <a-descriptions-item label="TransactionHash" :span="2">
              {{ checkInfo.basic.transactionHash }}
            </a-descriptions-item>
            <a-descriptions-item label="TransactionIndex" :span="2">
              {{ checkInfo.basic.transactionIndex }}
            </a-descriptions-item>
            <a-descriptions-item label="BlockHash" :span="2">
              {{ checkInfo.basic.blockHash }}
            </a-descriptions-item>
            <a-descriptions-item label="BlockNumber" :span="2">
              {{ checkInfo.basic.blockNumber }}
            </a-descriptions-item>
          </a-descriptions>
        </a-collapse-panel>
        <a-collapse-panel
          v-for="education in checkInfo.education"
          :key="education.eduId"
          :header="'Academic Information - ' + education.eduType"
        >
          <a-descriptions bordered>
            <a-descriptions-item label="Institution Name">
              {{ education.nodeName }}
            </a-descriptions-item>
            <a-descriptions-item label="Institution Type">
              <template v-if="education.allianceType == 0">
                High School
              </template>
              <template v-else> Social Teaching and Training </template>
            </a-descriptions-item>
            <a-descriptions-item label="Major">
              {{ education.majorName }}
            </a-descriptions-item>
            <a-descriptions-item label="Admission Time">
              {{ education.beginTime }}
            </a-descriptions-item>
            <a-descriptions-item label="Graduation time">
              {{ education.endTime }}
            </a-descriptions-item>
            <a-descriptions-item label="Receive credit">
              {{ education.acquireCredit }}
            </a-descriptions-item>
            <a-descriptions-item label="Graduation credits">
              {{ education.graduateCredit }}
            </a-descriptions-item>
            <a-descriptions-item label="Academic Certificate" :span="2">
              {{ education.certifyUri }}
            </a-descriptions-item>
            <a-descriptions-item label="CreateTime" :span="3">
              {{ education.createTime }}
            </a-descriptions-item>
            <a-descriptions-item label="TransactionHash" :span="2">
              {{ education.transactionHash }}
            </a-descriptions-item>
            <a-descriptions-item label="TransactionIndex" :span="2">
              {{ education.transactionIndex }}
            </a-descriptions-item>
            <a-descriptions-item label="BlockHash" :span="2">
              {{ education.blockHash }}
            </a-descriptions-item>
            <a-descriptions-item label="BlockNumber" :span="2">
              {{ education.blockNumber }}
            </a-descriptions-item>
            <a-descriptions-item label="" :span="3">
              <a-badge status="processing" text="Course Information" />
            </a-descriptions-item>
            <a-descriptions-item
              v-for="(course, index) in education.course"
              :key="course.key"
              :label="index + 1"
              :span="3"
            >
              <div class="check-course-achieve-info">
                <div style="flex-basis: 70%">
                  <b>Name:</b> {{ course.name }}
                </div>
                <div style="flex-basis: 30%">
                  <b>Credits:</b> {{ course.credit }}
                </div>
                <div style="">
                  <b>Certificates:</b>
                  {{ course.certifyFile }}
                </div>
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="" :span="3">
              <a-badge status="processing" text="Achievement Information" />
            </a-descriptions-item>
            <a-descriptions-item
              v-for="(achieve, index) in education.achieve"
              :key="achieve.id"
              :label="index + 1"
              :span="3"
            >
              <div class="check-course-achieve-info">
                <div style="flex-basis: 40%">
                  <b>Date:</b> {{ achieve.acquireTime }}
                </div>
                <div style="flex-basis: 60%">
                  <b>Content:</b> {{ achieve.title }}
                </div>
                <div style="flex-basis: 100%">
                  <b>Certificates:</b>
                  {{ achieve.certifyFile }}
                </div>
              </div>
            </a-descriptions-item>
          </a-descriptions>
        </a-collapse-panel>
      </a-collapse>
    </div>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
import { GetAuthorization, SearchInfo } from "../api";

import axios from "axios";
export default {
  name: "Check",
  data() {
    return {
     
      checkInfo: "",
      key: "",
      fileObj: "",
      form: {
        layout: "inline",
        CheckCode: "",
        CheckID: "",
      },
    };
  },
  computed: {
    ...mapState("check", ["checkInfo"]),
    mobile() {
      return this.$mobile;
    },
  },
  methods: {
    ...mapActions("check", {
      dataKey: "dataKey",
    }),
    ...mapMutations("check", {
      CLEAR: "CLEAR",
    }),
    getFile(info) {
      this.fileObj = document.getElementById("filePath").files[0];
      //  this.fileObj = info.fileList[0]
      // console.log(info);
      // console.log(info.fileList[0]);
    },
    search() {
      let formData = new FormData();
      formData.append("file", this.fileObj);
      formData.append("personId", this.form.CheckID);
      console.log(formData.get("file"));
      SearchInfo(this.key, formData)
        .then((res) => {
          console.log(res);
          this.checkInfo = res.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // search() {
    //   this.$message.loading("Get information in......", 0);
    //   let result = this.dataKey(this.key);
    //   result
    //     .then((value) => {
    //       this.$message.destroy();
    //       this.$message.success("Success!");
    //     })
    //     .catch((error) => {
    //       let message = null;
    //       error = String(error).slice(7, 10);
    //       if (error == 204) {
    //         message = "Request normal, no data returned";
    //       } else if (error == 301) {
    //         message = "Redirection";
    //       } else if (error == 400) {
    //         // 客户端请求参数异常
    //         message = "Please check the input data for errors";
    //       } else if (error == 401) {
    //         message = "Not authorized, no permission";
    //       } else if (error == 404) {
    //         message = "Server does not have corresponding resources";
    //       } else if (error == 500) {
    //         message = "Server Error";
    //       } else {
    //         message = "Unknown error";
    //       }
    //       this.$message.destroy();
    //       this.$message.error(`Failed to get!${message}`);
    //     });
    // },
    clear() {
      this.key = "";
      this.CLEAR();
    },
    ApplyAuthor() {
      console.log(this.form.CheckCode);
      console.log(this.form.CheckID);
      let data = {
        personId: this.form.CheckID,
      };
      GetAuthorization(this.form.CheckCode, data)
        .then((value) => {
          this.$message.success("Success!");
        })
        .catch((error) => {
          console.log("error" + error);
        });
      console.log(12312312312);
      // let param={

      // }
      // axios
      //   .get(`http://202.193.58.103:8084/api/projectInfo/GetAll`,
      //     { params: params })
      //   .then((value) => {
      //     this.$message.success("Success!");
      //   })
      //   .catch((error) => {
      //     console.log("error" + error)
      //   });
    },
  },
};
</script>

<style>
#check-layout {
  min-height: 600px;
}

#check-layout-title {
  position: relative !important;
  height: 55px;
  line-height: 55px;
  text-align: center;
  border-bottom: 1px solid #e8e8e8;
}

#check-layout {
  width: 80%;
  margin: 20px 10% 10px;
}

.check-course-achieve-info {
  display: flex;
  text-align: left;
  flex-wrap: wrap;
}
.searchpart {
  display: flex;
  justify-content: center;
}
.inputcss {
  display: none;
}
</style>
