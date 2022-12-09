<template>
  <div id="page-layout">
    <div id="major-title-layout">
      <b>Alliance Specialty and Course Management</b>
    </div>
    <!-- 界面主体框架 -->
    <div id="major-course-main-layout">
      <!-- 专业管理 -->
      <div id="major-layout">
        <div>
          <!-- 添加专业 -->
          <div id="add-major-layout">
            <a-select
              id="add-major-select"
              placeholder="Select Major Type"
              v-model="major.type"
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
            <a-input
              id="add-major-input"
              placeholder="Enter the Major Name"
              v-model="major.name"
            >
            </a-input>
            <a-button id="add-major-add" @click="handleAddMajor">
              Add
            </a-button>
            <a-button id="add-major-clear" @click="clear"> Clear </a-button>
            <a-tooltip placement="bottomRight" id="add-major-file">
              <template slot="title">
                <span>Batch add majors</span>
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
          <!-- 专业列表 -->
          <a-table
            class="major-table"
            bordered
            :data-source="majorList"
            :columns="majorColumns"
            :pagination="{ hideOnSinglePage: true, pageSize: 9 }"
            :rowKey="(record) => record.majorId"
            :currentRow="selectMajor"
            :customRow="onClickRow"
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
            <!-- 专业和删除 -->
            <template slot="name" slot-scope="text, record">
              <div style="position: relative" class="cell">
                {{ text || " " }}
                <a-popconfirm
                  v-if="majorList.length"
                  placement="topRight"
                  ok-text="Yes"
                  title="Are you sure to delete?"
                  @confirm="() => handleDelMajor(record.majorId)"
                >
                  <a-icon slot="icon" type="warning" style="color: red" />
                  <a-tooltip placement="bottomRight">
                    <template slot="title">
                      <span>Delete Major</span>
                    </template>
                    <a-icon type="delete" class="cell-icon" />
                  </a-tooltip>
                </a-popconfirm>
              </div>
            </template>
          </a-table>
        </div>
      </div>
      <!-- 课程管理 -->
      <div id="course-layout">
        <router-view v-if="majorList.length != 0 && destroy"></router-view>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
