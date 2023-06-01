<template>
  <div class="navbar">
    <div class="left-side">
      <a-space style="align-items: center">
        <router-link to="/">
          <img
              alt="logo"
              src="@/assets/logo-full.png"
              style="height: 50px; padding-left: 10px; padding-top: 12px"
          />
        </router-link>
      </a-space>
    </div>
    <ul class="right-side">
      <li class="search-container">
        <a-button v-if="this.isController" class="nav-btn" type="outline" :shape="'circle'"  @click="notificationVisible=true">
          <ToolsIcon />
        </a-button>
      </li>
      <li>
        <t-dialog
            v-model:visible="notificationVisible"
            :header="notificationHeader"
            attach="body"
            :footer="false"
            :on-esc-keydown="closeNotificationDialog"
            :on-close-btn-click="closeNotificationDialog"
            :on-close="closeNotificationDialog"
            width="800px"
            placement="top"
          >
     
            <t-form ref="form" :data="askIssueForm">
              <t-form-item 
              label="Title:" 
              :data="notification.title" 
              name="title" 
              :rules="[{ required: true, message: '此项必填' }]"
              >
                <t-input v-model="notification.title" placeholder="请输入标题" />
              </t-form-item>
              <t-form-item label="Content:" :data="notification.content" name="title" >
                <t-textarea v-model="notification.content" placeholder="请输入内容" autofocus autosize={minRows:5} />
              </t-form-item>
              <div style="margin-top:10px ; display: flex; justify-content: center;">
              <t-button size="large" style=" margin-left: 2px;" theme="success" @click="publishNotification">确认发布</t-button>
              </div>
            </t-form>
         
          
              
     
          
        </t-dialog>
        

        <a-trigger trigger="click">
          <a-tooltip :content="$t('language')">
        <a-dropdown @select="changeLanguage">
          <a-button class="nav-btn" type="outline" :shape="'circle'">
            <template #icon>
              <icon-language/>
            </template>
          </a-button>
          <template #content>
            <a-doption value="zh-CN">简体中文</a-doption>
            <a-doption value="en-US">English</a-doption>
          </template>
        </a-dropdown>
          </a-tooltip>
        </a-trigger>

      </li>
      <li>

        <a-trigger trigger="click">
          <a-tooltip :content="$t('backgroundColor')">
            <a-button class="nav-btn" type="outline" :shape="'circle'">
              <template #icon>
                <icon-skin />
              </template>
            </a-button>
          </a-tooltip>

          <template #content>
            <t-color-picker-panel v-model=getBackgroundColor :color-modes="['linear-gradient']"  @change="changeBackground" :swatch-colors="systemColors"/>

          </template>
        </a-trigger>


      </li>
      <li>
      <a-trigger trigger="click">
        <a-tooltip :content="chat">
          <a-button class="nav-btn" type="outline" :shape="'circle'">
            <template #icon>
              <icon-message />
            </template>
          </a-button>
        </a-tooltip>
        <template #content>
            <div style="background-color: rgb(255, 255, 255); width: 780px; height: 700px; border-style:solid; border-color: rgb(231, 231, 231); border-width:1px; box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);">
              <!-- header -->
              <div style="display: flex; flex-direction: row;height: 7%; background-color: rgb(203, 243, 248);">
                <div style="display: flex; align-items: center; width: 26%; border-style:solid; border-color: rgb(231, 231, 231); border-width:0 1px 1px 0"> 
                  <font style="font-size: large;  margin-left: 15px; color: rgb(3, 93, 177);">私信</font>
                </div>
                <div style="display: flex; align-items: center;justify-content: space-between;width: 74%; border-style:solid; border-color: rgb(231, 231, 231); border-width:0 0 1px 1px">
                  <div>
                  <font style="font-size: large; margin-left: 15px; color: rgb(3, 93, 177);">{{ this.chooseUser.name }}</font>
                  <t-tag shape="round" style="margin-left: 5px; color: rgb(213, 218, 221); background-color:aliceblue ;">{{this.chooseUser.recentTime}}</t-tag>
                  </div>
                  <div style="margin-right: 10px;">
                    <t-button size="large" shape="circle" theme="primary" variant="text" style="margin-right: 10px;">
                      <EllipsisIcon/>
                    </t-button>
                    <t-button size="large" shape="circle" variant="text" theme="primary">
                      <CloseIcon />
                    </t-button>
                  </div>
                </div>
              </div>
              <!-- body  border-style:solid; border-color: rgb(80, 153, 228); border-width: 2px -->
              <div style="display: flex; flex-direction: row; height: 93%; border-style:solid; border-color: rgb(231, 231, 231); border-width:0 0 1px 0;">
                <div style="width: 26%; border-style:solid; border-color: rgb(231, 231, 231); border-width:1px 1px 0 0; background-color: aliceblue;"> 
                  <div v-for="user in userList" >
                    <div class="choose" v-if="user.name==chooseUser.name" >
                      <div style="display: flex; flex-direction: row; align-items: center; margin-left: 10px;">
                        <t-avatar :image=user.userImg size=50px />
                        <div style="margin-left: 10px;">
                          <div><font style="font-size: large;">{{user.name}}</font></div>
                          <div><font style="font-size: smaller; color: rgb(211, 212, 212);">{{user.recentTime}}</font></div>
                        </div>
                      </div>
                    </div>
                    <div class="nochoose" v-else @click="chooseOne(user)">
                      <div style="display: flex; flex-direction: row; align-items: center; margin-left: 10px;">
                        <t-avatar :image=user.userImg size=50px />
                        <div style="margin-left: 10px;">
                          <div><font style="font-size: large;">{{user.name}}</font></div>
                          <div><font style="font-size: smaller; color: rgb(211, 212, 212);">{{user.recentTime}}</font></div>
                        </div>
                      </div>
                    </div>
                    
                  </div>
                </div>
                <div style="width: 74%; background-color: rgb(245, 249, 250); border-style:solid; border-color: rgb(231, 231, 231); border-width: 1px 0 0 1px">
                  <div style="height: 90%; overflow-y: auto;"> 
                      <div v-for="message in messageList" style=" margin-left: 10px; margin-right: 10px; display: flex;">
                        <div v-if="message.name!=myName" style="display: flex; flex-direction: row; margin-top: 10px;">
                          <div><t-avatar :image=message.userImg size=40px /></div>
                          <div style="margin-left: 10px;">
                            <font style="font-size: small; margin-right: 5px;">{{message.name}}</font>
                            <font style="font-size: smaller; color: rgb(211, 212, 212);">{{message.time}}</font>
                            <div style="margin-top: 5px;border-style: solid; border-width: 0; border-radius: 10px; background-color: rgb(201, 231, 250);">
                              <font style="font-size: 15px; padding-left: 5px; padding-right: 5px;">{{message.content}}</font>
                            </div>
                          </div>
                       </div>
                       <div v-else style="display: flex; flex-direction: row; margin-top: 10px; margin-left: auto;">
                            <div style="margin-right: 10px;">
                              <div style="display: flex;">
                                <div style="margin-left: auto;">
                                  <font style="font-size: smaller; margin-right: 5px; color: rgb(211, 212, 212);">{{message.time}}</font>
                                  <font style="font-size: small;">{{message.name}}</font>
                                </div>
                              </div>
                              <div style="margin-top: 5px;border-style: solid; border-width: 0; border-radius: 10px; background-color: rgb(223, 249, 223);">
                                <font style="font-size: 15px; padding-left: 5px; padding-right: 5px;">{{message.content}}</font>
                              </div>
                            </div>
                            <div><t-avatar :image=message.userImg size=40px /></div>
                       </div>
                      </div>
                    <t-divider id="msg_end"></t-divider>
                  </div>
                  <div style="height: 10%;width:100%; display: flex; align-items: center; justify-content: center;">
                    <t-input v-model="messageInput" size="large" style="width: 520px; margin-right: 1px;" placeholder="请输入内容" />
                    <t-button size="large" shape="circle" variant="text" theme="primary" @click="send">
                      <ArrowUpIcon />
                    </t-button>
                  </div>
                </div>
              </div>
            </div>

        </template>
      </a-trigger>


      </li>
      <li>
        <a-button class="nav-btn" type="outline" :shape="'circle'">
          <template #icon>
            <icon-notification/>
          </template>
        </a-button>
      </li>
      <li>
        <a-button style="border-radius: 10px" v-if="!isLogin" @click="$router.push('/login')">{{ $t('login') }}
        </a-button>
        <a-dropdown v-else trigger="hover" >
          <a-button class="nav-btn" type="outline" :shape="'circle'" @click="jumpToUser">
            <template #icon>
              <img
                  id="avatar1"
                  :alt="user"
                  :src="getAvatar"
                  style="height: 30px; width: 30px; border-radius: 30px"
              />
            </template>
          </a-button>
          <template #content>
            <a-doption @click="jumpToUser" style="text-align: center"> {{ getUser?.login ?? '' }}</a-doption>
            <a-doption @click="logout" style="text-align: center;">{{$t('SignOut')}}</a-doption>
          </template>
        </a-dropdown>
      </li>
    </ul>
  </div>
