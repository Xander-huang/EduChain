<template>
  <div>
    <a-button type="primary" block @click="agree()"> agree </a-button>
    <input
      type="file"
      ref="clearFile"
      id="filePath"
      name="file"
      @change="getFile($event)"
      multiple="multiplt"
      class="add-file-right-input"
      style="margin-left: 70px"
    />
    <a-button type="danger" block @click="disagree()"> disagree </a-button>
    <a-button type="link" block> Link </a-button>
  </div>
</template>

<script>
import { AgreeAuthorization, DisagreeGetAuthorization } from "../api";
import axios from "axios";
export default {
  data() {
    return {
      id: "",
      fileObj: "",
    };
  },
  created() {
    // this.id = this.$route.params.id
    console.log(this.$route.params.id);
    this.id = this.$route.params.id;
  },
  methods: {
    getFile(obj) {
      this.fileObj = document.getElementById("filePath").files[0];
      console.log(this.fileObj);
    //   console.log(document.getElementById("filePath").files);
    //   this.filePath = this.fileObj.path;
    //   this.fileName = this.fileObj.name;
    //   console.log(this.filePath);
    //   console.log(this.fileName);
     
    },
    agree() {
      let formData = new FormData()
    formData.append('file',this.fileObj)
        console.log(formData.get('file'))
      AgreeAuthorization(this.id, formData)
        .then((res) => {
          console.log(res)
        })
        .catch((error) => {
          console.log(error)
        });
    },
    disagree() {
    //   DisagreeGetAuthorization(this.id);
    },
  },
};
// let id = this.$route.params.id
</script>

<style scoped>
#app-layout {
  text-align: center;
}
</style>