const majorColumns = [
  {
    title: "Major ID",
    dataIndex: "majorId",
    width: "25%",
    className: "major-row-layout",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "customRender",
    },
    onFilter: (value, record) =>
      record.majorId.toString().toLowerCase().includes(value.toLowerCase()),
    sorter: (a, b) => a.majorId - b.majorId,
    sortDirections: ["descend", "ascend"],
  },
  {
    title: "Type",
    dataIndex: "type",
    width: "20%",
    className: "major-row-layout",
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
    onFilter: (value, record) => record.type === value,
  },
  {
    title: "Major Name",
    dataIndex: "name",
    width: "55%",
    className: "major-row-layout",
    ellipsis: true,
    scopedSlots: {
      filterDropdown: "filterDropdown",
      filterIcon: "filterIcon",
      customRender: "name",
    },
    onFilter: (value, record) =>
      record.name.toString().toLowerCase().includes(value.toLowerCase()),
  },
];
export default {
  name: "Major",
  mounted() {
    this.$message.loading("Getting professional information......", 0);
    let result = this.getMajor();
    result.then((value) => {
      this.$message.success("Get succeeded!");
    });
    let that = this;
    this.$nextTick(function () {
      setTimeout(function () {
        if (
          that.$route.query.majorId != undefined &&
          that.majorList.length != 0
        ) {
          let majorObj = that.majorList.filter(
            (item) => that.$route.query.majorId === item.majorId
          )[0];
          that.renderRowStyle(majorObj);
          that.destroy = false;
          that.$nextTick(() => {
            that.destroy = true;
            that.$router.replace({
              name: "Course",
              query: {
                majorId: that.$route.query.majorId,
              },
            });
          });
        } else if (
          that.$route.query.majorId == undefined &&
          that.majorList.length != 0
        ) {
          that.renderRowStyle(that.majorList[0]);
          that.destroy = false;
          that.$nextTick(() => {
            that.destroy = true;
            that.$router.replace({
              name: "Course",
              query: {
                majorId: that.majorList[0].majorId,
              },
            });
          });
        }
      }, 200);
    });
  },
  data() {
    return {
      major: {
        type: undefined,
        name: undefined,
      },
      // flag
      selectMajor: {},
      destroy: true,
      // rule
      majorColumns,
      // search
      searchText: "",
      searchedColumn: "",
    };
  },
  computed: {
    ...mapState("user", ["userInfo"]),
    ...mapState("major", ["majorList"]),
  },
  watch: {
    // 监视修改行，调用插入样式方法
    selectMajor(val) {
      this.renderRowStyle(val);
    },
  },
  methods: {
    ...mapActions("major", {
      getMajor: "getMajor",
      addMajor: "addMajor",
      delMajor: "delMajor",
      addMajors: "addMajors",
    }),
    // 选中对应专业, 点击回调
    onClickRow(record) {
      return {
        on: {
          // 选择专业行，展示对应
          click: () => {
            this.selectMajor = record;
            this.destroy = false;
            this.$nextTick(() => {
              this.destroy = true;
              this.$router.replace({
                name: "Course",
                query: {
                  majorId: record.majorId,
                },
              });
            });
          },
        },
      };
    },
    // 选中对应专业，插入样式
    renderRowStyle(currentRow) {
      const rowBody = document.getElementsByClassName("major-table"),
        rowEles = rowBody[0].getElementsByClassName("ant-table-row"),
        rowSelectEles = document.getElementsByClassName("row-click");
      let rowList;
      if (rowSelectEles.length) {
        rowSelectEles[0].classList.remove("row-click");
      }
      if (rowEles.length) {
        rowList = [...rowEles];
        rowList
          .find((row) => row.dataset.rowKey == currentRow.majorId)
          .classList.add("row-click");
      }
    },
    // 增加专业
    handleAddMajor() {
      if (this.major.type != undefined && this.major.name.trim() != "") {
        const major = new FormData();
        major.append("majorType", this.major.type);
        major.append("name", this.major.name);
        let result = this.addMajor(major);
        this.$message.loading("Adding Major......");
        result.then((value) => {
          this.$message.success("Successfully added");
          this.clear();
        });
      }
    },
    // 删除专业
    handleDelMajor(majorId) {
      const major = new FormData();
      major.append("majorId", majorId);
      let result = this.delMajor(major);
      this.$message.loading("Deleting a major......");
      result.then((value) => {
        this.$message.success("Deletion succeeded");
        this.getMajor();
      });
    },
    // 清空专业添加
    clear() {
      this.major.type = undefined;
      this.major.name = undefined;
    },
    // 筛选专业功能
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
        formData.append("file", file);
        this.$message.loading("Adding Major......");
        let result = this.addMajors(formData);
        result.then((value) => {
          this.$message.success("Successfully added");
          this.getMajor();
        });
      }
      return false;
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
#major-title-layout {
  text-align: left !important;
  position: relative !important;
  width: 100%;
  min-width: 1200px;
  height: 55px;
  line-height: 55px;
  padding-left: 25px;
  text-align: center;
  border-bottom: 1px solid #e8e8e8;
  background: #fafafa;
}
#major-course-main-layout {
  display: -webkit-flex; /* Safari */
  display: flex;
  height: 645px;
  background: #fff;
  min-width: 1200px;
  overflow: hidden;
}
#major-layout {
  flex-grow: 1;
  width: 40%;
}
#add-major-layout {
  overflow: hidden;
  display: flex;
  padding: 8px;
  justify-content: space-around;
}
#add-major-select {
  margin-right: 1%;
  width: 29%;
}
#add-major-input {
  margin-right: 1%;
  width: 33%;
}
#add-major-add {
  margin-left: 1%;
  padding: 0;
  width: 12%;
}
#add-major-clear {
  margin-left: 1%;
  padding: 0;
  width: 12%;
}
#add-major-file {
  margin-left: 1%;
  width: 9%;
}
/deep/th.major-row-layout,
/deep/td.major-row-layout {
  cursor: pointer;
}
/deep/.row-click {
  background: #f5f5f5;
}
.cell-icon {
  position: absolute;
  right: 0;
  top: 5px;
  width: 20px;
  cursor: pointer;
  line-height: 18px;
  display: none;
}
.cell:hover .cell-icon {
  display: inline-block;
}
.cell-icon:hover {
  color: #108ee9;
}
#course-layout {
  flex-grow: 2;
  width: 60%;
  background: #fff;
  overflow-x: auto;
  overflow-y: hidden;
  height: 700px;
  border-left: 1px solid #e8e8e8;
}
</style>