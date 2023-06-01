<!-- @format -->

<template>
  <div class="export-page">
<!--    <div class="button-group">-->
      <el-button round @click="onBackToMainPage">返回主页</el-button>
<!--      <el-button round @click="onExportBtnClick" type="primary">生成导出</el-button>-->
<!--    </div>-->
<!--    <PreviewVditor :pdata="pdata" />-->

    <div>
      <input type="file" ref="fileInput" v-show="isShow" @change="readFile">
      <button @click="showFileInput">选择文件</button>
    </div>
    <Markdown ref="markdownEditor" :value="pdata" height="90%" width="80%" :isPin=true :isHideTools=true vidtorMode="sv" />
  </div>
</template>

<script>
import html2pdf from 'html2pdf.js'
import PreviewVditor from './PreviewVditor'
import Markdown from './edit/index'
import defaultText from '../config/default'


export default {
  name: 'export-pdf',

  data() {
    return {
      isLoading: true,
      pdata: defaultText,
      isShow: false,
    }
  },

  created() {
  },

  components: {
    PreviewVditor,
    Markdown
  },

  mounted() {
    setTimeout(() => {
      // 进行需要的操作
      this.$refs.markdownEditor.toPreview()

    }, 100); // 等待1秒

  },

  methods: {
    exportAndDownloadPdf(element, filename) {
      const scale = window.devicePixelRatio
      console.log(this.pdata)

      // console.log(element)
      const opt = {
        margin: 1,
        filename: filename,
        html2canvas: { scale },
        jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
      }
      html2pdf()
          .set(opt)
          .from(element)
          .save()
          .then(() => {
            this.isLoading = false
          })
    },
    /* ---------------------Callback Event--------------------- */
    onBackToMainPage() {
      // this.$router.push('/')
      // this.pdata = 'changed'
      // this.$refs.markdownEditor.setValue('changed')
      // this.$refs.markdownEditor.setValue('hello')
      // this.$refs.markdownEditor.disabled()
      this.$refs.markdownEditor.toPreview()

    },
    onExportBtnClick() {
      this.isLoading = true
      const element = document.getElementsByClassName('vditor-preview')[0]
      const filename = this.$utils.getExportFileName()
      this.exportAndDownloadPdf(element, filename)
    },
    showFileInput() {
      this.isShow = true;
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
        // 处理文件内容
      };
      reader.readAsText(file);
    },
  }
}
</script>
