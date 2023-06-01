<!-- @format -->

<template>
  <div >
    <div >
      <el-button  @click="onBackToMainPage">返回主页</el-button>
      <el-button  @click="onExportBtnClick" type="primary">生成导出</el-button>
    </div>

    <PreviewVditor :pdata="pdata" />
  </div>
</template>

<script>
import html2pdf from 'html2pdf.js'
import PreviewVditor from '../EditVditor'

export default {
  name: 'export-pdf',

  data() {
    return {
      isLoading: true,
      pdata: localStorage.getItem('vditor')
    }
  },

  created() {},

  components: {
    PreviewVditor
  },

  mounted() {

  },

  methods: {
    exportAndDownloadPdf(element, filename) {
      const scale = window.devicePixelRatio
      console.log(element)
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
      this.$router.push('/')
    },
    onExportBtnClick() {
      this.isLoading = true
      const element = document.getElementsByClassName('vditor')[0]
      const filename = 'test'
      this.exportAndDownloadPdf(element, filename)
    }
  }
}
</script>
