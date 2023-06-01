<template>
  <div class="page">
    <div class="leftpanel">
      <leftpanel></leftpanel>

    </div>
    <div class="rightpanel" >
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <div>
          <div style="margin-left: 10px;" >
            <font style="font-size: 35px; font-weight:550; color:#0969da;">{{noteInfo.title}}</font>
            <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue; color: black; border-color: rgb(220, 215, 215);">{{noteInfo.auth}}</t-tag>
            <font style="margin-left: 10px; color: rgb(97, 95, 95); font-size: smaller;">Update on {{noteInfo.updateTime}}</font>

            <t-button size="small" variant="base" theme="default" style="margin-left: 40px" @click="starNote" >
              <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="isStar" />
              <StarIcon v-else/>
              Star
            </t-button>
          </div>
<!--          <br>-->
<!--          <div style="margin-left: 10px;">-->
<!--            <t-button size="small" variant="base" theme="default" style="float: left; margin-top: 0px;">-->
<!--              <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="true" />-->
<!--              <StarIcon v-else/>-->
<!--              Star-->
<!--            </t-button>-->
<!--&lt;!&ndash;            <br>&ndash;&gt;-->
<!--          </div>-->
<!--          <br>-->

<!--          <div style="margin-left: 10px;">-->
<!--            <t-tag style="background-color: rgb(235, 230, 230); border-color: rgb(186, 181, 181);">-->
<!--              <span class="repo-language-color" style="background-color: #f1e05a; margin-right:2px"></span>-->
<!--              <font>{{"science"}}</font>-->
<!--            </t-tag>-->
<!--          </div>-->
        </div>
      </div>
      <br>
      <div class="test">
        <div class="markdownview" >
          <!--        <Markdown ref="markdownEditor" :value="editorContent" height="900px" width="80%" :isPin=true :isHideTools=false vidtorMode="sv" v-show="isLoading" />-->
          <Markdown ref="markdownEditor"  :value="editorContent"  :isHideTools=false :isPin=true vidtorMode="ir"  height="900px" width="80%" :titleID="titleID" v-show="isLoading"  />
        </div>
      </div>
    </div>
  </div>


</template>

<script>

import axios from 'axios';
import leftpanel from '@/pages/home/leftpanel.vue';
import vditor from '@/components/EditVditor.vue';
import Markdown from '@/components/edit/index'
import defaltText from "@/config/default";
import request from "@/utils/request";
import {RootListIcon, HomeIcon, StarIcon, StarFilledIcon } from 'tdesign-icons-vue-next';
import {BACKEND_URL} from "@/utils/config";