</template>

<script>
import {mapGetters, mapActions, mapMutations} from 'vuex'
import {ToolsIcon, EllipsisIcon, CloseIcon, ArrowUpIcon } from 'tdesign-icons-vue-next';
import {BACKEND_URL} from "@/utils/config";
import request from '@/utils/request';
import vuex from "@/store/index";
let socket;

export default {
  components: {
    EllipsisIcon,
    CloseIcon,
    ArrowUpIcon,
    ToolsIcon
  },
  data() {
    return {
      notification:{
        title:"",
        content:"",
      },
      notificationVisible: false,
      notificationHeader: '创建通知',
      userId:"",
      systemColors: ['linear-gradient(45deg, white 0%, white 100%)',  'linear-gradient(0deg,rgb(230, 249, 255) 0%,rgb(255, 255, 255) 100%)'],
      myName: "我",
      myImg:"https://tdesign.gtimg.com/site/avatar.jpg",
      chat: '聊天窗口',
      messageInput:"",
      content:"",
      chooseUser: {
        name:'你爹',
        online:true,
        recentTime:"2小时前在线",
        userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
        unread:0,
      },
      userList:[
        {
          name:'你爹',
          online:true,
          recentTime:"2小时前在线",
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          unread:0,
        },
        {
          name:'你大爹',
          online:false,
          recentTime:"2小时前在线",
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          unread:0,
        },
        {
          name:'Wyyar',
          online:false,
          recentTime:"5天前在线",
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          unread:0,
        },
      ],
      messageList:[
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"你爹",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"你爹",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！v",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"wangy",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！",
        },
        {
          userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
          name:"你爹",
          time:"今天 2:30",
          content:"你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！你好你好你好呀！！！！！！v",
        },
      ],

    }
  },
  async created() {
    // localStorage.setItem('username', 'admin');
    // vuex.commit('user/setToken', localStorage.getItem('token'));
    var el = document.getElementById("avatar1");    
    el.src = BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
    this.messageList=[];
    this.userList=[];
    this.userid = localStorage.getItem("userid");
    if (localStorage.getItem('token')) {
      await request.post(BACKEND_URL+'/getUserList?username='+localStorage.getItem("username")).then((respond) => {
          var users = respond.data;
          for(var i = 0; i<users.length; i++){
            this.userList.push({
              name:users[i].name,
              online:users[i].online,
              recentTime:users[i].recentTime,
              // userImg:users[i].userImg,
              unread:users[i].unread,
            })
          }
      });
    }
    if (localStorage.getItem('token')) {
      let username = localStorage.getItem('username')
      this.myName = username;
      let _this = this;
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        let socketUrl = "ws://10.26.11.88:8080/websocket/" + username;  // 服务器端接口地址 改下啊！！！！！！！！！！！！！！
        if (socket != null) {
          socket.close();
          socket = null;
        }
        // 开启一个websocket服务
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function () {
          console.log("websocket已打开");
        };
        //  浏览器端收消息，获得从服务端发送过来的文本消息
        socket.onmessage = function (msg) {
          console.log("收到数据====" + msg.data)
          let data = JSON.parse(msg.data)  // 对收到的json数据进行解析， 类似这样的： {"users": [{"username": "zhang"},{ "username": "admin"}]}
          if (data.users) {  // 获取在线人员信息
            _this.users = data.users.filter(user => user.username !== username)  // 获取当前连接的所有用户信息，并且排除自身，自己不会出现在自己的聊天列表里
          } else {
            // 如果服务器端发送过来的json数据 不包含 users 这个key，那么发送过来的就是聊天文本json数据
            //  // {"from": "zhang", "text": "hello"}
            if (data.username === _this.chatUser) {
              _this.messageList.push({
                // userImg:_this.chatUser.userImg,
                name:data.fromUser,
                time:data.sendTime,
                content:data.content,
              })
            }
          }
        };
        //关闭事件
        socket.onclose = function () {
          console.log("websocket已关闭");
        };
        //发生了错误事件
        socket.onerror = function () {
          console.log("websocket发生了错误");
        }
      }
    

    }
  },
  computed: {
    ...mapGetters('user', ['isLogin', 'getUser']),
    ...mapGetters(['getBackgroundColor']),
    isController() {
      return localStorage.getItem('username') === 'admin'
    },
    getAvatar() {
      return BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
    },  
  },
  mounted() {
    // this.getUserInfo()
  },
  methods: {
    BACKEND_URL() {
      return BACKEND_URL
    },
    ...mapActions('user', ['logout', 'getUserInfo']),
    ...mapMutations(['setBackgroundColor']),
    jumpToSearch() {
      this.$router.push('/search')
    },
    jumpToUser() {
      if (this.isLogin) {
        this.$router.push({name: 'user', params: {user: this.getUser.login}})
      }
    },
    changeLanguage(val) {
      this.$i18n.locale = val   // 设置当前语言
      localStorage.setItem('liquid-locale', val)
    },
    changeBackground(val){
      this.setBackgroundColor(val)
    },
    send() {
      if (typeof (WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        // 组装待发送的消息 json
        // {"from": "zhang", "to": "admin", "text": "聊天文本"}
        let message = {fromUser: this.myName, toUser: this.chooseUser.name, content: this.messageInput}
        socket.send(JSON.stringify(message));  // 将组装好的json发送给服务端，由服务端进行转发
        this.messageList.push({
          userImg:this.myImg,
          name:this.myName,
          time:new Date().getTime(),
          content: this.messageInput ,
        })
        this.messageInput = '';
        msg_end.scrollIntoView();
      }
      
    },
    async chooseOne(user){
      this.messageList = [];
      await request.post(BACKEND_URL+'/inChat?from_user='+localStorage.getItem("username")+"&to_user="+user.name).then((respond) => {
          console.log(respond.data);
          for(var i = 0; i<respond.data.data.length; i++){
            this.messageList.unshift({
              userImg:"https://tdesign.gtimg.com/site/avatar.jpg",
              name:respond.data.data[i].fromUser,
              time:respond.data.data[i].sendTime,
              content: respond.data.data[i].content ,
            });
          }
      });
      console.log(this.messageList);
      this.chooseUser = user;
      msg_end.scrollIntoView();
    },
    closeNotificationDialog() {
      this.notificationVisible = false;
    },
    async publishNotification(){
      await request.post(BACKEND_URL+'/publishNotification?title='+this.notification.title+"&content="+this.notification.content).then((respond) => {
          console.log(respond.data);
      });
      this.notifacation={
        title:"",
        content:"",
      },
      this.notificationVisible = false;
    },
    
  }
}

