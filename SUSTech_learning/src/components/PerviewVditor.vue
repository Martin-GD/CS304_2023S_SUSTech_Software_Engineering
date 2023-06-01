<template>
  <div class="preview-vditor" v-loading="isLoading" element-loading-text="正在努力，请稍候...">
    <div id="previewV" class="vditor-preview" />
  </div>
</template>

<script>
import Vditor from 'vditor'
export default {
  name: 'PreviewVditor',

  data() {
    return {
      isLoading: true,
      vditor: null
    }
  },

  props: {
    pdata: {
      type: String,
      required: true,
      default: 'preview vditor'
    }
  },

  created() {
    // this.$utils.updateHtmlStyle()
    const htmlNode = document.querySelector('html')
    htmlNode.style.overflow = 'auto'
    htmlNode.style.height = 'auto'
    // this.setDefaultText()
  },

  components: {

  },

  mounted() {
    this.initVditor()
    // const exportVditorNode = document.getElementById('khaleesi')
    // const option = {
    //   childList: true, // 子节点的变动（新增、删除或者更改）
    //   attributes: true, // 属性的变动
    //   characterData: true, // 节点内容或节点文本的变动
    //
    //   subtree: true, // 是否将观察器应用于该节点的所有后代节点
    //   attributeFilter: ['class', 'style'], // 观察特定属性
    //   attributeOldValue: true, // 观察 attributes 变动时，是否需要记录变动前的属性值
    //   characterDataOldValue: true // 观察 characterData 变动，是否需要记录变动前的值
    // }
    // const mutationObserver = new MutationObserver(() => {
    //   const vditorTextarea = document.getElementsByClassName('vditor-textarea')
    //   if (vditorTextarea && vditorTextarea[0]) {
    //     vditorTextarea[0].style.display = 'none'
    //     mutationObserver.disconnect()
    //   }
    // })
    // mutationObserver.observe(exportVditorNode, option)
    // this.$utils.hideVditorTextarea()
  },

  methods: {
    initVditor() {
      const options = {
        width: '61.8%',
        // mode: 'sv',
        value: '3333',
        preview: {
          // 开启预览功能
          show: true,
          // 将预览内容渲染到名为 "khaleesi" 的 div 元素中
          // element: document.getElementById('previewV')
        }
      }
      this.vditor = new Vditor('previewV', options)
      this.$nextTick(() => {
        this.isLoading = false
        // this.vditor.setValue('这是新的内容');
      })
    },

    setDefaultText() {
      // localStorage.setItem('previewV', this.pdata)
      this.$nextTick(() => {
        if (this.vditor) {
          this.vditor.setValue('这是新的内容');
        }else {
          console.log('null vditor')
        }
      });
      // if(this.vditor != null){
      //   this.vditor.setValue('this.pdata')
      // }else {
      //   console.log('no init vditor')
      // }
    }
  }
}
</script>


<style lang="less">
@import './../assets/styles/style.less';

.preview-vditor {
  width: 100%;
  height: 100%;
  min-height: 100vh;
  background-color: @white;
  .flex-box-center(column);
  #previewV {
    max-width: 960px;
    height: 100%;
    min-height: 100vh;
    margin: 20px auto;
    text-align: left;
    .vditor-toolbar {
      display: none;
    }
    .vditor-content {
      .vditor-sv {
        display: none !important;
      }
    }
    .vditor-preview {
      padding: 0 20px;
      box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.1);
      .vditor-preview__action {
        display: none;
      }
      .vditor-reset {
        h1 {
          text-align: center;
        }
      }
    }
  }
  .vditor {
    border: 0;
  }
}

</style>
