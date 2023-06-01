<template>
  <div class="preview-vditor" v-loading="isLoading" element-loading-text="正在努力，请稍候...">
    <div id="khaleesi" class="vditor-preview" />
  </div>
</template>

<script>
import Vditor from 'vditor'

export default {
  name: 'PreviewVditor',

  data() {
    return {
      isLoading: true
    }
  },

  props: {
    pdata: {
      type: String,
      required: true,
      default: ''
    }
  },

  created() {
    // this.$utils.updateHtmlStyle()
    // this.setDefaultText()
  },

  components: {},

  mounted() {
    this.initVditor()
    // this.$utils.hideVditorTextarea()
  },

  methods: {
    initVditor() {
      const options = {
        width: '61.8%',
        mode: 'sv',
        value: '`# 欢迎使用 \\`SUSTech learning\\` 笔记共享系统\n' +
            '\n' +
            '\n' +
            '---\n' +
            '\n' +
            '## 如何使用\n' +
            '\n' +
            '**微注**：清空目前这份默认文档，即处于可使用态。\n' +
            '\n' +
            '默认为[所见即所得](https://hacpai.com/article/1577370404903?utm_source=github.com)模式，可通过 \\`⌘-⇧-M\\`（\\`Ctrl-⇧-M\\`）进行切换；或通过以下方式：\n' +
            '- 所见即所得：\\`⌘-⌥-7\\`（\\`Ctrl-alt-7\\`）；\n' +
            '- 即时渲染：\\`⌘-⌥-8\\`（\\`Ctrl-alt-8\\`）；\n' +
            '- 分屏渲染：\\`⌘-⌥-9\\`（\\`Ctrl-alt-9\\`）；\n' +
            '\n' +
            '### PPT 预览\n' +
            '\n' +
            '如果您用作 \\`PPT\\` 预览（入口在\\`设置\\`中），需要注意，这里暂还不能支持各种图表的渲染；您可以使用 \\`---\\` 来定义水平方向上幻灯片，用 \\`--\\` 来定义垂直幻灯片；更多设定可以参见 [RevealJs 文档](https://github.com/hakimel/reveal.js#table-of-contents)。\n' +
            '\n' +
            '---\n' +
            '\n' +
            '### 1. 制作待办事宜 \\`Todo\\` 列表\n' +
            '\n' +
            '- [x] 🎉 通常 \\`Markdown\\` 解析器自带的基本功能；\n' +
            '- [x] 🍀 支持**流程图**、**甘特图**、**时序图**、**任务列表**；\n' +
            '- [x] ✨ 升级 Vditor，新增对 \\`echarts\\` 图表的支持；\n' +
            '- [x] 💃🏻 支持插入原生 Emoji、设置常用表情列表；\n' +
            '- [x] 🚑 支持编辑内容保存**本地存储**，防止意外丢失；\n' +
            '- [x] 🌟 增加对**所见即所得**编辑模式的支持(\\`⌘-⇧-M\\`)；\n' +
            '- [ ] 🏁 支持粘贴 HTML 自动转换为 Markdown；\n' +
            '- [ ] 📝 支持**实时预览**，主窗口大小拖拽，字符计数；\n' +
            '- [ ] 🛠 支持常用快捷键(**Tab**)，及代码块添加复制\n' +
            '- [ ] ✨ 支持**导出**携带样式的 PDF、PNG、JPEG 等；\n' +
            '- [ ] 👏 支持检查并格式化 Markdown 语法，使其专业；\n' +
            '---\n' +
            '\n' +
            '### 2. 书写一个质能守恒公式[^LaTeX]\n' +
            '\n' +
            '$$\n' +
            'E=mc^2\n' +
            '$$\n' +
            '\n' +
            '---\n' +
            '\n' +
            '### 3. 高亮一段代码[^code]\n' +
            '\n' +
            '\\`\\`\\`js\n' +
            '// 给页面里所有的 DOM 元素添加一个 1px 的描边（outline）;\n' +
            '[].forEach.call($$("*"),function(a){\n' +
            '  a.style.outline="1px solid #"+(~~(Math.random()*(1<<24))).toString(16);\n' +
            '})\n' +
            '\\`\\`\\`\n' +
            '\n' +
            '### 4. 高效绘制[流程图](https://github.com/knsv/mermaid#flowchart)\n' +
            '\n' +
            '\\`\\`\\`mermaid\n' +
            'graph TD;\n' +
            '  A-->B;\n' +
            '  A-->C;\n' +
            '  B-->D;\n' +
            '  C-->D;\n' +
            '\\`\\`\\`\n' +
            '\n' +
            '### 5. 高效绘制[序列图](https://github.com/knsv/mermaid#sequence-diagram)\n' +
            '\n' +
            '\\`\\`\\`mermaid\n' +
            'sequenceDiagram\n' +
            '  participant Alice\n' +
            '  participant Bob\n' +
            '  Alice->John: Hello John, how are you?\n' +
            '  loop Healthcheck\n' +
            '      John->John: Fight against hypochondria\n' +
            '  end\n' +
            '  Note right of John: Rational thoughts <br/>prevail...\n' +
            '  John-->Alice: Great!\n' +
            '  John->Bob: How about you?\n' +
            '  Bob-->John: Jolly good!\n' +
            '\\`\\`\\`\n' +
            '\n' +
            '### 6. 高效绘制[甘特图](https://github.com/knsv/mermaid#gantt-diagram)\n' +
            '\n' +
            '>**甘特图**内在思想简单。基本是一条线条图，横轴表示时间，纵轴表示活动（项目），线条表示在整个期间上计划和实际的活动完成情况。它直观地表明任务计划在什么时候进行，及实际进展与计划要求的对比。\n' +
            '\n' +
            '\\`\\`\\`mermaid\n' +
            'gantt\n' +
            '  title 项目开发流程\n' +
            '  section 项目确定\n' +
            '    需求分析       :a1, 2019-06-22, 3d\n' +
            '    可行性报告     :after a1, 5d\n' +
            '    概念验证       : 5d\n' +
            '  section 项目实施\n' +
            '    概要设计      :2019-07-05  , 5d\n' +
            '    详细设计      :2019-07-08, 10d\n' +
            '    编码          :2019-07-15, 10d\n' +
            '    测试          :2019-07-22, 5d\n' +
            '  section 发布验收\n' +
            '    发布: 2d\n' +
            '    验收: 3d\n' +
            '\\`\\`\\`\n' +
            '\n' +
            '### 7. 支持图表\n' +
            '\n' +
            '\\`\\`\\`echarts\n' +
            '${echartsDemoJsonStr}\n' +
            '\\`\\`\\`\n' +
            '\n' +
            '>**备注**：上述 echarts 图表📈，其数据，须使用严格的 **JSON** 格式；您可使用 JSON.stringify(data)，将对象传换从而得标准数据，即可正常使用。\n' +
            '\n' +
            '### 8. 绘制表格\n' +
            '\n' +
            '| 作品名称        | 在线地址   |  上线日期  |\n' +
            '| :--------  | :-----  | :----:  |\n' +
            '| 软件工程 | [https://cs304.sustech.com](https://nicelinks.site/??utm_source=markdown.lovejade.cn) |2017-09-20|\n' +
            '| 操作系统 | [https://cs302.sustech.com](https://jeffjade.com/??utm_source=markdown.lovejade.cn) |2014-09-20|\n' +
            '| 数据挖掘 | [http://cs306.sustech.com](http://quickapp.lovejade.cn/??utm_source=markdown.lovejade.cn) |2019-01-12|\n' +
            '\n' +
            '\n' +
            '`',
        preview: {
          delay: 1000,
          show: true
        }
      }
      this.vditor = new Vditor('khaleesi', options)
      this.$nextTick(() => {
        this.isLoading = false
      })
    },

    setDefaultText() {
      localStorage.setItem('vditorkhaleesi', this.pdata)
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
  #khaleesi {
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

@media (max-width: 768px) {
  .preview-vditor {
    #khaleesi {
      width: 100% !important;
      margin: 0 !important;
    }
    .vditor-preview {
      padding: 0 10px;
    }
    .vditor-reset {
      table {
        display: inline-block;
        overflow-x: auto;
      }
    }
  }
}
</style>
