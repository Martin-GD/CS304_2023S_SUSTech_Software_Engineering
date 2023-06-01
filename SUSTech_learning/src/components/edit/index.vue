<template>
  <div>
    <input type="file" ref="fileInput" v-show="isShow" @change="readFile">
    <input type="file" ref="imageInput" v-show="isShow" @change="uploadImage" accept="image/*">
  </div>
  <div id="vditor"></div>
</template>
<script>
import Vditor from "vditor"
// import { Message } from "element-ui";

import "vditor/dist/index.css"
import axios from "axios";
import request from '@/utils/request';
import router from "@/router";
import {BACKEND_URL} from "@/utils/config";

export default {
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      required: false,
      default () {
        return 'markdown-editor-' + +new Date() + ((Math.random() * 1000).toFixed(0) + '')
      }
    },
    isHideTools: {
      type: Boolean,
      required: false,
      default: false
    },
    isPin: {
      type: Boolean,
      required: false,
      default: true
    },
    height: {
      type: String,
      required: false,
      default: '300px'
    },
    width: {
      type: String,
      required: false,
      default: "auto"
    },
    vidtorMode: {
      type: String,
      required: true,
      default: 'ir'
    },
    titleID: {
      type: String,
      required: false,
      default: ''
    },
    autoSaveFile: {
      type: Boolean,
      required: false,
      default: false
    }
  },

  data () {
    return {
      contentEditor: "",
      isShow: false
    }
  },
  //mounted
  mounted () {
    console.log(this.vidtorMode)
    console.log(this.isPin)
    console.log(this.width)
    // alert(this.mode)
    const that = this
    this.contentEditor = new Vditor("vditor", {
      height: this.height,
      width: this.width,
      toolbarConfig: {
        pin: this.isPin,
        hide: this.isHideTools,
      },
      mode: this.vidtorMode,
      cache: {
        enable: false
      },
      // toolbar: [],
      toolbar: [
        "emoji",
        "headings",
        "bold",
        "italic",
        "strike",
        "link",
        "|",
        "list",
        "ordered-list",
        "check",
        "outdent",
        "indent",
        "|",
        "quote",
        "line",
        "code",
        "inline-code",
        "insert-before",
        "insert-after",
        "|",
        //"upload",
        // "record",
        { //自定义上传
          hotkey: "",
          name: "upload_img",
          icon: `<img style="height: 16px" src='https://img.58cdn.com.cn/escstatic/docs/imgUpload/idocs/publish.svg'/>`,
          tip: "上传图片",
          className: "img_up",
          click() {
            that.chooseImage();
          }
        },
        {
          hotkey: "",
          name: "load_file",
          tip: "载入文件",
          className: "right",
          icon: `<img style="height: 16px" src='https://img.58cdn.com.cn/escstatic/docs/imgUpload/idocs/publish.svg'/>`,
          click() {
            that.showFileInput();
          }
        },

        "table",
        "|",
        "undo",
        "redo",
        "|",
        // "fullscreen",
        "edit-mode",
        {
          name: "more",
          toolbar: [
            //"both",
            "code-theme",
            "content-theme",
            "export",
            "outline",
            "preview",
            "devtools",
            "info",
            "help",
          ],
        },
      ],
      after: () => {
        this.contentEditor.setValue(this.value)
      },
      blur: () => {
        if (this.autoSaveFile){
          this.autoSave();
        }
        // console.log('blur')
      },

      preview: {
        mode: "both",
        actions: []
      },


      // 这里写上传
      // upload: {
      //   accept: 'image/jpg, image/jpeg, image/png',//规定上传的图片格式
      //   // url: "/api/admin/uploadFile?type=99",//请求的接口
      //   multiple: false,
      //
      //   // fieldName: 'file',
      //   // max: 2 * 1024 * 1024,//上传图片的大小
      //   // extraData: { 'access_token': this.token }, //为 FormData 添加额外的参数
      //   // linkToImgUrl: "/api/admin/uploadFile?type=99",
      //   // filename (name) {
      //   //   return name.replace(/[^(a-zA-Z0-9\u4e00-\u9fa5\.)]/g, "")
      //   //       .replace(/[\?\\/:|<>\*\[\]\(\)\$%\{\}@~]/g, "")
      //   //       .replace("/\\s/g", "");
      //   // },
      //   validate (files) {
      //     const isLt2M = files[0].size / 1024 / 1024 < 2;
      //     alert("jjjj")
      //     // if (!isLt2M) {
      //     //   Message({
      //     //     message: "上传图片大小不能超过 10MB!",
      //     //     type: 'error',
      //     //   })
      //     //   //this.$message.error('')
      //     // }
      //   },
      //   //上传图片回显处理
      //   // format (files, responseText) {
      //   //   var self = this;
      //   //   var data = JSON.parse(responseText)
      //   //   //上传图片请求状态
      //   //   if (data.status) {
      //   //     let filName = data.msg
      //   //     let lastTipNum = filName.substr(filName.lastIndexOf('/', filName.lastIndexOf('/') - 1) + 1);
      //   //     console.log("lastTipNum", lastTipNum)
      //   //     let index = lastTipNum.lastIndexOf("\/");
      //   //     let succ = {}
      //   //     succ[filName] = "/api" + data.data
      //   //     //图片回显
      //   //     return JSON.stringify({
      //   //       data,
      //   //       data,
      //   //       data: {
      //   //         errFiles: [],
      //   //         succMap: succ
      //   //       }
      //   //     })
      //   //   } else {
      //   //     // Message({
      //   //     //   message: "图片上传失败",
      //   //     //   type: 'error',
      //   //     // })
      //   //   }
      //   //
      //   // },
      //   error (msg) {
      //     console.log(msg + "上传失败了")
      //   },
      // }
    })


  },
  methods: {
    getValue () {
      return this.contentEditor.getValue();     //获取 Markdown 内容
    },
    getHTML () {
      return this.contentEditor.getHTML();      //获取 HTML 内容
    },
    setValue (value) {
      return this.contentEditor.setValue(value);     //设置 Markdown 内容
    },
    disabled () {
      return this.contentEditor.disabled();     //设置 只读
    },
    toPreview () {
      var evt = document.createEvent('Event');
      evt.initEvent('click', true, true);
      this.contentEditor.vditor.toolbar.elements.preview.firstElementChild.dispatchEvent(evt);
    },
    autoSave() {
      var content = this.getValue();
      // console.log(content)
      // axios.post('http://10.26.11.88:8080/updateNoteContent')
      // axios.post(this.titleID,content)
      var formData = new FormData();
      formData.append('note', this.titleID);
      formData.append('content', this.getValue());
      formData.append('token', localStorage.getItem('token'));
      request.post(BACKEND_URL+"/updateNoteContent", formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }).then(resp=>{
        console.log("auto save success")
      })
    },
    showFileInput() {
      // this.isShow = true;
      this.$nextTick(() => {
        this.$refs.fileInput.click();
      });
    },
    readFile(event) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        const fileContent = reader.result;
        console.log(fileContent)
        this.setValue(fileContent)
        // 处理文件内容
      };
      reader.readAsText(file);
    },
    chooseImage() {
      this.$nextTick(() => {
        // alert("jjjj")
        this.$refs.imageInput.click();
      });
    },
    uploadImage(event){
      const file = event.target.files[0];
      const fileName = file.name;
      const formData = new FormData();
      formData.append('note', this.titleID)
      formData.append('imageName', fileName)
      formData.append('image', file);
      formData.append('token', localStorage.getItem('token'));
      request.post(BACKEND_URL+'/uploadNoteImage', formData)
          .then(response => {
            // 处理上传成功的响应
            alert("you can use this url to get image: "+response.data.data)
            // console.log(response.data.data)
            console.log('上传成功');
          })
          .catch(error => {
            // 处理上传失败的错误
            console.error('上传失败', error);
          });
    }

  },

}
</script>
