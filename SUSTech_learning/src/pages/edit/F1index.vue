<template>
    <div class="page">
      <div class="leftpanel">
         <leftpanel></leftpanel>
      </div>
      <div class="rightpanel">
        <br><br>
<!--         <vditor></vditor>-->
        <Markdown ref="markdownEditor"  :value="editorContent"  :isHideTools=false :isPin=true vidtorMode="sv"  height="95%" width="100%" :titleID="titleID"  :autoSaveFile=true />
      </div>
    </div>


  </template>

  <script>

  // import axios from 'axios';
  import request from '@/utils/request';

  import leftpanel from '@/pages/home/leftpanel.vue';
  import vditor from '@/components/EditVditor.vue';
  import Markdown from '@/components/edit/index'

  // import dayjs from 'dayjs';
  // import request from "@/utils/request";
  // import jsCookie from "js-cookie";
  import {BACKEND_URL} from "@/utils/config";


  import defaltText from '../../config/default'
  export default {
    components:{
      leftpanel,
      vditor,
      Markdown
    },
    data: function(){
      return {
        editorContent: '',
        markdownContent: "",
        titleID: ""

      }
    },

    mounted() {
      this.titleID = this.$route.query.id;
      console.log(this.titleID)
      this.getContent();
    },
    methods:{
      getContent(){
        const _this = this
        request.get(BACKEND_URL+`/getNoteContent?note=${this.titleID}`).then(resp => {
          _this.editorContent = resp.data.data
          // alert(resp.data.data)
        }).then(() => {

        })
      }
    }
  }

  // export default {
  //   name: "index",
  //   components: {userAvatar, userLink, F1issueItem, F1pullItem},
  //   created() {
  //     document.title = 'Home - liquid'
  //   },
  //   mounted() {
  //     this.recentRepos = JSON.parse(localStorage.getItem('recents') || '[]')

  //     request.get(BACKEND_URL + `/core/api/user//allsiterepo`, {
  //       headers: {
  //         'Authorization': jsCookie.get('Authorization')
  //       }
  //     }).then(resp => this.repos = resp.data.data)
  //     request.get(BACKEND_URL + `/core/api/issue/watchingrepos`, {
  //       headers: {
  //         'Authorization': jsCookie.get('Authorization')
  //       }
  //     }).then(resp => this.inform_issue_list = resp.data.data)
  //     request.get(BACKEND_URL + `/core/api/pr/listwatching`, {
  //       headers: {
  //         'Authorization': jsCookie.get('Authorization')
  //       }
  //     }).then(resp => this.inform_pr_list = resp.data.data)
  //   },
  //   data() {
  //     return {
  //       informType: '1',
  //       dayjs,
  //       me: localStorage.getItem('inUser'),
  //       repos: [],
  //       inform_issue_list: [],
  //       inform_pr_list: [],
  //       recentRepos: []
  //     }
  //   }
  // }
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
    margin-top: 10px;
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