export default {
  components:{
    leftpanel,
    vditor,
    Markdown,
    StarIcon ,
    StarFilledIcon,
  },
  data: function(){
    return {
      isLoading: false,
      isStar: false,
      editorContent: defaltText,
      markdownContent: "",
      titleID: '',
      noteInfo: {
        title: '',
        auth: '',
        updateTime: '',

      }
    }
  },
  mounted() {
    this.titleID = this.$route.query.id;
    // this.editorContent = defaltText;
    this.getNoteInfo();
    // this.checkIsStar();
    this.getContent();
    setTimeout(() => {
      // 进行需要的操作
      this.$refs.markdownEditor.setValue(this.editorContent)
      this.$refs.markdownEditor.toPreview()
      this.isLoading = true

    }, 500); // 等待1秒

  },
  methods: {

    getNoteInfo(){
      const _this = this
      // TODO:
      var token = localStorage.getItem('token')
      // var token = localStorage.getItem('token')
      // const formData = new FormData();
      // formData.append('noteId', this.titleID)
      request.get(BACKEND_URL+`/getNote?noteId=${this.titleID}&token=${token}`).then(resp => {
        _this.noteInfo.title = resp.data.data.note.name;
        _this.noteInfo.auth = resp.data.data.note.owner;
        // _this.noteInfo.updateTime = resp.data.data.note.updatedAt;
        let timestamp = resp.data.data.note.updatedAt; // 替换为您的时间戳
// 使用Date对象将时间戳转换为日期对象
        let date = new Date(timestamp);
// 使用Date对象的方法获取年、月、日、时等信息
        let year = date.getFullYear();
        let month = ('0' + (date.getMonth() + 1)).slice(-2); // 月份从0开始，需要加1，并且补零
        let day = ('0' + date.getDate()).slice(-2); // 补零
        let hour = ('0' + date.getHours()).slice(-2); // 补零
        let minute = ('0' + date.getMinutes()).slice(-2); // 补零
        let second = ('0' + date.getSeconds()).slice(-2); // 补零

// 拼接成年月日时分秒格式的字符串
        let formattedDate = year + '-' + month + '-' + day + ' ' + hour + ':' + minute + ':' + second;

        _this.noteInfo.updateTime = formattedDate;
        _this.isStar = resp.data.data.star
        console.log("star: "+resp.data.data.star)
        // _this.auth = resp.data.data
      }).then(() => {
      })
    },
    checkIsStar(){
      const _this = this
      const formData = new FormData();
      formData.append('user', localStorage.getItem('userid'))
      formData.append('note', this.titleID)
      // todo
      request.get(BACKEND_URL+`/check?note=${this.titleID}`).then(resp => {
        _this.isStar = resp.data.data
      }).then(() => {
      })
    },
    starNote(){
      // console.log("you click star button: "+this.isStar);
      if (this.isStar){
        this.isStar = false;
      }else {
        this.isStar = true;
      }
      // this.isStar = ~this.isStar;
      console.log("you click star button: "+this.isStar);
      const formData = new FormData();
      formData.append('note', this.titleID)
      // TODO
      formData.append('token', localStorage.getItem('token'))
      console.log(localStorage.getItem('token'))
      // formData.append('token', localStorage.getItem('token'))
      // alert(localStorage.getItem('userid'))
      // formData.append('isStar', this.isStar)

      if(this.isStar){
        request.post(BACKEND_URL+'/starNote', formData)
            .then(response => {
            })
            .catch(error => {
            });
      }else {
        request.post(BACKEND_URL+'/unstarNote', formData)
            .then(response => {
            })
            .catch(error => {
            });
      }

    },
    getContent(){
      const _this = this
      request.get(BACKEND_URL+`/getNoteContent?note=${this.titleID}`).then(resp => {
        _this.editorContent = resp.data.data
        // console.log('content below')
        // console.log(resp.data)
      }).then(() => {

      })
    }

  }
}
</script>

<style scoped>

.page {
  display: flex;
  flex-direction: row;
  height: 100%;
  align-content: space-around;

}

.leftpanel {
  width: 300px;
  height: 100%;
  margin-top: 10px;
  margin-right: 5px;
  margin-left: 5px;
}

.username {
  display: grid;
}

.repo-item {
  display: flex;
  grid-template-columns: 1fr 1fr;
}

.rightpanel {
  width: 80%;
  height: 100%;
  /*background-color: #fff;*/
  overflow: hidden;
  margin-top: 20px;
  margin-right: 5px;
  margin-left: 5px;
}

.test {
  /*width: 80%;*/
  /*height: 100%;*/
  /*background-color: #fff;*/
  overflow: hidden;
  margin-top: 0px;
  margin-right: 5px;
  margin-left: 5px;
}

.markdownview {
  width: 100%;
  height: 100%;
  /*background-color: #fff;*/
  overflow: hidden;
  margin-top: -40px;
  margin-right: 5px;
  margin-left: 5px;
}

.card-demo {
  /*width: 360px;*/
  margin-left: 50px;
  transition-property: all;
}

.card-demo:hover {
  transform: translateY(-5px);
}



@media screen and (max-width: 1200px) {
  .rightpanel {
    display: none;
  }

  .centerpanel {
    width: 80%;
    height: 100%;
    /*background-color: #fff;*/
    margin-top: 10px;
    margin-right: 10px;
    margin-left: 5px;
  }
}

@media screen and (max-width: 900px) {

  .leftpanel {
    display: none;
  }
  .rightpanel {
    display: none;
  }

  .centerpanel {
    width: 100%;
    height: 100%;
    /*background-color: #fff;*/
    margin-top: 10px;
    margin-right: 20px;
    margin-left: 20px;
  }

}
</style>

