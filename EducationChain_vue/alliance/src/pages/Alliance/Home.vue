<template>
  <div id="page-layout">
    <a-row class="home-main-layout" type="flex" justify="space-around">
      <a-col
        class="info-card"
        style="background: #33cabb"
        :xs="22"
        :sm="10"
        :md="10"
        :lg="5"
        :xl="5"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="team" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Total number of students</p>
          <p style="font-size: 24px">{{ homeInfo.student }}</p>
        </div>
      </a-col>
      <a-col
        class="info-card"
        style="background: #f96868"
        :xs="22"
        :sm="10"
        :md="10"
        :lg="5"
        :xl="5"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="book" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Total number of majors</p>
          <p style="font-size: 24px">{{ homeInfo.major }}</p>
        </div>
      </a-col>
      <a-col
        class="info-card"
        style="background: #15c377"
        :xs="22"
        :sm="10"
        :md="10"
        :lg="5"
        :xl="5"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="file-text" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Total number of courses</p>
          <p style="font-size: 24px">{{ homeInfo.course }}</p>
        </div>
      </a-col>
      <a-col
        class="info-card"
        style="background: #926dde"
        :xs="22"
        :sm="10"
        :md="10"
        :lg="5"
        :xl="5"
      >
        <div class="info-card-icon">
          <a-icon style="font-size: 48px" type="link" />
        </div>
        <div class="info-card-text">
          <p style="font-size: 12px; margin-bottom: 5px">Number of UpChain people </p>
          <p style="font-size: 24px">{{ homeInfo.uplinked }}</p>
        </div>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="23"
        :xl="23"
      >
        <a-card title="Home Card">
          <p>Welcome to EducationChain-Alliance</p>
          <p>
            you are:
            <template v-if="userInfo.role == 'admin'"> administrator </template>
            <template v-else-if="userInfo.role == 'director'">
              director
            </template>
            <template v-else> teacher </template>
          </p>
          <p v-if="userInfo.role == 'admin'">You can manage members in the alliance</p>
          <p v-else-if="userInfo.role == 'director'">
            You can manage the majors, courses and students in the alliance. And complete the on-chain of student results
          </p>
          <p v-else>You can manage the grades of students in the alliance and certify the experience of students</p>
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="11"
        :xl="11"
      >
        <a-card title="Union Enrollment">
          <Chart
            :xAxis="homeInfo.annualEnrollment.subscript"
            :series="[
              {
                name: 'Enrollment',
                type: 'bar',
                data: homeInfo.annualEnrollment.data,
              },
            ]"
            :legend="[{ name: 'Enrollment' }]"
            :color="['#ff7f50']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="11"
        :xl="11"
      >
        <a-card title="The number of students on the chain of the alliance">
          <Chart
            :xAxis="homeInfo.annualGraduation.subscript"
            :series="[
              {
                name: 'Number of people on the chain',
                type: 'bar',
                data: homeInfo.annualGraduation.data,
              },
            ]"
            :legend="[{ name: 'Number of people on the chain' }]"
            :color="['#87cefa']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="11"
        :xl="11"
      >
        <a-card title="The number of alliance majors">
          <Chart
            :xAxis="homeInfo.annualMajor.subscript"
            :series="[
              {
                name: 'Professional quantity',
                type: 'line',
                data: homeInfo.annualMajor.data,
              },
            ]"
            :legend="[{ name: 'Professional quantity' }]"
            :color="['#da70d6']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="11"
        :xl="11"
      >
        <a-card title="The number of Alliance Courses">
          <Chart
            :xAxis="homeInfo.annualCourse.subscript"
            :series="[
              {
                name: 'number of courses',
                type: 'line',
                data: homeInfo.annualCourse.data,
              },
            ]"
            :legend="[{ name: 'number of courses' }]"
            :color="['#32cd32']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="23"
        :xl="23"
      >
        <a-card title="Alliance Majors">
          <Chart
            :yAxis="homeInfo.majorNum.subscript"
            :series="[
              {
                name: 'Number of Majors',
                type: 'bar',
                data: homeInfo.majorNum.data,
              },
            ]"
            :legend="[{ name: 'Number of Majors' }]"
            :inverseY="true"
            :color="['#6495ed']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="23"
        :xl="23"
      >
        <a-card title="Alliance Professional Achievement Information">
          <Chart
            :yAxis="homeInfo.majorAverage.subscript"
            :series="[
              {
                name: 'professional average',
                type: 'bar',
                data: homeInfo.majorAverage.data,
              },
            ]"
            :legend="[{ name: 'professional average' }]"
            :inverseY="true"
            :color="['#ff69b4']"
          />
        </a-card>
      </a-col>
      <a-col
        class="home-info-layout"
        :xs="22"
        :sm="22"
        :md="22"
        :lg="23"
        :xl="23"
      >
        <a-card title="Union Student Overall Achievement Information">
          <Chart
            :series="[
              {
                name: 'grade of student',
                type: 'pie', // 设置图表类型为饼图
                radius: '55%', // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
                data: [
                  { value: homeInfo.education[0], name: 'excellent' },
                  { value: homeInfo.education[1], name: 'good' },
                  { value: homeInfo.education[2], name: 'pass' },
                  { value: homeInfo.education[3], name: 'failed' },
                ],
              },
            ]"
            :color="['#87cefa', '#00fa9a', '#ffd700', '#ff6347']"
          />
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import Chart from "../../components/Chart";
import { mapState, mapActions } from "vuex";
export default {
  name: "AllianceHome",
  components: {
    Chart,
  },
  mounted() {
    this.getHomeInfo();
  },
  data() {
    return {};
  },
  computed: {
    ...mapState("user", ["userInfo", "homeInfo"]),
  },
  methods: {
    ...mapActions("user", {
      getHomeInfo: "getHomeInfo",
    }),
    getHomeData() {
      let result = this.getHomeInfo();
      // result.then((value) => {
      //   console.log(value);
      //   this.student = value.student;
      //   this.major = value.major;
      //   this.course = value.course;
      //   this.uplinked = value.uplinked;
      //   this.annualEnrollment = value.annualEnrollment || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.annualGraduation = value.annualGraduation || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.annualMajor = value.annualMajor || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.annualCourse = value.annualCourse || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.majorAverage = value.majorAverage || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.majorNum = value.majorNum || {
      //     subscript: [0],
      //     data: [0],
      //   };
      //   this.education = value.education || [5, 5, 5, 5];
      //   console.log(this.education[0]);
      // });
    },
  },
};
</script>

<style scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: auto;
  height: 700px;
  padding: 30px;
  background: #f5f6fa;
}
.home-main-layout {
  margin-bottom: 24px;
}
.home-info-layout {
  margin-bottom: 24px;
  background: #fff;
  text-align: left;
}
.info-card {
  height: 106px;
  padding: 24px 24px;
  margin-bottom: 24px;
  color: #fff;
  overflow: hidden;
}
.info-card-icon {
  float: left;
  margin-left: 5px;
}
.info-card-text {
  float: right;
  margin-right: 5px;
  margin-top: 4px;
}
</style>