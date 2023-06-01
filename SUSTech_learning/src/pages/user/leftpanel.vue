<template>
    <div class="username">
        <div class="avator" style="margin-bottom: 10px;">
            <!-- <t-avatar id="avatar" v-if="this.myname==this.userName" style="margin-bottom: -35px;" :image="getUserAvatar" size=230px ></t-avatar>
            <t-avatar id="avatar" v-else :image="getUserAvatar" size=230px ></t-avatar> -->
            <img
                  id="avatar2"
                  :alt="user"
                  :src="userImg"
                  style="height: 230px; width: 230px; border-radius: 230px"
              />
        
            <div style="display: flex; margin-left: 200px;">
              <t-button v-if="this.myname==this.userName" style="margin-right: 2px; margin-left: -1px;" theme="primary"  shape="circle" @click="uploadAvatar">
                <input type="file" ref="imageInput" v-show="false" @change="uploadImage" accept="image/*">
                <AddIcon />
              </t-button>
              
            </div>

        </div>
        <div class="demo-basic">
          <div v-if="!isEdit">
            <div>
                <div style="font-size: 30px;display: flex; justify-content: space-between;">
                  <div>{{ userName }}</div>
                <t-button v-if="this.myname==this.userName" size="small" @click="this.isEdit=true"> <EditIcon /> edit</t-button>
                
                </div>
                <div style="margin-top: 10px; display: flex; justify-content: center;">
                    <a-button v-if="this.myname!==this.userName" shape="round" style="width: 90%; background-color: bisque;">Follow</a-button>
                </div>
            </div>
            <a-space direction="vertical">
            <div style="margin-top: 5px"></div>
            <div style="color: gray ">
                <icon-home />
                <font color="black" > {{" "+organization }}</font>
            </div>
            <div style="color: gray ">
                <icon-location/>
                <font color="black" > {{ " "+location }}</font>
            </div>
            <div style="color: gray">
                <icon-UserGroup />
                <font color="black" style="font-weight:bold;">{{ " "+followers_num }}</font> followers ·  <font color="black" style="font-weight:bold;">{{ following_num}}</font> following
            </div>
            <a-typography-paragraph
                :ellipsis="{
                rows: 2,
                showTooltip: true,
            }"
                type="secondary"
                :style="{marginBottom:'0px',color:'black'}">
                {{ descript }}
            </a-typography-paragraph>
            </a-space>
            </div>
            <div v-else>
              <div style="margin-top: 5px"></div>
            <div style="color: gray; display: flex; justify-content: center; align-items: center; margin-top: 5px; ">
                <UserIcon size="large"  style="margin-right: 2px;"/>
                <t-input disabled clearable v-model="this.myname" placeholder="Modify yourname." ></t-input>
            </div>
            <div style="color: gray; display: flex; justify-content: center; align-items: center; margin-top: 5px; ">
                <icon-home size="large"  style="margin-right: 2px;"/>
                <t-input clearable v-model="editInfo.organization" placeholder="Modify organization." ></t-input>
            </div>
            <div style="color: gray; display: flex; justify-content: center; align-items: center; margin-top: 5px; ">
                <icon-location size="large"  style="margin-right: 2px;"/>
                <t-input clearable v-model="editInfo.location" placeholder="Modify location." ></t-input>
            </div>
            <div style="color: gray; display: flex; justify-content: center; align-items: center; margin-top: 5px; ">
                <EditIcon size="large"  style="margin-right: 2px;"/>
                <t-input clearable v-model="editInfo.descript" placeholder="Modify description." ></t-input>
            </div>
            <div style="display: flex; justify-content: center; align-items: center; margin-top: 5px; ">
              <t-button theme="primary" @click="confirmEdit" style="margin-right: 10px;">确定</t-button>
              <t-button theme="danger" @click="cancelEdit">取消</t-button>
            </div>
            
           
            </div>
        </div>
      <a-divider/>
      </div>
        <div style="margin-top: 5px">
          <div style="margin-left: 30px;">
            <div><font color="black" style="font-weight:bold" >Achievement</font></div>
            <t-avatar-group size="large" :max="5" style="margin-top: 5px;" collapseAvatar="more">
                <t-avatar :image="userImg"></t-avatar>
                <t-avatar :image="userImg"></t-avatar>
                <t-avatar :image="userImg"></t-avatar>
                <t-avatar :image="userImg"></t-avatar>
                <t-avatar :image="userImg"></t-avatar>
                <t-avatar>Avatar</t-avatar>
                <t-avatar :icon="icon"></t-avatar>
            </t-avatar-group>
          </div>
        </div>
   
    <a-divider/>

      
