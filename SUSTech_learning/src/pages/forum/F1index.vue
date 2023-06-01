<template>
    <div class="page">
      <div class="leftpanel" style="background-color:transparent;">
        <div style="background-color: white; border-radius: 1px; height: 50px; display: flex; flex-direction: row; justify-content: space-around; align-items: center;">
          <t-button variant="text" ghost theme="primary" style="height: 100%; width:100px">关注</t-button>
          <t-button variant="text" ghost theme="primary" style="height: 100%; width:100px">热点</t-button>
          <t-button variant="text" ghost theme="primary" style="height: 100%; width:100px">推荐</t-button>
        
            <a-space direction="vertical" style="width:40%; height:70%">
              <a-input-search
                v-model:value="value"
                placeholder="input search text"
                style="width: 100%; height: 100%; border-radius: 10px;"
                @search="onSearch"
              />
            </a-space>


        </div>
        <div style="overflow-y:auto; height: 1400px;">
         <div v-for="topic in topicList">
    
          <answercard :cardInfo="topic"></answercard>
        
         </div>
         </div>
      </div>
  
      <div class="rightpanel" style="background-color:transparent;">
        <rightpanel></rightpanel>
      </div>
    </div>
  
  
  </template>
  
  <script>
  import { toRaw } from '@vue/reactivity'
  import rightpanel from './rightpanel.vue';
  import answercard from './answercard.vue';
  import request from '@/utils/request';
  import {BACKEND_URL} from '@/utils/config';
  export default {
    components:{
        rightpanel,
        answercard
    },
    data: function(){
      return {
        topicList:[
          {
            id:1,
            title:"第一个topic!",
            host:"Wyatt",
            replyNum:234,
            loveNume:223,
            isLove:true,
            isStar:true,
            content:"问题是这样的，这是第一个问题，。。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。问题是这样的，这是第一个问题，。v",
            image:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
            time:"2020-10-11 20:30",
            answer:{
              id:1,//第几个回答
              username:"WangY",
              userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
              content:"这是第一个回答，。。。。。�",
              likesNum:234,
              isLike:false,
              commentsNum:123,
              isFollow:false,
              time:"2020-10-11 20:30",
              comments:[
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
              ]
            }
            
          },
          {
            title:"第2个topic!",
            host:"Wyatt222",
            replyNum:140,
            loveNume:223,
            isLove:false,
            isStar:false,
            content:"问题是这样的，这是第2个问题，。。。。。。。。。。。。。。。。",
            image:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
            time:"2020-10-11 20:30",
            answer:{
              id:1,//第几个回答
              username:"WangY",
              userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
              content:"这是第2个回答，。。。。。�这是一个很这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答这是第2个回答，。。。。。�这是一个很长很长的回答",
              likesNum:234,
              isLike:false,
              commentsNum:123,
              isFollow:false,
              time:"2020-10-11 20:30",
              comments:[
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第2个回答的第2个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
                {
                  username:"WangY",
                  userImage:"https://tdesign.gtimg.com/site/avatar-boy.jpg",
                  content:"这是第一个回答的第一个评论，。。。。。�",
                  likesNum:234,
                  isLike:false,
                  time:"2020-10-11 20:30",
                },
              ]
            }
            
          },
          
        ]
              
                        
                    
      }
    },
    async created(){    
      // sessionStorage.setItem("token", "2") 
      // sessionStorage.setItem("avator", "https://tdesign.gtimg.com/site/avatar-boy.jpg") 
      var tmp;
      this.topicList = [];
      if(this.$route.params.type="0"){
        await request.get(BACKEND_URL+'/getIssueByContent',{params:{user:localStorage.getItem("userid"),content:this.$route.params.content}}).then((respond) => {
          tmp = respond.data.data;
        });
      } else{
        await request.get(BACKEND_URL+'/getRecentIssue',{params:{user:localStorage.getItem("userid")}}).then((respond) => {
          tmp = respond.data.data;
        });
      }
      console.log(tmp);
      for(var i=0;i<tmp.length;i++){
        var comments = [];
        if (tmp[i].reply){
          console.log(tmp[i].reply.commentList);
          for(var j=0;j<tmp[i].reply.commentList.length;j++){
            comments.push({
              id:tmp[i].reply.commentList[j].id,
              username:tmp[i].reply.commentList[j].name,
              userImage: BACKEND_URL+"/avatar/"+tmp[i].reply.commentList[j].author,
              userId:tmp[i].reply.commentList[j].author,
              content:tmp[i].reply.commentList[j].content,
              likesNum:tmp[i].reply.commentList[j].star,
              isLike:tmp[i].reply.commentList[j].isStar,
              time:tmp[i].reply.commentList[j].createdAt,
            });
          }
          this.topicList.push({
            id:tmp[i].id, 
            title:tmp[i].title,
            host:tmp[i].opener,
            replyNum:tmp[i].replyNum,                    //回答数
            loveNume:tmp[i].love,            
            isLove:tmp[i].isLike,                 //是否点赞topic
            isFollow:tmp[i].follow,               //是否关注发布者
            isStar:tmp[i].isStar,                 //是否收藏topic
            content:tmp[i].content,
            image:"none",                    //发布者用户图片        
            time:tmp[i].createdAt,
            answer:{
              id:tmp[i].reply.id,
              username:tmp[i].reply.name,  
              userId:tmp[i].reply.author,      
              userImage: BACKEND_URL+"/avatar/"+tmp[i].reply.author,     //回答用户头像 
              content:tmp[i].reply.comment,
              likesNum:tmp[i].reply.star,
              isLike:tmp[i].reply.isStar,                 //是否点赞该回答
              commentsNum:comments.length,
              isFollow:tmp[i].reply.follow,
              time:tmp[i].reply.createdAt,
              comments:comments,
            }
          });
          console.log(this.topicList);
        } else{
          this.topicList.push({
            id:tmp[i].id, 
            title:tmp[i].title,
            host:tmp[i].opener,
            replyNum:tmp[i].replyNum,                    //回答数
            loveNume:tmp[i].love,            
            loveNume:tmp[i].love,            
            isLove:tmp[i].isLike,              
            content:tmp[i].content,
            image:"none",                    //发布者用户图片        
            time:tmp[i].createdAt, 
            isStar:tmp[i].isStar,       
          });
        }
      }
      console.log(this.topicList);
    },
  }
  

  </script>
  
  <style scoped>
  
  .page {
    display: flex;
    flex-direction: row;
    justify-content: center;
    height: 100%;
    align-content: space-around;
  
  }
  
  
  .leftpanel {
    width: 930px;
    height: 100%;
    margin-top: 10px;
    margin-left: 8px;
    margin-right: 8px;
  }
  

  
  .rightpanel {
    display: flex;
    flex-direction: column;
    width: 320px;
    height: 100%;
    margin-top: 10px;
    margin-left: 7px;
  }

  .title {
    font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
    font-size: 28px;
    font-weight: bold;
    line-height: 1.3;
    color: #333;
    margin: 0 0 12px;
  }


  @media screen and (max-width: 1265px) {
  .rightpanel {
    display: none;
  }
}

  
  
 
  </style>