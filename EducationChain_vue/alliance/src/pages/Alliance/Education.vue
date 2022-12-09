<template>
  <div id="page-layout">
    <div id="education-title-layout">
      <b>League Student Performance Management</b>
      <ul id="education-menu-layout">
        <li>
          <a-tooltip placement="bottomRight">
            <template slot="title">
              <span>Batch input student grades</span>
            </template>
            <a-upload
              name="file"
              :multiple="false"
              :beforeUpload="fileUploadEducation"
              :showUploadList="false"
              :disabled="userInfo.role == 'director'"
            >
              <a-icon type="upload" class="icon-btn" />
            </a-upload>
          </a-tooltip>
        </li>
      </ul>
    </div>
    <div id="education-main-layout">
      <a-table
        :columns="studentColumns"
        :data-source="studentList"
        :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
        :rowKey="(record) => record.stuId"
        :expandedRowKeys="expandedRowKeys"
        @expand="onExpand"
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
        <!-- 上链 -->
        <template slot="isUplinked" slot-scope="text">
          <template>
            <div v-if="text == 0" style="color: #000">No</div>
            <div v-else style="color: #1890ff">Yes</div>
          </template>
        </template>
        <!-- 操作-添加课程-修改成绩 -->
        <template slot="operation" slot-scope="text, record">
          <!-- 修改成绩部分 -->
          <span v-if="record.editable">
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Confirm score modification?"
              @confirm="() => saveEdit(record.stuId)"
            >
              <a style="margin-right: 8px"> Preserve </a>
            </a-popconfirm>
            <a @click="() => cancelEdit(record.stuId)"> Cancel </a>
          </span>
          <!-- 添加课程部分 -->
          <span v-else-if="addKey == record.stuId">
            <a-popconfirm
              placement="topRight"
              ok-text="Yes"
              title="Add course or not?"
              @confirm="() => addCourse(record.stuId)"
            >
              <a style="margin-right: 8px"> Add </a>
            </a-popconfirm>
            <a @click="() => cancelAdd()"> Cancel </a>
          </span>
          <!-- 添加课程，修改成绩主要功能 -->
          <span v-else>
            <a
              style="margin-right: 8px"
              :disabled="
                editingKey != '' ||
                addKey != '' ||
                record.isUplinked == 1 ||
                userInfo.role == 'teacher'
              "
              @click="() => handleAddCourseInStudent(record.stuId)"
            >
            Add Course
            </a>
            <a
              :disabled="
                editingKey != '' ||
                addKey != '' ||
                record.isUplinked == 1 ||
                userInfo.role == 'director'
              "
              @click="() => editEducation(record.stuId)"
            >
            Enter grades
            </a>
          </span>
        </template>
        <div slot="expandedRowRender" slot-scope="text">
          <!-- 添加课程 -->
          <div v-if="addKey == text.stuId" style="display: flex">
            <a-select
              style="width: 20%; margin-right: 8px; margin-left: -5px"
              placeholder="Please select an education type"
              v-model="course.type"
              @change="getMajor"
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
            <a-select
              style="width: 20%; margin-right: 8px"
              show-search
              option-filter-prop="children"
              :filter-option="filterOption"
              v-model="course.majorId"
              @change="getCourseByMajor"
              placeholder="Please select your major"
            >
              <a-select-option
                v-for="major in majorList"
                :key="major.majorId"
                :value="major.majorId"
              >
                {{ major.name }}
              </a-select-option>
            </a-select>
            <a-select
              style="width: 20%"
              show-search
              option-filter-prop="children"
              :filter-option="filterOption"
              v-model="course.courseId"
              placeholder="Please select a professional course"
            >
              <a-select-option
                v-for="course in courseList"
                :key="course.courseId"
                :value="course.courseId"
              >
                {{ course.name }}
              </a-select-option>
            </a-select>
          </div>
          <!-- 学生个人成绩显示，修改 -->
          <a-table
            v-else
            :columns="courseColumns"
            :data-source="text.course"
            :pagination="{ hideOnSinglePage: true, pageSize: 5 }"
            :rowKey="(record) => record.courseId"
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
                @click="
                  () => handleSearch(selectedKeys, confirm, column.dataIndex)
                "
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
            <!-- 分数-显示，修改 -->
            <template slot="score" slot-scope="text, record">
              <div>
                <a-input-number
                  v-if="record.editable"
                  style="margin: -5px 0; width: 50%"
                  :defaultValue="text"
                  :min="0"
                  :max="100"
                  @change="
                    (e) => changeEducation(e, record.stuId, record.courseId)
                  "
                />
                <template v-else>
                  {{ text }}
                </template>
              </div>
            </template>
            <!-- 操作-删除课程 -->
            <template slot="operation" slot-scope="text, record">
              <a-popconfirm
                placement="topRight"
                ok-text="Yes"
                title="Are you sure to delete?"
                @confirm="
                  () => handleDelCourseInStudent(record.stuId, record.courseId)
                "
              >
                <a-icon slot="icon" type="warning" style="color: red" />
                <a
                  style="margin-right: 8px"
                  :disabled="
                    editingKey != '' ||
                    record.isUplinked == 1 ||
                    userInfo.role == 'teacher'
                  "
                >
                Delete
                </a>
              </a-popconfirm>
              <a-upload
                name="file"
                :multiple="false"
                :beforeUpload="
                  (file) =>
                    fileUploadCertificate(file, record.courseId, record.stuId)
                "
                :showUploadList="false"
              >
                <a :disabled="editingKey != '' || record.uplinked == 1">
                  Upload course certificate
                </a>
              </a-upload>
            </template>
          </a-table>
        </div>
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
const courseColumns = [
  {
    title: "Course",
    dataIndex: "name",
    width: "",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.name.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Credit",
    dataIndex: "credit",
    width: "",
    ellipsis: true,
  },
  {
    title: "Score",
    dataIndex: "score",
    width: "",
    scopedSlots: { customRender: "score" },
    ellipsis: true,
  },
  {
    title: "Operation",
    ellipsis: true,
    scopedSlots: { customRender: "operation" },
  },
];
export default {
  name: "Education",
  mounted() {
    this.$message.loading("Getting student information......", 0);
    let result = this.getStudentEducation();
    result.then((value) => {
      this.cacheData = [];
      let tempCacheData = this.studentList.map((item) => ({ ...item }));
      for (let i = 0; i < tempCacheData.length; i++) {
        let tempCacheDataCell = tempCacheData[i].course.map((item) => ({
          ...item,
        }));
        tempCacheData[i].course = tempCacheDataCell;
        this.cacheData.push(tempCacheData[i]);
      }
      this.$message.success("Get succeeded");
    });
    // this.cacheData = [];
    // let tempCacheData = this.studentList.map((item) => ({ ...item }));
    // for (let i = 0; i < tempCacheData.length; i++) {
    //   let tempCacheDataCell = tempCacheData[i].course.map((item) => ({
    //     ...item,
    //   }));
    //   tempCacheData[i].course = tempCacheDataCell;
    //   this.cacheData.push(tempCacheData[i]);
    // }
  },
  data() {
    return {
      course: {
        type: undefined,
        majorId: undefined,
        courseId: undefined,
      },
      majorList: [],
      courseList: [],
      expandedRowKeys: [],
      // flag
      editingKey: "",
      addKey: "",
      // rule
      studentColumns,
      courseColumns,
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    ...mapState("education", ["studentList"]),
  },
  methods: {
    ...mapMutations("education", {
      EDUCATION_INFO: "EDUCATION_INFO",
      ADD_COURSE: "ADD_COURSE",
      DEL_COURSE: "DEL_COURSE",
      GET_EDITABLE: "GET_EDITABLE",
      DEL_EDITABLE: "DEL_EDITABLE",
    }),
    ...mapActions("education", {
      getStudentEducation: "getStudentEducation",
      updateStudentEducation: "updateStudentEducation",
      updateStudentEducations: "updateStudentEducations",
    }),
    ...mapActions("course", {
      getCourse: "getCourse",
      addStudentCourse: "addStudentCourse",
      delStudentCourse: "delStudentCourse",
    }),
    ...mapActions("major", {
      getMajorByType: "getMajorByType",
    }),
    ...mapActions("student", {
      addCertificate: "addCertificate",
    }),
    // 修改学生成绩，本地
    changeEducation(score, stuId, courseId) {
      const newData = [...this.studentList];
      const student = newData.filter((item) => stuId === item.stuId)[0];
      const target = student.course.filter(
        (item) => courseId === item.courseId
      )[0];
      if (target) {
        target["score"] = score;
        this.EDUCATION_INFO(newData);
      }
    },
    // 编辑学生成绩
    editEducation(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      this.editingKey = key;
      if (target) {
        this.EDUCATION_INFO(newData);
        this.GET_EDITABLE(key);
      }
      this.expandedRowKeys.push(key);
    },
    // 修改学生成绩，上传
    saveEdit(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0];
      let { stuId, course } = { ...target };
      let courseList = [];
      for (let i = 0; i < course.length; i++) {
        courseList.push({
          courseId: course[i].courseId,
          score: course[i].score,
        });
      }
      this.$message.loading("Editing student grades......", 0);
      console.log(courseList);
      let result = this.updateStudentEducation({ stuId, courseList });
      result.then((value) => {
        const newCacheData = [...this.cacheData];
        const targetCache = newCacheData.filter((item) => key === item.stuId)[0]
          .course;
        this.EDUCATION_INFO(newData);
        this.DEL_EDITABLE(key);
        for (let i = 0; i < target.course.length; i++) {
          Object.assign(targetCache[i], target.course[i]);
        }
        this.cacheData = newCacheData;
        this.editingKey = "";
        this.$message.success("Modification succeeded!");
      });
    },
    // 取消编辑
    cancelEdit(key) {
      const newData = [...this.studentList];
      const target = newData.filter((item) => key === item.stuId)[0].course;
      const targetCache = this.cacheData.filter((item) => key === item.stuId)[0]
        .course;
      this.editingKey = "";
      if (target) {
        for (let i = 0; i < target.length; i++) {
          Object.assign(target[i], targetCache[i]);
        }
        this.EDUCATION_INFO(newData);
        this.DEL_EDITABLE(key);
      }
    },
    // 添加课程逻辑处理
    handleAddCourseInStudent(key) {
      this.addKey = key;
      this.expandedRowKeys.push(key);
    },
    // 添加课程
    addCourse(stuId) {
      if (this.course.courseId != undefined) {
        let formData = new FormData();
        formData.append("stuId", stuId);
        formData.append("courseId", this.course.courseId);
        this.$message.loading("Adding Course......");
        let result = this.addStudentCourse(formData);
        result.then((value) => {
          let { name, credit } = this.courseList.filter(
            (item) => this.course.courseId === item.courseId
          )[0];
          const course = {
            courseId: this.course.courseId,
            stuId: stuId,
            isUplinked: 0,
            name: name,
            credit: credit,
            score: null,
          };
          this.ADD_COURSE(course);
          this.cancelAdd();
          this.$message.success("Successfully added");
          let result = this.getStudentEducation();
          result.then((value) => {
            this.cacheData = [];
            let tempCacheData = this.studentList.map((item) => ({ ...item }));
            for (let i = 0; i < tempCacheData.length; i++) {
              let tempCacheDataCell = tempCacheData[i].course.map((item) => ({
                ...item,
              }));
              tempCacheData[i].course = tempCacheDataCell;
              this.cacheData.push(tempCacheData[i]);
            }
          });
        });
      } else {
        this.$message.error("Please select a course to add");
      }
    },
    // 取消添加课程
    cancelAdd() {
      this.addKey = "";
      this.expandedRowKeys.pop();
      this.course = {
        type: undefined,
        majorId: undefined,
        courseId: undefined,
      };
      this.majorList = [];
      this.courseList = [];
    },
    // 根据教育类型获取专业
    getMajor() {
      let type = null;
      if (this.course.type == "本科") {
        type = 0;
      } else if (this.course.type == "硕士") {
        type = 1;
      } else if (this.course.type == "博士") {
        type = 2;
      } else if (this.course.type == "职业教育") {
        type = 3;
      }
      let result = this.getMajorByType(type);
      result.then((value) => {
        this.majorList = value;
        this.courseList = [];
        this.course.majorId = undefined;
        this.course.courseId = undefined;
      });
    },
    // 根据专业获取课程
    getCourseByMajor() {
      let result = this.getCourse(this.course.majorId);
      result.then((value) => {
        this.courseList = value;
        this.course.courseId = undefined;
      });
    },
    // 删除课程
    handleDelCourseInStudent(stuId, courseId) {
      let formData = new FormData();
      formData.append("stuId", stuId);
      formData.append("courseId", courseId);
      this.$message.loading("Deleting a course......");
      let result = this.delStudentCourse(formData);
      result.then((value) => {
        this.DEL_COURSE({ stuId, courseId });
        this.$message.success("Deletion succeeded");
        let result = this.getStudentEducation();
        result.then((value) => {
          this.cacheData = [];
          let tempCacheData = this.studentList.map((item) => ({ ...item }));
          for (let i = 0; i < tempCacheData.length; i++) {
            let tempCacheDataCell = tempCacheData[i].course.map((item) => ({
              ...item,
            }));
            tempCacheData[i].course = tempCacheDataCell;
            this.cacheData.push(tempCacheData[i]);
          }
        });
      });
    },
    // 查询专业, 课程列表
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
    // 编辑锁定操作
    onExpand(expanded, record) {
      // expanded控制操作类型
      if (expanded) {
        // 设置展开窗Key，代表展开操作
        if (this.editingKey == "" && this.addKey == "") {
          this.expandedRowKeys.push(record.stuId);
        }
      } else {
        // 代表折叠操作
        if (
          this.expandedRowKeys.length &&
          this.editingKey == "" &&
          this.addKey == ""
        ) {
          this.expandedRowKeys = this.expandedRowKeys.filter((v) => {
            return v !== record.stuId;
          });
        }
      }
    },
    // 筛选功能
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
    fileUploadEducation(file) {
      const isSHEET =
        file.type ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||
        file.type === "application/vnd.ms-excel";
      if (!isSHEET) {
        this.$message.error("You can only upload xlsx or csv files");
      } else {
        let formData = new FormData();
        formData.append("file", file);
        this.$message.loading("Entering grades......");
        let result = this.updateStudentEducations(formData);
        result.then((value) => {
          this.$message.success("Successfully entered");
          this.getStudentEducation();
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
  },
};
</script>

<style scoped>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
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
.icon-btn {
  margin-top: 20px;
  font-size: 18px;
  cursor: pointer;
}
.icon-btn:hover {
  color: #108ee9;
}
#education-main-layout {
  min-width: 1200px;
  height: 645px;
  overflow: auto;
}
</style>