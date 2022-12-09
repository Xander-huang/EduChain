<template>
  <div id="page-layout">
    <div id="add-course-layout">
      <a-input
        id="add-course-input-name"
        placeholder="Please enter the course name to add"
        v-model="course.name"
      >
      </a-input>
      <a-select
        id="add-course-input-credit"
        placeholder="select course credits"
        v-model="course.credit"
      >
        <a-select-option
          v-for="credit in creditList"
          :key="credit"
          :value="credit"
        >
          {{ credit }}
        </a-select-option>
      </a-select>
      <a-button id="add-course-add" @click="handleAddCourse"> Add </a-button>
      <a-button id="add-course-clear" @click="clear"> Clear </a-button>
      <a-tooltip placement="bottomRight" id="add-course-file">
        <template slot="title">
          <span>Batch add courses</span>
        </template>
        <a-upload
          name="file"
          :multiple="false"
          :beforeUpload="fileUpload"
          :showUploadList="false"
        >
          <a-button> <a-icon type="upload" /></a-button>
        </a-upload>
      </a-tooltip>
    </div>
    <!-- 课程列表 -->
    <a-table
      bordered
      :data-source="courseList"
      :columns="courseColumns"
      :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
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
      <!-- 课程名称 -->
      <template slot="name" slot-scope="text, record">
        <a-input
          v-if="record.editable"
          style="margin: -5px 0; width: 100%"
          :value="text"
          @change="(e) => changeCourse(e.target.value, record.courseId, 'name')"
        />
        <template v-else>
          {{ text }}
        </template>
      </template>
      <!-- 课程学分 -->
      <template slot="credit" slot-scope="text, record">
        <a-select
          v-if="record.editable"
          style="margin: -5px 0; width: 100%"
          :value="text"
          @change="(value) => changeCourse(value, record.courseId, 'credit')"
        >
          <a-select-option
            v-for="credit in creditList"
            :key="credit"
            :value="credit"
          >
            {{ credit }}
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
            @confirm="() => save(record.courseId)"
          >
            <a style="margin-right: 8px">Preserve</a>
          </a-popconfirm>
          <a @click="() => cancel(record.courseId)">Cancel</a>
        </span>
        <span v-else>
          <a
            style="margin-right: 8px"
            :disabled="editingKey != ''"
            @click="() => edit(record.courseId)"
          >
            Change
          </a>
          <a-popconfirm
            placement="topRight"
            ok-text="Yes"
            title="Are you sure to delete?"
            @confirm="() => handleDelCourse(record.courseId)"
          >
            <a-icon slot="icon" type="warning" style="color: red" />
            <a :disabled="editingKey != ''"> Delete </a>
          </a-popconfirm>
        </span>
      </template>
    </a-table>
  </div>
</template>