</script>

<style scoped lang="less">

.demo-basic {
  padding: 10px;
  width: 200px;
  background-color: var(--color-bg-popup);
  border-radius: 4px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
}

.language-item {
  font-size: 14px;
  height: 50px;
}

.language-item:hover {
  background-color: var(--color-neutral-2);
  cursor: pointer;
}

.search-container {
  border-radius: 25px;
  display: flex;
  justify-content: space-between;
  background-color: transparent;
  transition: 1s;
  margin-right: 15px;
}
.choose{
  border-style:solid; 
  border-color: rgb(231, 231, 231); 
  border-width:0 0 1px 0; 
  display: flex; 
  align-items: center; 
  height: 70px; 
  background-color:rgb(131, 187, 243);
}

.nochoose{
  border-style:solid; 
  border-color: rgb(231, 231, 231); 
  border-width:0 0 1px 0; 
  display: flex; 
  align-items: center; 
  height: 70px; 
  background-color: rgb(239, 241, 243);
}

.nochoose:hover{
  border-style:solid; 
  border-color: rgb(231, 231, 231); 
  border-width:0 0 1px 0; 
  display: flex; 
  align-items: center; 
  height: 70px; 
  background-color: rgb(197, 222, 247);
}


.search-text {
  margin-left: 10px;
  border: none;
  outline: none;
  float: left;
  padding: 0;
  width: 0;
  color: transparent;
  transition: 1s;
  background-color: transparent;
}

