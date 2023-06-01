<template>
    <div class="username">
        <div class="avator">
          <img
                  id="avatar3"
                  :alt="user"
                  :src="userImg"
                  style="height: 230px; width: 230px; border-radius: 230px"
              />
        </div>
        <div class="demo-basic">
            <div>
                <div style="font-size: 30px">{{ userName }}</div>
                <!-- <div style="margin-top: 10px; display: flex; justify-content: center;">
                    <a-button v shape="round" style="width: 85%; background-color: bisque;">Follow</a-button>
                </div> -->
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

        <div style="margin-top: 5px">
            <font color="black" style="font-weight:bold" >Achievement</font>
            <t-avatar-group size="large" :max="5" style="margin-top: 5px" collapseAvatar="more">
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

      <div>
        <a-list :split="false" :bordered="false">
          <template #header>
            <div style="display: flex; flex-direction: row; justify-content: space-between;">
              <div style="font-size: 16px">My article</div>
            </div>
          </template>
          <a-list-item v-for="item in articles">
            <a-list-item-meta>
              <template #avatar>
                <div class="repo-item">

                  <a-link @click="clickArticle(item.id)"
                          :hoverable="false"
                          style="justify-content: flex-start; margin-left: 8px;" >
                      {{ item.articleName }}
<!--                         <icon-ThumbUp />-->
<!--                        {{item.goodnum}}-->
                  </a-link>

                </div>
              </template>
            </a-list-item-meta>

          </a-list-item>
        </a-list>
      </div>
</template>

<script>
import request from "@/utils/request";
import defaultText from "@/config/default";
import {BACKEND_URL} from "@/utils/config";
export default {
  data: function(){
    return {
      userName: "GMD",
      userImg: "",
      follower_images:[],
      location: "Shenzhen, China",
      followers_num: 100,
      following_num:100,
      organization: "IMED, SUSTech",
      descript:"I am a student from SUSTech.",
      articles:[{goodnum:32, articleName:"Atom 使用介绍", id:1},{goodnum:67, articleName:'新手到黑客的最全入门路劲图（附全学习资料下载', id:2}],
    }
  },
  methods:{
    clickArticle(id) {
      // this.$router.push('/edit')
      this.$router.push({path:'/read',query: {id:id}})
      // this.$router.push({name:'/edit',params: {id:id}})
    }
  },
  async created() {
    await request.get(BACKEND_URL + '/getProfile', {params:{token:localStorage.getItem('token')}})
            .then(res => {
                try {
                    if (res.data.code === 200) {
                        this.location = res.data.data.location;
                        // this.followers_num = res.data.data.followers_num;
                        // this.following_num = res.data.data.following_num;
                        localStorage.setItem('userid',res.data.data.id);
                        this.organization = res.data.data.company;
                        this.descript = res.data.data.bio;
                    } else {
                        Message.error(res.data.msg)
                    }
                } catch (e) {
                    Message.error(res.data.msg)
                }
            })
            .catch(res => Message.error(res.response.data.msg));
            await request.get(BACKEND_URL + '/getFollowerCount?token='+localStorage.getItem('token'))
      .then(res => {
          try {
              this.followers_num = res.data.data;
          } catch (e) {
              Message.error(res.data.msg)
          }
      })
      .catch(res => Message.error(res.response.data.msg))

    this.userImg=BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
    this.userName=localStorage.getItem('username');
    var el = document.getElementById("avatar3");
    el.src = BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
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
  computed: {
    getUserAvatar() {
      return BACKEND_URL+"/avatar/"+localStorage.getItem('userid');
    },
  },

}


</script>
<style scoped>
.username {
  margin-top: 20px;
  margin-left: 20px;
  margin-bottom: 20px;

}

.demo-basic {
  padding: 10px;
  width: 80%;
  border-radius: 4px;
  box-shadow: 0 2px 8px 0 rgba(0, 0, 0, 0.15);
}

.repo-item {
  display: flex;
  grid-template-columns: 1fr 1fr;
}
</style>
