<template>
  <div>
    <t-dialog
        v-model:visible="replyVisible"
        :header=this.replyHeader
        attach="body"
        :footer="false"
        :on-esc-keydown="closeReplyDialog"
        :on-close-btn-click="closeReplyDialog"
        :on-close="closeReplyDialog"
        width="750px"       
        placement="top"
    >
    <div>
      <div>
        <t-tag size="large">Title:</t-tag>
        <font style="font-size: 25px; color: black;">{{ this.cardInfo.title }}</font>
      </div>
      <t-textarea style="margin-top: 5px;" v-model="this.answerInput" placeholder="请输入内容" autofocus :autosize="{ minRows: 6, maxRows: 10 }" />
      <div style="margin-top: 10px; display: flex; justify-content: center;">
        <t-button style="margin-right: 5px;"  theme="primary" @click="submitReply">提交回答</t-button>
        <t-button theme="primary" @click="clickReplyCancel">取消</t-button>
      </div>
    </div>
      
  </t-dialog>
  <div v-if="this.cardInfo.answer">
    <t-card  :style="{ width:'100%' }" :title=this.cardInfo.title>
        <div>
            <t-avatar :image=this.cardInfo.answer.userImage size="40px"></t-avatar>
            <span style="font-weight: bolder; margin-left: 10px;"> {{this.cardInfo.answer.username}}</span>
            <t-button v-if=this.cardInfo.answer.isFollow style="float: right;" @click="cancelFollow">
                <span>已关注</span>
            </t-button>
            <t-button v-else style="float: right; " @click="Follow">
                <AddIcon />
                <span> 关注</span>
            </t-button>
        </div>
        <div v-if="this.cardInfo.answer.content.length>100">
          <div v-if="isUnfold">
              {{ this.cardInfo.answer.content }}
              <div>
                  <font style="color: rgb(167, 159, 159);">
                  发布于 {{this.cardInfo.answer.time}}
                  </font>
              </div>
          </div>
          <div v-else>
              {{foldContent}}
          </div>
        </div>
        <div v-else>
          {{ this.cardInfo.answer.content }}
              <div>
                  <font style="color: rgb(167, 159, 159);">
                  发布于 {{this.cardInfo.answer.time}}
                  </font>
              </div>
        </div>

        <div v-if="this.cardInfo.answer.content.length>100">
          <div class="readAll" v-if="isUnfold">
              <t-link theme="primary" hover="underline" @click="clickFold"> 收起全文 </t-link>
              <ChevronDownIcon />
          </div>
          <div class="readAll" v-else>
              <t-link theme="primary" hover="underline" @click="clickUnfold"> 展开阅读全文 </t-link>
              <ChevronDownIcon />
          </div>
        </div>


      <template #actions>
        <t-button variant="text" shape="square" @click="topicVisible=true;">
            <BrowseIcon />
        </t-button>
      </template>
      <t-dialog
            v-model:visible="topicVisible"
            :header=this.topicHeader
            attach="body"
            :footer="false"
            :on-esc-keydown="closeTopicDialog"
            :on-close-btn-click="closeTopicDialog"
            :on-close="closeTopicDialog"
            width="600px"       
            placement="top"
        >
        <div>
          <div style="margin-bottom: 5px;font-size: 20px; font-weight: 600; color: black;">{{ cardInfo.title }}</div>
          <div style="margin-bottom: 10px;color: black;">{{ cardInfo.content }}</div>
          <div>
            <t-button style="margin-right: 5px;" v-if="cardInfo.isStar==false" variant="outline" theme="primary" @click="clickSave">关注问题</t-button>
            <t-button style="margin-right: 5px;"  v-else theme="primary" @click="clickSave">已关注</t-button>
            <t-button style="margin-right: 5px;" variant="outline" theme="primary" @click="replyVisible=true; topicVisible=false">写回答</t-button>
            <t-button style="margin-right: 5px;" variant="outline" theme="primary">邀请回答</t-button>
            <t-button variant="text" shape="square" :style="{'width': '100px'}" @click="likeIssue">
              <ThumbUpIcon v-if=this.cardInfo.isLove style="color: rgb(48, 116, 241); margin-bottom: 3px;" />
              <ThumbUpIcon style="margin-bottom: 3px;" v-else/>
              好问题{{this.cardInfo.loveNume}}
            </t-button>
            <t-button variant="text" shape="square" :style="{'width': '100px'}">
              <ChatIcon style="margin-bottom: 3px;"/>
              {{this.cardInfo.replyNum}}条回答
            </t-button>
            <t-button variant="text" shape="square" :style="{ 'width': '50px' }">
              <share-icon  style="margin-bottom: 3px;"/>
              分享
            </t-button>
          </div>
        </div>
      </t-dialog>

      <template #footer>
        <t-button variant="text" shape="square" :style="{'width': '80px' ,'margin-right':'8px' }" @click="clickHeart">
          <div v-if=this.cardInfo.answer.isLike>
            <HeartFilledIcon style="color: rgb(45, 104, 241);" />
            赞同{{this.cardInfo.answer.likesNum}}
          </div>
          <div v-else>
            <heart-icon />
            赞同{{this.cardInfo.answer.likesNum}}
          </div>
        </t-button>
        <t-button variant="text" shape="square" :style="{ 'width': '100px' ,'margin-right':'8px'}" @click="commentVisible=true">
          <chat-icon />
          {{this.cardInfo.answer.commentsNum}}条评论
        </t-button>
        <t-dialog
            v-model:visible="commentVisible"
            :header=this.commentHeader
            attach="body"
            :footer="false"
            :on-esc-keydown="closeCommentDialog"
            :on-close-btn-click="closeCommentDialog"
            :on-close="closeCommentDialog"
            width="800px"       
            placement="top"
        >

        <div style="overflow-y:scroll; height: 400px;">
     
            <div v-for="comment in this.cardInfo.answer.comments">
                <div>
                    <t-avatar :image=comment.userImage size="40px"></t-avatar>
                    <span style="font-weight: bolder; margin-left: 10px;"> {{comment.username}}</span>
                    <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">
                        <t-button variant="text" shape="square" style="float: right;">
                            <EllipsisIcon/>
                        </t-button>
                    </t-dropdown>
                </div>
                <div style="margin-left: 40px; ">
                    <div>
                        {{comment.content}}
                    </div>

                    <div style="display: flex; align-items: center;">
                        <div>
                            <font style="color: rgb(167, 159, 159);">
                            发布于 {{comment.time}}
                            </font>
                        </div>
                        <div style="float: right;">
                            <t-button variant="text" shape="square" style="width:80px" @click="clickCommentHeart(comment)">
                                <div v-if=comment.isLike>
                                    <HeartFilledIcon style="color: #f50000;" />
                                    赞同{{comment.likesNum}}
                                </div>
                                <div v-else>
                                    <heart-icon />
                                    赞同{{comment.likesNum}}
                                </div>
                            </t-button>
                        </div>
                    </div>


                </div>

            </div>
        </div>

        <div style="display: flex; align-items: center;">
            <div style="margin-right: 10px;">
                <t-avatar :image=this.myImg size="55px"></t-avatar>
            </div>
            <div style="width:80%">
                <t-input v-model="commentInput"  placeholder="评论千万条，友善第一条" style="width:100%"></t-input>
            </div>
            <t-button style="float: right; margin-left: 10px;" @click="commentAnswer" >
                <span>发布</span>
            </t-button>

        </div>

        </t-dialog>
        <t-button variant="text" shape="square" :style="{ 'width': '50px','margin-right':'8px' }">
          <share-icon />
          分享
        </t-button>
        <t-button variant="text" shape="square" :style="{ 'width': '50px' }" @click="clickSave">
            <div v-if=this.cardInfo.isStar>
                <StarFilledIcon  style="color: rgb(254, 205, 5);"/>
                收藏
            </div>
            <div v-else>
                <StarIcon />
                收藏
            </div>
        </t-button>
        <t-dropdown :options="options" :min-column-width="112" @click="clickHandler">
          <t-button variant="text" shape="square">
            <EllipsisIcon />
          </t-button>
        </t-dropdown>
        <t-button style="float: right;" @click="nextReply">
            <span>下一个回答</span>
        </t-button>
      </template>
    </t-card>
  </div>
  <div v-else>
    <t-card  :style="{ width:'100%' }" :title=this.cardInfo.title>
      <div>
          <div style="margin-bottom: 10px;color: black;">{{ cardInfo.content }}</div>
          <div>
            <t-button style="margin-right: 5px;" v-if="cardInfo.isStar==false" variant="outline" theme="primary" @click="clickSave">关注问题</t-button>
            <t-button style="margin-right: 5px;"  v-else theme="primary" @click="clickSave">已关注</t-button>
            <t-button style="margin-right: 5px;" variant="outline" theme="primary" @click="replyVisible=true">写回答</t-button>
            <t-button style="margin-right: 5px;" variant="outline" theme="primary">邀请回答</t-button>
            <t-button variant="text" shape="square" :style="{'width': '100px'}" @click="likeIssue">
              <ThumbUpIcon v-if=this.cardInfo.isLove style="color: rgb(48, 116, 241); margin-bottom: 3px;" />
              <ThumbUpIcon style="margin-bottom: 3px;" v-else/>
              好问题{{this.cardInfo.loveNume}}
            </t-button>
            <t-button variant="text" shape="square" :style="{'width': '100px'}">
              <ChatIcon style="margin-bottom: 3px;"/>
              {{this.cardInfo.replyNum}}条回答
            </t-button>
            <t-button variant="text" shape="square" :style="{ 'width': '50px' }">
              <share-icon  style="margin-bottom: 3px;"/>
              分享
            </t-button>
          </div>
        </div>
      
    </t-card>
  </div>
  </div>
  </template>

  <script>
  import {ThumbUpIcon, EllipsisIcon, BrowseIcon, ChevronDownIcon, StarIcon, StarFilledIcon, HeartFilledIcon, AddIcon, HeartIcon, ChatIcon, ShareIcon, MoreIcon } from 'tdesign-icons-vue-next';
  import { MessagePlugin } from 'tdesign-vue-next';

  import request from '@/utils/request';
  import {BACKEND_URL} from '@/utils/config';


  const options = [
    {
      content: '举报',
      value: 1,
    },
    {
      content: '打赏',
      value: 2,
    },
  ];

  const clickHandler = function(data) {
    MessagePlugin.success(`${data.content}成功`);
  };

  export default {
    props: {
      cardInfo: {
        type: Object,
        default: () => ({}),
      },
    },
    data() {
      return {
        myImg: '',
        answerInput:"",
        commentInput:"",
        options: options,
        isUnfold:false,
        commentVisible: false,
        anserList: [],
        commnetList:[],
        topicHeader: '问题详情',
        topicVisible:false,
        replyVisible:false,
        replyHeader:'回答一下',
      };
    },
    async created() {
      this.myImg = BACKEND_URL+"/avatar/"+localStorage.getItem("userid");
    },
    methods: {
      clickHandler: clickHandler,
      closeCommentDialog: function(){
        this.commentVisible = false;
      },
      closeTopicDialog: function(){
        this.topicVisible = false;
      },
      closeReplyDialog: function(){
        this.replyVisible = false;
      },
      clickReplyCancel: function(){
        this.answerInput = "";
        this.replyVisible = false;
      },
      clickHeart: async function(){
        if(this.cardInfo.answer.isLike){
          this.cardInfo.answer.likesNum--;
          this.cardInfo.answer.isLike = false;
          // ,{"reply": this.cardInfo.answer.id, "user":localStorage.getItem("token")}
          await request.post(BACKEND_URL+"/unstarReply?reply="+this.cardInfo.answer.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }
        else{
          this.cardInfo.answer.likesNum++;
          this.cardInfo.answer.isLike = true;
           //发送axios请求
           await request.post(BACKEND_URL+"/starReply?reply="+this.cardInfo.answer.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }

      },
      clickCommentHeart: async function(comment){
        if(comment.isLike){
            comment.likesNum--;
            comment.isLike = false;
            //发送axios请求
            await request.post(BACKEND_URL+"/unstarComment?comment="+comment.id+"&user="+localStorage.getItem("userid")).then((res) => {
              console.log(res.data);
            })
        }
        else{
            comment.likesNum++;
            comment.isLike = true;
            //发送axios请求
            await request.post(BACKEND_URL+"/starComment?comment="+comment.id+"&user="+localStorage.getItem("userid")).then((res) => {
              console.log(res.data);
            })
        }

      },
      cancelFollow: async function(){
        this.cardInfo.answer.isFollow = false;
        await request.post(BACKEND_URL+"/unlikeUser?likeUserId="+this.cardInfo.answer.userId+"&token="+localStorage.getItem("token")).then((res) => {
          console.log(res.data);
        })
      },
      Follow: async function(){
        this.cardInfo.answer.isFollow = true;
        await request.post(BACKEND_URL+"/likeUser?likeUserId="+this.cardInfo.answer.userId+"&token="+localStorage.getItem("token")).then((res) => {
          console.log(res.data);
        })
      },
      clickSave: async function(){
        if(this.cardInfo.isStar){
          this.cardInfo.isStar = false;
          await request.post(BACKEND_URL+"/unstarIssue?Issue="+this.cardInfo.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }
        else{
          this.cardInfo.isStar = true;
          await request.post(BACKEND_URL+"/starIssue?Issue="+this.cardInfo.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }
        //发送axios请求
      },
      clickUnfold: function(){
        this.isUnfold = true;
      },
      clickFold: function(){
        this.isUnfold = false;
      },
      commentAnswer: async function(){
        await request.post(BACKEND_URL+"/addComment?content="+this.commentInput+"&reply="+this.cardInfo.answer.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
            this.cardInfo.answer.commentsNum++;
            this.cardInfo.answer.comments.unshift({
              id:res.data.data.id,
              content:this.commentInput,
              username:localStorage.getItem("username"),
              userImage:this.myImg,
              likesNum:res.data.data.star,
              time:res.data.data.createdAt,
              isLike:res.data.data.isStar,
            })
        })
        this.commentInput = "";
      },
      nextReply: async function(){
        var tmp;
        await request.get(BACKEND_URL+"/getChangeRecentIssue?Issue="+this.cardInfo.id+"&createdAt="+this.cardInfo.answer.time+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
            tmp = res.data.data;
        })
        var comments = [];
        if (tmp.reply){
          for(var j=0;j<tmp.reply.commentList.length;j++){
            comments.push({
              id:tmp.reply.commentList.id,
              username:tmp.reply.commentList.author,
              userImage:"none",
              content:tmp.reply.commentList.content,
              likesNum:tmp.reply.commentList.star,
              isLike:tmp.reply.commentList.isStar,
              time:tmp.reply.commentList.createdAt,
            });
          } 
          this.cardInfo.id=tmp.id; 
          this.cardInfo.title=tmp.title;
          this.cardInfo.host=tmp.opener;
          this.cardInfo.replyNum=tmp.replyNum;                    //回答数
          this.cardInfo.loveNume=tmp.love;
          this.cardInfo.isLove=tmp.isLike;                 //是否点赞topic
          this.cardInfo.isFollow=tmp.follow;               //是否关注发布者
          this.cardInfo.isStar=tmp.isStar;                 //是否收藏topic
          this.cardInfo.content=tmp.content;
          this.cardInfo.image="none";                    //发布者用户图片
          this.cardInfo.time=tmp.createdAt;
          this.cardInfo.answer={
            id:tmp.reply.id,
            username:tmp.reply.author,  
            userImage:"none",              //回答用户头像 
            content:tmp.reply.comment,
            likesNum:tmp.reply.star,
            isLike:tmp.reply.isStar,                 //是否点赞该回答
            commentsNum:comments.length,
            isFollow:tmp.reply.follow,
            time:tmp.reply.createdAt,
            comments:comments,
          }
        console.log("----------------------------")
        console.log(this.cardInfo);
          
        } else{
          alert("没有下一个回答！")
        }
      },
      likeIssue: async function(){
        if(this.cardInfo.isLove){
          this.cardInfo.isLove = false;
          this.cardInfo.loveNume--;
          await request.post(BACKEND_URL+"/dislikeIssue?Issue="+this.cardInfo.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }
        else{
          this.cardInfo.isLove = true;
          this.cardInfo.loveNume++;
          await request.post(BACKEND_URL+"/likeIssue?Issue="+this.cardInfo.id+"&user="+localStorage.getItem("userid")).then((res) => {
            console.log(res.data);
          })
        }
        //发送axios请求
      },
      async submitReply(){
        await request.post(BACKEND_URL+"/addReply?content="+this.answerInput+"&issue="+this.cardInfo.id+"&user="+localStorage.getItem("userid")).then((res) => {
          console.log(res.data);
          if (this.cardInfo.replyNum==0){
            this.cardInfo.replyNum++;
            this.cardInfo.answer = {
              id:res.data.data.id,  
              username:res.data.data.author,  
              userImage:"none",              //回答用户头像 
              content:res.data.data.comment,
              likesNum:res.data.data.star,
              isLike:res.data.data.isStar,                 //是否点赞该回答
              commentsNum:0,
              isFollow:res.data.data.follow,
              time:res.data.data.createdAt,
              comments:[],
            };
          }
        })
        
        this.replyVisible=false;
        this.answerInput="";
      },

     
    },
    computed: {
      foldContent:function(){
        return this.cardInfo.answer.content.substring(0,100)
      },
      commentHeader:function(){
        return String(this.cardInfo.answer.commentsNum)+' 条评论'
      }
    },
    components: {
      HeartIcon,
      ChatIcon,
      ShareIcon,
      MoreIcon,
      AddIcon,
      HeartFilledIcon,
      StarIcon,
      StarFilledIcon,
      ChevronDownIcon,
      BrowseIcon,
      EllipsisIcon,
      ThumbUpIcon
    },
  };
  </script>
  <style>
  .readAll{
    display: flex;
    justify-content: center;
    align-items: center;
  }
  </style>