.search-container .search-btn {
  padding: 0;
  margin-right: -10px;
  border-color: rgb(var(--gray-2));
  color: rgb(var(--gray-8));
  background-color: white;
  font-size: 16px;
  float: right;
}


.search-container:hover {
  background-color: var(--color-neutral-2);
  transition: 1s;
}

.search-container:focus-within {
  background-color: var(--color-neutral-2);
  transition: 1s;
}

.search-container:hover .search-text {
  width: 200px;
  background-color: transparent;
  color: black;
}

.search-container:hover .search-btn {
  background-color: rgb(var(--arcoblue-5));
  border-color: transparent;
  color: white;
}


.search-container:focus-within .search-text {
  width: 200px;
  background-color: transparent;
  color: black;
}


.search-container:focus-within .search-btn {
  background-color: rgb(var(--arcoblue-5));
  border-color: transparent;
  color: white;
}


.navbar {
  display: flex;
  justify-content: space-between;
  height: 100%;
  background-color: var(--color-bg-2);
  border-bottom: 1px solid var(--color-border);
}

.left-side {
  display: flex;
  align-items: center;
  padding-left: 20px;
}

.right-side {
  display: flex;
  padding-right: 20px;
  list-style: none;

  li {
    display: flex;
    align-items: center;
    padding: 0 10px;
  }

  .nav-btn {
    border-color: rgb(var(--gray-2));
    color: rgb(var(--gray-8));
    font-size: 16px;
  }


}

</style>

