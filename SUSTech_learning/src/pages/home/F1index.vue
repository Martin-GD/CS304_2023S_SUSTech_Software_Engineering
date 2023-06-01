<template>
  <div class="page">
    <div class="leftpanel">
       <leftpanel></leftpanel>
    </div>


    <div class="centerpanel" >
      <div class="search-container" style="display: flex; align-items: center; justify-content: center;" >
        <a-space direction="vertical" style="width:70%; height:70%">
          <img
              alt="logo"
              src="@/assets/atom.png"
              style="height: 80px; padding-left: 28%"
          />
          <a-input-search
            v-model="this.searchInput"
            placeholder="input search text"
            style="width: 100%; height: 50px; border-radius: 15px;"
            @search="searchEssay"
          />
        </a-space>
      </div>

      <div class="button" style="margin-top: 10px;">
        <div style="display:flex;" >
        <a-button type="default" style="border-style: solid;border-color: rgb(193, 222, 248);background-color: rgb(146, 203, 249);width:30%; height: 50px; border-radius: 15px; margin: 25px 2% 10px 18% ;" @click="searchEssay">Search Essay</a-button>
        <a-button type="default" style="border-style: solid;border-color: rgb(193, 222, 248);background-color: rgb(146, 203, 249);width: 30%; height: 50px; border-radius: 15px;margin: 25px 12% 10px 2%;" @click="searchTopic">Search in Forum</a-button>
        </div>
        <div style="display:flex; justify-content: center; margin-right: 15px;margin-top: 10px;">
          <a-button type="default" style="border-style: solid;border-color: rgb(122, 219, 156);background-color: rgb(5, 171, 113);width:60%; height: 50px; border-radius: 15px;" @click="gptVisible=true;msgList=[];">
            <t-avatar :image="require('@/assets/gpt.png')" size=40px style="margin-right: 10px;"/>
            ChatGPT
          </a-button>
        </div>
      </div>
      <t-dialog
        v-model:visible="gptVisible"
        :header=this.gptHeader
        attach="body"
        :footer="false"
        :on-esc-keydown="closeGptDialog"
        :on-close-btn-click="closeGptDialog"
        :on-close="closeGptDialog"
        width="750px"
        placement="top"
    >
    <div style="height: 500px; overflow-y: scroll;">
      <div v-for="msg in msgList">
        <div v-if="msg.isgpt" style="display: flex; flex-direction: row; margin-top: 15px;">
          <div><t-avatar :image="require('@/assets/gpt.png')" size=40px /></div>
          <div style="margin-left: 10px;">
            <font style="font-size: small; margin-right: 5px;">ChatGPT</font>
            <div style="margin-top: 5px;border-style: solid; border-width: 0; border-radius: 10px; background-color: rgb(201, 231, 250);">
              <font style="font-size: 15px; padding-left: 5px; padding-right: 5px;">{{msg.content}}</font>
            </div>
          </div>
        </div>

        <div v-else style="display: flex; flex-direction: row; margin-top: 15px;">
          <div><t-avatar :image=this.userImg size=43px /></div>
          <div style="margin-left: 10px;">
            <font style="font-size: small; margin-right: 5px;">You</font>
            <div style="margin-top: 5px;border-style: solid; border-width: 0; border-radius: 10px; background-color: rgb(201, 231, 250);">
              <font style="font-size: 15px; padding-left: 5px; padding-right: 5px;">{{msg.content}}</font>
            </div>
          </div>
          </div>

      </div>

    </div>
      <div style="display: flex; align-items: center;">
            <div style="margin-right: 10px;">
                <t-avatar :image="this.userImg" size="55px"></t-avatar>
            </div>
            <div style="width:80%">
                <t-input v-model="gptInput"  placeholder="请提出你的疑问" style="width:100%"></t-input>
            </div>
            <t-button style="float: right; margin-left: 10px;" @click="askGpt" >
                <span>确认</span>
            </t-button>

        </div>

     </t-dialog>

      <div class="recommend-list">
        <a-list :split="false" :bordered="false">
          <template #header>
            {{this.recommend}}
          </template>

