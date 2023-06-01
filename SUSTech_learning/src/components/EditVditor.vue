<template>
  <div id="vditor" ></div>
</template>
<script>
import Vditor from "vditor";  //1.import一下vditor组件
import 'vditor/dist/index.css';
import router from "@/router"; //1.import一下vditor组件样式
export default {
  data() {
    return {
      contentEditor: {}, //3.声明一个变量

    }
  },
  props: {
    pdata: {
      type: String,
      required: true,
      default: 'edit'
    }
  },
  created() {
  },
  mounted() {
    let id = this.$route.query.id;

    console.log(this.$route.params)
    this.contentEditor = new Vditor('vditor', { //4.刚刚声明的变量contentEditor被赋值为一个Vditor实例,
      height: 850,
      placeholder: '欢迎使用Atom...',
      value: '# vditor text',
      theme: 'classic',
      counter: {
        enable: true,
        type: 'markdown'
      },
      preview: {
        delay: 1000,
        hljs: {
          style: 'monokai',
          lineNumber: true
        },
        show: false
      },
      tab: '\t',
      typewriterMode: true,
      toolbarConfig: {
        pin: true
      },
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
        "upload",
        "record",
        "table",
        "|",
        "undo",
        "redo",
        "|",
        "fullscreen",
        "edit-mode",

        {
          hotkey: "",
          name: "exportPDF",
          tipPosition: "s",
          tip: "导出PDF",
          className: "right",
          icon: `<img style="height: 16px" src='https://img.58cdn.com.cn/escstatic/docs/imgUpload/idocs/publish.svg'/>`,
          click() {
            router.push('/exportPDF')
            // that.publishDoc();
          }
        },
        {
          name: "more",
          toolbar: [
            "both",
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

      cache: {
        enable: false
      },
      mode: 'ir',
      outline: true,
      upload: {
        max: 5 * 1024 * 1024,
        // linkToImgUrl: 'https://sm.ms/api/upload',
        handler(file) {
          let formData = new FormData()
          for (let i in file) {
            formData.append('smfile', file[i])
          }
          let request = new XMLHttpRequest()
          request.open('POST', 'https://sm.ms/api/upload')
          request.onload = that.onloadCallback
          request.send(formData)
        },
      }
    })
    this.setDefaultText()
  },

  methods: {
    setDefaultText() {
      localStorage.setItem('editV', this.pdata)
    }
  }
}
</script>
<!--<template>-->
<!--  <div class="preview-vditor" v-loading="isLoading" element-loading-text="正在努力，请稍候...">-->
<!--    <div id="vditor"  />-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import Vditor from 'vditor'-->

<!--export default {-->
<!--  name: 'PreviewVditor',-->

<!--  data() {-->
<!--    return {-->
<!--      isLoading: true-->
<!--    }-->
<!--  },-->

<!--  props: {-->
<!--    pdata: {-->
<!--      type: String,-->
<!--      required: true,-->
<!--      default: ''-->
<!--    }-->
<!--  },-->

<!--  created() {-->
<!--    // this.myUtils.updateHtmlStyle()-->
<!--    const htmlNode = document.querySelector('html')-->
<!--    htmlNode.style.overflow = 'auto'-->
<!--    htmlNode.style.height = 'auto'-->
<!--    this.setDefaultText()-->
<!--  },-->

<!--  components: {},-->

<!--  mounted() {-->
<!--    this.initVditor()-->
<!--    // this.myUtils.hideVditorTextarea()-->
<!--    const exportVditorNode = document.getElementById('khaleesi')-->
<!--    const option = {-->
<!--      childList: true, // 子节点的变动（新增、删除或者更改）-->
<!--      attributes: true, // 属性的变动-->
<!--      characterData: true, // 节点内容或节点文本的变动-->

<!--      subtree: true, // 是否将观察器应用于该节点的所有后代节点-->
<!--      attributeFilter: ['class', 'style'], // 观察特定属性-->
<!--      attributeOldValue: true, // 观察 attributes 变动时，是否需要记录变动前的属性值-->
<!--      characterDataOldValue: true // 观察 characterData 变动，是否需要记录变动前的值-->
<!--    }-->
<!--    const mutationObserver = new MutationObserver(() => {-->
<!--      const vditorTextarea = document.getElementsByClassName('vditor-textarea')-->
<!--      if (vditorTextarea && vditorTextarea[0]) {-->
<!--        vditorTextarea[0].style.display = 'none'-->
<!--        mutationObserver.disconnect()-->
<!--      }-->
<!--    })-->
<!--    mutationObserver.observe(exportVditorNode, option)-->
<!--  },-->

<!--  methods: {-->
<!--    initVditor() {-->
<!--      const options = {-->
<!--        width: '61.8%',-->
<!--        mode: 'sv',-->
<!--        preview: {-->
<!--          delay: 1000,-->
<!--          show: true-->
<!--        }-->
<!--      }-->
<!--      this.vditor = new Vditor('khaleesi', options)-->
<!--      this.contentEditor = new Vditor('vditor', { //4.刚刚声明的变量contentEditor被赋值为一个Vditor实例,-->
<!--        height: 850,-->
<!--        placeholder: '欢迎使用Atom...',-->
<!--        value: 'defaultText',-->
<!--        theme: 'classic',-->
<!--        counter: {-->
<!--          enable: true,-->
<!--          type: 'markdown'-->
<!--        },-->
<!--        preview: {-->
<!--          delay: 0,-->
<!--          hljs: {-->
<!--            style: 'monokai',-->
<!--            lineNumber: true-->
<!--          }-->
<!--        },-->
<!--        tab: '\t',-->
<!--        typewriterMode: true,-->
<!--        toolbarConfig: {-->
<!--          pin: true-->
<!--        },-->
<!--        toolbar: [-->
<!--          "emoji",-->
<!--          "headings",-->
<!--          "bold",-->
<!--          "italic",-->
<!--          "strike",-->
<!--          "link",-->
<!--          "|",-->
<!--          "list",-->
<!--          "ordered-list",-->
<!--          "check",-->
<!--          "outdent",-->
<!--          "indent",-->
<!--          "|",-->
<!--          "quote",-->
<!--          "line",-->
<!--          "code",-->
<!--          "inline-code",-->
<!--          "insert-before",-->
<!--          "insert-after",-->
<!--          "|",-->
<!--          "upload",-->
<!--          "record",-->
<!--          "table",-->
<!--          "|",-->
<!--          "undo",-->
<!--          "redo",-->
<!--          "|",-->
<!--          "fullscreen",-->
<!--          "edit-mode",-->

<!--          // {-->
<!--          //   hotkey: "⌘s",-->
<!--          //   name: "save",-->
<!--          //   tipPosition: "s",-->
<!--          //   tip: "保存",-->
<!--          //   className: "right",-->
<!--          //   icon: `<img style="height: 16px" src='https://img.58cdn.com.cn/escstatic/docs/imgUpload/idocs/save.svg'/>`,-->
<!--          //   click() {-->
<!--          //     that.saveDoc();-->
<!--          //   }-->
<!--          // },-->
<!--          {-->
<!--            hotkey: "",-->
<!--            name: "exportPDF",-->
<!--            tipPosition: "s",-->
<!--            tip: "导出PDF",-->
<!--            className: "right",-->
<!--            icon: `<img style="height: 16px" src='https://img.58cdn.com.cn/escstatic/docs/imgUpload/idocs/publish.svg'/>`,-->
<!--            click() {-->
<!--              router.push('/edit/exportPDF')-->
<!--              that.publishDoc();-->
<!--            }-->
<!--          },-->
<!--          {-->
<!--            name: "more",-->
<!--            toolbar: [-->
<!--              "both",-->
<!--              "code-theme",-->
<!--              "content-theme",-->
<!--              "export",-->
<!--              "outline",-->
<!--              "preview",-->
<!--              "devtools",-->
<!--              "info",-->
<!--              "help",-->
<!--            ],-->
<!--          },-->
<!--        ],-->

<!--        cache: {-->
<!--          enable: false-->
<!--        },-->
<!--        mode: 'sv',-->
<!--        outline: true,-->
<!--        upload: {-->
<!--          max: 5 * 1024 * 1024,-->
<!--          // linkToImgUrl: 'https://sm.ms/api/upload',-->
<!--          handler(file) {-->
<!--            let formData = new FormData()-->
<!--            for (let i in file) {-->
<!--              formData.append('smfile', file[i])-->
<!--            }-->
<!--            let request = new XMLHttpRequest()-->
<!--            request.open('POST', 'https://sm.ms/api/upload')-->
<!--            request.onload = that.onloadCallback-->
<!--            request.send(formData)-->
<!--          },-->
<!--        }-->
<!--      })-->
<!--      this.$nextTick(() => {-->
<!--        this.isLoading = false-->
<!--      })-->
<!--    },-->

<!--    setDefaultText() {-->
<!--      localStorage.setItem('vditorkhaleesi', this.pdata)-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->

<!--<style lang="less">-->
<!--@import './../assets/styles/style.less';-->

<!--.preview-vditor {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  min-height: 100vh;-->
<!--  background-color: @white;-->
<!--  .flex-box-center(column);-->
<!--  #khaleesi {-->
<!--    max-width: 960px;-->
<!--    height: 100%;-->
<!--    min-height: 100vh;-->
<!--    margin: 20px auto;-->
<!--    text-align: left;-->
<!--    .vditor-toolbar {-->
<!--      display: none;-->
<!--    }-->
<!--    .vditor-content {-->
<!--      .vditor-sv {-->
<!--        display: none !important;-->
<!--      }-->
<!--    }-->
<!--    .vditor-preview {-->
<!--      padding: 0 20px;-->
<!--      box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.1);-->
<!--      .vditor-preview__action {-->
<!--        display: none;-->
<!--      }-->
<!--      .vditor-reset {-->
<!--        h1 {-->
<!--          text-align: center;-->
<!--        }-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--  .vditor {-->
<!--    border: 0;-->
<!--  }-->
<!--}-->

<!--@media (max-width: 768px) {-->
<!--  .preview-vditor {-->
<!--    #khaleesi {-->
<!--      width: 100% !important;-->
<!--      margin: 0 !important;-->
<!--    }-->
<!--    .vditor-preview {-->
<!--      padding: 0 10px;-->
<!--    }-->
<!--    .vditor-reset {-->
<!--      table {-->
<!--        display: inline-block;-->
<!--        overflow-x: auto;-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</style>-->