</template>

<script>
import request from "@/utils/request";
import {BACKEND_URL} from "@/utils/config";
import defaultText from "@/config/default";
import {AddIcon, EditIcon, UserIcon} from 'tdesign-icons-vue-next'; 
export default {
  components:{
      AddIcon,
      EditIcon,
      UserIcon,
    },
  data: function(){
    return {
      isEdit: false,
      myname:"GMD",
      editInfo:{
        userName: "",
        location: "",
        organization: "",
        descript:"",
      },
      userName: "GMD",
      userImg: "https://tdesign.gtimg.com/site/avatar.jpg",
      follower_images:[],
      location: "Shenzhen, China",
      followers_num: 100,
      following_num:100,
      organization: "IMED, SUSTech",
      descript:"I am a student from SUSTech.",
    }
  },
  async created(){
    this.myname = localStorage.getItem('username');
    this.userName = this.$route.params.username;
    await request.get(BACKEND_URL + '/getIdByName?name='+this.userName)
            .then(res => {
                try {
                    this.userId = res.data.data.id;
                } catch (e) {
                    Message.error(res.data.msg)
                }
            })
            .catch(res => Message.error(res.response.data.msg));
    var el = document.getElementById("avatar2"); 
    if(this.myname==this.userName){
        this.userImg = BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
      } else{
        this.userImg = BACKEND_URL+"/avatar/"+this.userId;
      }
    console.log(this.userImg);
    el.src = this.userImg;  

    await request.get(BACKEND_URL + '/getProfile', {params:{token:localStorage.getItem('token')}})
            .then(res => {
                try {
                    if (res.data.code === 200) {
                        this.location = res.data.data.location;
                        // this.followers_num = res.data.data.followers_num;
                        // this.following_num = res.data.data.following_num;
                        this.organization = res.data.data.company;
                        this.descript = res.data.data.bio;
                    } else {
                        Message.error(res.data.msg)
                    }
                } catch (e) {
                    Message.error(res.data.msg)
                }
            })
            .catch(res => Message.error(res.response.data.msg))
    await request.get(BACKEND_URL + '/getFollowerCount?token='+localStorage.getItem('token'))
      .then(res => {
          try {
              this.followers_num = res.data.data;
          } catch (e) {
              Message.error(res.data.msg)
          }
      })
      .catch(res => Message.error(res.response.data.msg))
    await request.get(BACKEND_URL + '/getLikeUserCount?token='+localStorage.getItem('token'))
      .then(res => {
          try {
              this.following_num = res.data.data;
          } catch (e) {
              Message.error(res.data.msg)
          }
      })
      .catch(res => Message.error(res.response.data.msg))
  },
  methods:{
    async uploadImage(event){
      const file = event.target.files[0];
      const fileName = file.name;
      const formData = new FormData();
      formData.append('avatar', file); 
      await request.post(BACKEND_URL+'/uploadAvatar?token='+localStorage.getItem('token'), formData)
          .then(response => {
            // 处理上传成功的响应
            // alert("you can use this url to get image: "+response.data.data)
            console.log('上传成功');
            this.userImg = response.data.data;
          })
          .catch(error => {
            // 处理上传失败的错误
            console.error('上传失败', error);
          });
      localStorage.setItem('avatar', this.userImg);
    },
    uploadAvatar(){
        this.$refs.imageInput.click();
    },
    cancelEdit(){
      this.isEdit = false;
      this.editInfo={
        userName: "",
        location: "",
        organization: "",
        descript:"",
      }
    },
    async confirmEdit(){
      await request.post(BACKEND_URL+'/modifyProfile?token='+localStorage.getItem('token'), {bio: this.editInfo.descript, location: this.editInfo.location, company: this.editInfo.organization})
          .then(response => {
            // 处理上传成功的响应
            // alert("you can use this url to get image: "+response.data.data)
            console.log('上传成功');
            this.location = this.editInfo.location;
            this.organization = this.editInfo.organization;
            this.descript = this.editInfo.descript;
          })
          .catch(error => {
            // 处理上传失败的错误
            console.error('上传失败', error);
          });
    }
    
  
  },
  computed: {
    getUserAvatar() {
      if(this.myname==this.userName){
        return BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
      } else{
        console.log(this.userId);
        return BACKEND_URL+"/avatar/"+this.userId;
      
      }
      
    },
  },
}


</script>
<style scoped>
.username {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-top: 20px;

}

.demo-basic {
  padding: 10px;
  width: 80%;
  border-radius: 4px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
  margin-bottom: 10px;
}

.repo-item {
  display: flex;
  grid-template-columns: 1fr 1fr;
}
</style>