<!--          <t-divider></t-divider>-->
          <div v-for="note in articles">
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <div>
                <div style="margin-left: 10px;" @click="clickNote(note.id)">
                  <font style="font-size: medium; font-weight:550; color:#0969da;">{{note.name}}</font>
                  <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue; color: black; border-color: rgb(220, 215, 215);">{{note.owner}}</t-tag>
                </div>
                <p style="margin-left: 10px; color: rgb(78, 71, 71);">{{ note.description }}</p>
                <div style="margin-left: 10px;">
                  <t-tag v-for="tag in note.tag" size="samll" shape="round" style="margin-left: 3px; margin-top: 3px; background-color: rgb(247, 241, 177);">{{tag.name}}</t-tag>
                  <font style="margin-left: 10px; color: rgb(97, 95, 95); font-size: smaller;">Update on {{this.transTime(note.updatedAt)}}</font>
                </div>
              </div>

            </div>
            <t-divider></t-divider>
          </div>
        </a-list>
      </div>



    </div>


    <div class="rightpanel">
      <div class="timeline">
        <a-timeline>
          <a-timeline-item v-for="item in this.notificationList" :label="item.time" lineType="dashed">
            {{item.title}}
            <br/>
            <a-typography-text
                type="secondary"
                :style="{ fontSize: '12px', marginTop: '4px' }"
            >
             {{ item.content}}
            </a-typography-text>
          </a-timeline-item>
          <!-- <a-timeline-item label="2023-1-3" lineType="dashed">
            Note sharing function released 🎉
            <br/>
            <a-typography-text
                type="secondary"
                :style="{ fontSize: '12px', marginTop: '4px' }"
            >
              New year, new features and improvements! 🎆 We're making URLs in Projects more powerful with direct links
              to project READMEs...
            </a-typography-text>
          </a-timeline-item>
          <a-timeline-item label="2023-1-3" lineType="dashed">
            We open the discussion module. Let's talk about the future of the project. 🎉
            <br/>
            <a-typography-text
                type="secondary"
                :style="{ fontSize: '12px', marginTop: '4px' }"
            >
              The discussion module is a new feature that allows you to discuss the project with other users. You can......
            </a-typography-text>
          </a-timeline-item> -->
        </a-timeline>
      </div>
      <a-divider/>
      <div class="explore">
        <a-list :split="false" :bordered="false">
          <template #header>
            {{ $t('exploreRepositories') }}
          </template>
          <a-list-item v-for="item in articles">
            <div style="height: 50px;display:flex;justify-content: flex-start;border-radius: 4px; box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);">
            <t-avatar :image="userImg" size=48px :hideOnLoadFailed="false" @error="onError"></t-avatar>
            <font color="black" >{{item.userName}}</font>
            <a-list-item-meta>
              <template #avatar>
                <div class="repo-item">

                  <a-link click=""
                          :hoverable="false"
                          style="justify-content: flex-start; margin-left: 8px;" >
                      {{ item.articleName }}
                         <icon-ThumbUp />
                        {{item.goodnum}}
                  </a-link>

                </div>
              </template>
            </a-list-item-meta>
          </div>
          </a-list-item>
        </a-list>
      </div>


    </div>
  </div>


</template>

<script>
import axios from '@/axios';
import leftpanel from '@/pages/home/leftpanel.vue';
import userLink from "@/components/user/F1userLink.vue";
import userAvatar from "@/components/user/F1userAvatar.vue";
import request from "@/utils/request";
// import dayjs from 'dayjs';