<script>
import { mapState, mapMutations, mapActions } from "vuex";
const courseColumns = [
  {
    title: "Course ID",
    dataIndex: "courseId",
    width: "20%",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.courseId.toString().toLowerCase().includes(value.toLowerCase()),
    sorter: (a, b) => a.courseId - b.courseId,
    sortDirections: ["descend", "ascend"],
  },
  {
    title: "Course",
    dataIndex: "name",
    width: "41%",
    ellipsis: true,
    scopedSlots: {
      customRender: "name",
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
    },
    onFilter: (value, record) =>
      record.name.toString().toLowerCase().includes(value.toLowerCase()),
  },
  {
    title: "Credit",
    dataIndex: "credit",
    width: "20%",
    ellipsis: true,
    scopedSlots: {
      customRender: "credit",
    },
    filters: [
      {
        text: 0,
        value: 0,
      },
      {
        text: 1,
        value: 1,
      },
      {
        text: 2,
        value: 2,
      },
      {
        text: 3,
        value: 3,
      },
      {
        text: 4,
        value: 4,
      },
      {
        text: 5,
        value: 5,
      },
      {
        text: 6,
        value: 6,
      },
      {
        text: 7,
        value: 7,
      },
    ],
    filterMultiple: true,
    onFilter: (value, record) => record.credit === value,
    sorter: (a, b) => a.credit - b.credit,
    sortDirections: ["descend", "ascend"],
  },
  {
    title: "Operation",
    width: "19%",
    scopedSlots: { customRender: "operation" },
    ellipsis: true,
  },
];
export default {
  name: "Course",
  mounted() {
    if (this.majorList.length != 0) {
      let result = this.getCourse(this.$route.query.majorId);
      result.then((value) => {
        this.cacheData = this.courseList.map((item) => ({ ...item }));
      });
    }
    // this.cacheData = this.courseList.map((item) => ({ ...item }));
  },
  data() {
    return {
      course: {
        name: undefined,
        credit: undefined,
      },
      // role
      courseColumns,
      // flag
      editingKey: "",
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("course", ["courseList", "creditList"]),
    ...mapState("major", ["majorList"]),
  },
  methods: {
    ...mapMutations("course", {
      COURSE_INFO: "COURSE_INFO",
      GET_EDITABLE: "GET_EDITABLE",
      DEL_EDITABLE: "DEL_EDITABLE",
    }),
    ...mapActions("course", {
      getCourse: "getCourse",
      addCourse: "addCourse",
      delCourse: "delCourse",
      updateCourse: "updateCourse",
      addCourses: "addCourses",
    }),
    // 添加课程
    handleAddCourse() {
      if (this.course.name.trim() != "" && this.course.credit != undefined) {
        let formData = new FormData();
        formData.append("majorId", this.$route.query.majorId);
        formData.append("name", this.course.name);
        formData.append("credit", this.course.credit);
        this.$message.loading("Adding Course......");
        let result = this.addCourse(formData);
        result.then((value) => {
          this.$message.success("Successfully added");
          this.clear()
        });
      }
    },
    // 删除课程
    handleDelCourse(courseId) {
      let formData = new FormData();
      formData.append("courseId", courseId);
      this.$message.loading("Deleting a course......");
      let result = this.delCourse(formData);
      result.then((value) => {
        this.$message.success("Deletion succeeded");
      });
    },
    // 清空课程添加
    clear() {
      this.course = {
        name: undefined,
        credit: undefined,
      };
    },
    // 修改课程数据，本地
    changeCourse(value, key, column) {
      const newData = [...this.courseList];
      const target = newData.filter((item) => key === item.courseId)[0];
      if (target) {
        target[column] = value;
        this.COURSE_INFO(newData);
      }
    },
    // 编辑课程数据
    edit(key) {
      const newData = [...this.courseList];
      const target = newData.filter((item) => key === item.courseId)[0];
      this.editingKey = key;
      if (target) {
        target.editable = true;
        this.COURSE_INFO(newData);
      }
    },
    // 修改课程信息，上传
    save(key) {
      const newData = [...this.courseList];
      const target = newData.filter((item) => key === item.courseId)[0];
      let formData = new FormData();
      formData.append("courseId", target.courseId);
      formData.append("name", target.name);
      formData.append("credit", target.credit);
      // 发送更改数据
      this.$message.loading("Editing course information......", 0);
      let result = this.updateCourse(formData);
      result.then((value) => {
        const newCacheData = [...this.cacheData];
        const targetCache = newCacheData.filter(
          (item) => key === item.courseId
        )[0];
        this.COURSE_INFO(newData);
        this.DEL_EDITABLE(key);
        Object.assign(targetCache, target);
        this.cacheData = newCacheData;
        this.editingKey = "";
        this.$message.success("Modification succeeded!");
      });
    },
    // 取消编辑
    cancel(key) {
      const newData = [...this.courseList];
      const target = newData.filter((item) => key === item.courseId)[0];
      this.editingKey = "";
      if (target) {
        Object.assign(
          target,
          this.cacheData.filter((item) => key === item.courseId)[0]
        );
        this.COURSE_INFO(newData);
        this.DEL_EDITABLE(key);
      }
    },
    // 筛选课程功能
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
        formData.append("majorId", this.$route.query.majorId);
        formData.append("file", file);
        this.$message.loading("Adding Course......");
        let result = this.addCourses(formData);
        result.then((value) => {
          this.$message.success("Successfully added");
          this.getCourse(this.$route.query.majorId);
        });
      }
      return false;
    },
  },
};
</script>

<style>
#page-layout {
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
}
#add-course-layout {
  overflow: hidden;
  display: flex;
  padding: 8px;
  background: #fff;
  justify-content: space-around;
}
#add-course-input-name {
  margin-right: 1%;
  width: 43%;
}
#add-course-input-credit {
  margin-right: 1%;
  width: 25%;
}
#add-course-add {
  margin-left: 1%;
  width: 10%;
}
#add-course-clear {
  margin-left: 1%;
  width: 10%;
}
#add-course-file {
  margin-left: 1%;
  width: 7%;
}
</style>