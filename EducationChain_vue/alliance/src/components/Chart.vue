<template>
  <div style="width: 100%; height: 500px" ref="chart"></div>
</template>

<script>
let Echarts = require("echarts/lib/echarts");
require("echarts/lib/chart/bar"); //柱状图
require("echarts/lib/chart/line"); //折线图
require("echarts/lib/chart/pie"); //圆环图

import * as echarts from "echarts/lib/echarts";
import {
  GridComponent,
  TitleComponent,
  LegendComponent,
  TooltipComponent,
} from "echarts/components";
echarts.use([GridComponent, TitleComponent, LegendComponent, TooltipComponent]);

export default {
  name: "Chart",
  props: {
    xAxis: {
      type: Array,
    },
    yAxis: {
      type: Array,
    },
    series: {
      type: Array,
    },
    legend: {
      type: Array,
    },
    color: {
      type: Array,
      default: () => {},
    },
    inverseX: {
      type: Boolean,
      default: false,
    },
    inverseY: {
      type: Boolean,
      default: false,
    },
  },
  mounted() {
    setTimeout(() => {
      this.init();
    }, 1500);
    window.addEventListener("resize", () => {
      this.chart.resize();
    });
  },
  data() {
    return {
      chart: null,
    };
  },
  watch: {
    xAxis: {
      handler(newName, oldName) {
        // init();
      },
    },
    yAxis: {
      handler(newName, oldName) {
        // init();
      },
    },
    series: {
      handler(newName, oldName) {
        // init();
      },
    },
    immediate: true,
  },
  methods: {
    init() {
      // 初始化
      this.chart = Echarts.init(this.$refs.chart);
      // 配置数据
      let option = {
        color: this.color,
        // 提示信息
        tooltip: {},
        // X轴
        xAxis: {
          data: this.xAxis,
          inverse: this.inverseX,
        },
        // Y轴
        yAxis: {
          data: this.yAxis,
          inverse: this.inverseY,
        },
        // 数据
        series: this.series,
        // series: [
        //   {
        //     name: this.series,
        //     type: this.chartType,
        //     data: this.data,
        //   },
        // ],
        // 图例组件展现了不同系列的标记(symbol)，颜色和名字
        legend: {
          data: this.legend,
          // data: [
          //   {
          //     name: this.series,
          //   },
          // ],
        },
        // 标题 已停用
        // title: {
        //   text: "chart title",
        // },
      };
      // 传入数据
      this.chart.setOption(option);
    },
  },
};
</script>

<style scoped>
.color {
  /* background: #ff7f50, 橙*/
  /* background: #87cefa, 蓝*/
  /* background: #da70d6, 紫*/
  /* background: #32cd32, 绿*/
  /* background: #6495ed, 蓝*/
  /* background: #ff69b4, 粉*/
  /* background: #ba55d3, 紫*/
  /* background: #cd5c5c, 褐*/
  /* background: #ffa500, 橙*/
  /* background: #40e0d0, 青*/
  /* background: #1e90ff, 蓝*/
  /* background: #ff6347, 红*/
  /* background: #7b68ee, 蓝*/
  /* background: #00fa9a, 绿*/
  /* background: #ffd700, 黄*/
  /* background: #6699FF, 蓝*/
  /* background: #ff6666, 红*/
  /* background: #3cb371, 绿*/
  /* background: #b8860b, 橙*/
  /* background: #30e0e0, 青*/
}
</style>