// import jsCookie from "js-cookie";
import {BACKEND_URL} from "@/utils/config";
export default {
  components:{
    leftpanel
  },
  data: function(){
    return {
      recommend: '推荐浏览',
      notificationList:[
        {
          time:"2023-1-3",
          content:"New year, new features and improvements! 🎆 We're making URLs in Projects more powerful with direct linksto project READMEs...",
          title:"Note sharing function released 🎉",
        }
      ],
      searchInput:"",
      gptInput:"",
      msgList: [
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹',
        },
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹你好，我是你爹你好，我是你爹你好，我是你爹你好，我是你爹你好，我是你爹',
        },
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹',
        },
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹',
        },
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹',
        },
        {
          isgpt: true,
          content: '你好，我是GPT，我是一个聊天机器人，我可以和你聊天，你可以问我任何问题，我会尽力回答你的问题。',
        },
        {
          isgpt: false,
          content: '你好，我是你爹',
        },
      ],
      gptVisible: false,
      gptHeader: 'chatGPT',
      userName: "GMD",
      userImg: "https://tdesign.gtimg.com/site/avatar.jpg",
      follower_images:[],
      articles:[{userName:"student",goodnum:32, articleName:"Python 读写excel表格，就是这么简单粗暴又好用"},
      {userName:"jack",goodnum:67, articleName:'新手到黑客的最全入门路劲图（附全学习资料下载'},
      {userName:"abccc",goodnum:79, articleName:'三星引入 ChatGPT 不到 20 天，被曝发生 3 次芯片机密泄露!'},],

    }
  },
  async created() {
    await request.post(BACKEND_URL + "/getNotifications").then((res) => {
        console.log(res);

      });
  },
  mounted() {
    this.getRecommendNotes();
  },

  methods:{
    transTime(time){
      let timestamp = time; // 替换为您的时间戳
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
      return formattedDate;
    },
    clickNote(noteid){
      const url = '/read?id='+noteid; // 设置要打开的URL
      this.$router.push(url)
      // window.open(url);
    },
    closeGptDialog() {
      this.gptVisible = false;
    },
    async askGpt(){
      await request.post(BACKEND_URL + "/chat?token="+localStorage.getItem("token"), {
        "content": this.gptInput,
      }).then((res) => {
        console.log(res);
        if(res.data.code==1005){
          alert(res.data.message);
        } else{
          this.msgList.push({
          isgpt: true,
          content: res.data.data.response,
        });
        }
      });
      this.gptInput = "";
    },
    searchTopic(){
      var type = 0;
      console.log(this.searchInput);
      if(this.searchInput==""){
        type = 0;
        this.searchInput = "none";
      }else{
        type = 1;
      }
      this.$router.push({name:"forum",params:{type:type, content:this.searchInput}});
    },
    searchEssay(){
      var _this = this;
      request.get(BACKEND_URL+`/searchNotes?input=${this.searchInput}`).then(resp=>{
        _this.articles = resp.data.data;
        _this.recommend = '检索结果';
      })
    },
    getRecommendNotes(){
      var _this = this;
      request.get(BACKEND_URL+`/recommendNotes?token=${localStorage.getItem('token')}`).then(resp=>{
        _this.articles = resp.data.data;
        // _this.recommend = '检索结果';
      })
    },
  },
  computed:{
    userImg(){
      return localStorage.getItem("avatar");
    },
    // userName(){
    //   return this.$store.state.user.userName;
    // },
    // follower_images(){
    //   return this.$store.state.user.follower_images;
    // },
    // articles(){
    //   return this.$store.state.user.articles;
    // },
  },

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

.centerpanel {
  width: 55%;
  height: 100%;
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
.search-container {
  height: 20%;
  /* grid-template-columns: 30% 40% 30%;
  grid-template-rows: 42% 16% 42%; */
}
.button {
  width: 100%;
  height: 25%;
}
.recommend-list{
  margin-left:10% ;
  width: 80%;
  height: 70%;
}


.rightpanel {
  display: flex;
  justify-content: center;
  flex-direction: column;
  width: 25%;
  height: 100%;
  margin-top: 10px;
  margin-right: 5px;
  margin-left: 5px;
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
