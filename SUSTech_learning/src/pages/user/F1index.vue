<template>
    <div class="page">
      <div class="leftpanel" >
          <leftpanel></leftpanel>
      </div>
      <div class="rightpanel">
        <t-tabs :value="value" :theme="theme" @change="(newValue) => (value = newValue)">
          <t-tab-panel value="first">
            <template #label> <HomeIcon style="margin-right: 4px"/> <font style="color:black">Overview</font> </template>
            <div v-show="this.isMyPage" style="display: flex; justify-content: space-between; margin-top: 5px;">
              <div style="margin-left: 10px;">Your repositories</div>
              <t-button style="margin-right: 25px;" @click="this.createRepoVisible=true;">创建仓库</t-button>
            </div>
            <t-dialog
            v-model:visible="createRepoVisible"
            :header=this.createRepoHeader
            attach="body"
            :footer="false"
            :on-esc-keydown="closeCreateRepoDialog"
            :on-close-btn-click="closeCreateRepoDialog"
            :on-close="closeCreateRepoDialog"
            width="600px"
            placement="top"
            >
                  <t-form ref="form" :data="createRepoForm">
                    <t-form-item
                    label="Title:"
                    :data="createRepoForm.title"
                    name="title"
                    :rules="[{ required: true, message: '此项必填' }]"
                    >
                      <t-input v-model="createRepoForm.title" placeholder="请输入标题" />
                    </t-form-item>
                    <t-form-item label="Description:" :data="createRepoForm.description" name="Description:" >
                      <t-textarea v-model="createRepoForm.description" placeholder="请输入内容" autofocus :autosize="{minRows:5}" />
                    </t-form-item>

                      <div style="display: flex; margin-right: auto;">
                        <font style="color: black; margin-top: 2px;">添加标签：</font>
                        <t-input style="width: 370px;" v-model="this.newTag" placeholder="输入标签" />
                        <t-button style=" margin-left: 2px;" theme="success" @click="addTag">确认添加</t-button>
                      </div>
                      <div style="margin-top: 10px;"><t-tag-input v-model="createRepoForm.tags" placeholder="Define some tags." :tag-props="{ theme: 'primary' }"  /></div>
                      <div style="margin-top: 10px;display: flex; justify-content: center;"><t-button  style=" margin-left: 2px;"  @click="createRepo">确认</t-button></div>

                  </t-form>
            </t-dialog>

            <div v-for="i in Math.ceil(repoList.length/2)">
              <div style="display:flex; flex-direction: row;">
                <div v-for="j in 2" style="margin-left: 5px; margin-right: 5px; margin-top: 5px; margin-bottom: 5px;">
                  <div v-if="((i-1)*2+j-1)<this.repoList.length">
                    <t-card @click="clickRepo(this.repoList[(i-1)*2+j-1].id)" hover-shadow :style="{ width: '380px' }">
                      <div v-if="this.isMyPage">
                        <font style="font-size: medium; font-weight:550;">{{this.repoList[(i-1)*2+j-1].name}}</font>
                        <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue;">{{this.repoList[(i-1)*2+j-1].ownerName}}</t-tag>
                        <t-button @click.stop="deleteRepo(this.repoList[(i-1)*2+j-1].id)" size="small" variant="base" theme="danger" style="float: right; margin-top: -5px;">
<!--                         <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="this.repoList[(i-1)*2+j-1].isStar" />-->
<!--                         <StarIcon v-else/>-->
                          Delete
                        </t-button>
                      </div>
                      <div v-else>
                        <font style="font-size: medium; font-weight:550;">{{this.repoList[(i-1)*2+j-1].name}}</font>
                        <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue;">{{this.repoList[(i-1)*2+j-1].ownerName}}</t-tag>
                        <t-button @click.stop="starRepo(this.repoList[(i-1)*2+j-1])" size="small" variant="base" theme="default" style="float: right; margin-top: -5px;">
                          <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="this.repoList[(i-1)*2+j-1].isStar" />
                          <StarIcon v-else/>
                          Star
                        </t-button>
                      </div>
                      <t-tag v-for="tag in this.repoList[(i-1)*2+j-1].tags" size="samll" shape="round" style="margin-left: 3px; margin-top: 3px; background-color: rgb(247, 241, 177);">{{tag.name}}</t-tag>
                    </t-card>
                  </div>
                </div>
              </div>
            </div>
            <t-divider></t-divider>

          </t-tab-panel>
          <t-tab-panel value="second">
            <template #label> <RootListIcon style="margin-right: 4px"  /><font style="color:black">NoteList</font></template>
            <div v-show="this.isMyPage" style="display: flex; justify-content: space-between; margin-top: 5px;">
              <div style="margin-left: 10px;">Your repositories</div>
              <t-button style="margin-right: 25px;" @click="this.createNoteVisible=true;">创建笔记</t-button>
            </div>
            <t-dialog
                v-model:visible="createNoteVisible"
                :header=this.createNoteHeader
                attach="body"
                :footer="false"
                :on-esc-keydown="closeCreateRepoDialog"
                :on-close-btn-click="closeCreateRepoDialog"
                :on-close="closeCreateRepoDialog"
                width="600px"
                placement="top"
            >
              <t-form ref="form" :data="createNoteForm">
                <t-form-item
                    label="Title:"
                    :data="createNoteForm.title"
                    name="title"
                    :rules="[{ required: true, message: '此项必填' }]"
                >
                  <t-input v-model="createNoteForm.title" placeholder="请输入标题" />
                </t-form-item>
                <t-form-item label="Description:" :data="createNoteForm.description" name="Description:" >
                  <t-textarea v-model="createNoteForm.description" placeholder="请输入内容" autofocus :autosize="{minRows:5}" />
                </t-form-item>

                <div style="display: flex; margin-right: auto;">
                  <font style="color: black; margin-top: 2px;">添加标签：</font>
                  <t-input style="width: 370px;" v-model="this.newTag" placeholder="输入标签" />
                  <t-button style=" margin-left: 2px;" theme="success" @click="addNoteTag">确认添加</t-button>
                </div>
                <div style="margin-top: 10px;"><t-tag-input v-model="createNoteForm.tags" placeholder="Define some tags." :tag-props="{ theme: 'primary' }"  /></div>
                <div style="margin-top: 10px;display: flex; justify-content: center;"><t-button  style=" margin-left: 2px;"  @click="createNote">确认</t-button></div>

              </t-form>
            </t-dialog>
            <t-divider></t-divider>
            <div v-for="note in noteList">
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                  <div style="margin-left: 10px;" @click="clickNote(note.id)">
                    <font style="font-size: medium; font-weight:550; color:#0969da;">{{note.name}}</font>
                    <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue; color: black; border-color: rgb(220, 215, 215);">{{note.owner}}</t-tag>
                  </div>
                  <p style="margin-left: 10px; color: rgb(78, 71, 71);">{{ note.description }}</p>
                  <div style="margin-left: 10px;">
                    <t-tag v-for="tag in note.tag" size="samll" shape="round" style="margin-left: 3px; margin-top: 3px; background-color: rgb(247, 241, 177);">{{tag.name}}</t-tag>

<!--                    <t-tag style="background-color: rgb(235, 230, 230); border-color: rgb(186, 181, 181);">-->
<!--                      <span class="repo-language-color" style="background-color: #f1e05a; margin-right:2px"></span>-->
<!--                      <font>{{note.tag}}</font>-->
<!--                    </t-tag>-->
                    <font style="margin-left: 10px; color: rgb(97, 95, 95); font-size: smaller;">Update on {{this.transTime(note.updatedAt)}}</font>
                  </div>
                </div>

                <div style="margin-right: 10px;" v-if="this.isMyPage">
                  <t-button @click="deleteNote(note.id)" size="primary" theme="danger" style="float: right; margin-top: -5px;">
                    Delete
                  </t-button>
                  <t-button @click="editNote(note.id)" size="primary" variant="base" theme="primary" style="float: right; margin-top: -5px;">
<!--                    <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="note.isStar" />-->
<!--                    <StarIcon v-else/>-->
                    Edit
                  </t-button>
                </div>
<!--                <div style="margin-right: 10px;" v-else>-->
<!--                  <t-button @click="starNote(note.id)" size="primary" variant="base" theme="primary" style="float: right; margin-top: -5px;">-->
<!--                    <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="note.isStar" />-->
<!--                    <StarIcon v-else/>-->
<!--                    Star-->
<!--                  </t-button>-->
<!--                </div>-->
             </div>
              <t-divider></t-divider>
            </div>
          </t-tab-panel>
          <t-tab-panel value="third" v-show="this.isMyPage">
            <template #label> <StarIcon style="margin-right: 4px"/> <font style="color:black">Star</font> </template>
            <div style="margin-left: 10px;margin-top: 5px;">Stared repositories</div>
            <div v-for="i in Math.ceil(staredRepoList.length/2)">
              <div style="display:flex; flex-direction: row;">
                <div v-for="j in 2" style="margin-left: 5px; margin-right: 5px; margin-top: 5px; margin-bottom: 5px;">
                  <div v-if="((i-1)*2+j-1)<this.staredRepoList.length">
                    <t-card @click="clickStarRepo(this.staredRepoList[(i-1)*2+j-1])" hover-shadow :style="{ width: '380px' }">
                      <div >
                        <font style="font-size: medium; font-weight:550;">{{this.staredRepoList[(i-1)*2+j-1].name}}</font>
                        <t-tag size="small" theme="primary" shape="round" variant="outline" style="margin-left: 10px; margin-bottom: 3px; background-color: aliceblue;">{{this.staredRepoList[(i-1)*2+j-1].ownerName}}</t-tag>
                        <t-button @click.stop="starRepo(this.staredRepoList[(i-1)*2+j-1])" size="small" variant="base" theme="default" style="float: right; margin-top: -5px;">
                         <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="this.staredRepoList[(i-1)*2+j-1].isStar" />
                         <StarIcon v-else/>
                          Star
                        </t-button>

                      </div>
                      <t-tag v-for="tag in this.staredRepoList[(i-1)*2+j-1].tags" size="samll" shape="round" style="margin-left: 3px; margin-top: 3px; background-color: rgb(247, 241, 177);">{{tag.name}}</t-tag>
                    </t-card>
                  </div>
                </div>
              </div>
            </div>
            <div style="margin-left: 10px;margin-top: 10px;margin-bottom:-15px ;">Stared Notes</div>
            <t-divider></t-divider>
            <div v-for="note in staredNoteList">
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
                <div style="margin-right: 10px;">
                  <t-button @click="starNote(note)" size="small" variant="base" theme="default" style="float: right; margin-top: -5px;">
                    <StarFilledIcon style="color: rgb(241, 208, 42) ;" v-if="note.isStar" />
                    <StarIcon v-else/>
                    Star
                  </t-button>
                </div>
             </div>
              <t-divider></t-divider>
            </div>
          </t-tab-panel>
        </t-tabs>
      </div>

    </div>


  </template>

  <script>
  import request from '@/utils/request';

  import leftpanel from '@/pages/user/leftpanel.vue';
 import {RootListIcon, HomeIcon, StarIcon, StarFilledIcon } from 'tdesign-icons-vue-next';
  import {BACKEND_URL} from "@/utils/config";
  export default {
    components:{
      RootListIcon,
      HomeIcon,
      StarIcon ,
      leftpanel,
      StarFilledIcon,
    },
    data: function(){
      return {
        pageusername: '',
        pageuserid: '',
        isMyPage: false,
        curRepoId: '',
        newTag:"",
        newTags:[],
        createRepoForm:{title:"", description:"", tags:[]},
        createNoteForm:{title:"", description:"", tags:[]},
        theme: 'normal',
        value: 'first',
        filterInput:"",
        selectValue:"",
        createRepoVisible: false,
        createNoteVisible: false,
        createRepoHeader:"创建新仓库",
        createNoteHeader:"创建新笔记",
        filterTags:["TAG1","tag2"],
        options1: [
        { label: '标签', value: '1' },
        { label: '仓库', value: '2' },
      ],
        repoList:[
          {
            name: "Teedy",
            ownerName: "public",
            tags:["tag12","tag2","tag3"],
            isStar: true,
          },
          {
            name: "Teedy2",
            ownerName: "private",
            tags:["tag1","tag2afsd","tag3"],
            isStar: true,
          },
          {
            name: "Teedy3",
            ownerName: "public",
            tags:["tag1","tag2 asd","tag3","tag37","tag63","tag35","tag34"],
            isStar: false,
          },
        ],
        noteList:[

        ],
        staredRepoList:[
          {
            name: "1111Teedy",
            ownerName: "public",
            tags:["tag12","tag2","tag3"],
            isStar: true,
          },
          {
            name: "111Teedy2",
            ownerName: "private",
            tags:["tag1","tag2afsd","tag3"],
            isStar: true,
          },
          {
            name: "11Teedy3",
            ownerName: "public",
            tags:["tag1","tag2 asd","tag3","tag37","tag63","tag35","tag34"],
            isStar: true,
          },
        ],
        staredNoteList:[
          {
            title: "noteA",
            auth: "public",
            description: "This is a description for noteA",
            modifyTime: "May 30, 2020",
            tag:"tag1",
            isStar: true,
          },
          {
            title: "noteB",
            auth: "private",
            description: "This is a description for noteB",
            modifyTime: "Nov 30, 2020",
            tag:"tag2",
            isStar: true,
          },
          {
            title: "noteC",
            auth: "private",
            description: "This is a description for noteC",
            modifyTime: "Nov 30, 2020",
            tag:"tag3",
            isStar: true,
          },
        ],


      }
    },

    mounted() {
      this.pageusername = this.$route.params.username;
      // TODO: check isMyPage
      if (localStorage.getItem('username') === this.pageusername){
        this.isMyPage = true;
        this.getStarNote();
        this.getStarRepo();
      }else {
        this.isMyPage = false;
      }
      this.getRepoList();
      var _this = this;
      setTimeout(() => {
        this.repoList.map(function(repo) {
          _this.checkRepoStar(repo)
        });
        // console.log("repolist")
        // console.log(_this.repoList)

      }, 900);
    },

    methods: {
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
      getRepoList(){
        // alert(this.pageuserid);
        var token = localStorage.getItem('token');
        // var userid = 'gmd';
        console.log(token)
        const _this = this;
        request.get(BACKEND_URL+`/getIdByName?name=${this.pageusername}`).then(resp => {
          _this.pageuserid = resp.data.data.id;
        }).then(() => {
          request.get(BACKEND_URL + `/getRepositoryList?token=${token}&user=${this.pageuserid}`).then(resp => {
            _this.repoList = resp.data.data;
            // console.log(_this.repoList)
          }).then(() => {
          })
        })
      },
      clickRepo(repoId){
        this.value = 'second';
        this.curRepoId = repoId;
        // var userid = 'test';
        // alert(localStorage.getItem('token'));
        this.getNoteList(repoId);
      },
      getNoteList(repoId){
        const _this = this
        var token = localStorage.getItem('token');
        request.get(BACKEND_URL+`/getNoteList?repo=${repoId}&token=${token}`).then(resp => {
          _this.noteList = resp.data.data
          console.log(resp.data.data)
        }).then(() => {

        })
      },
      clickNote(noteid){
        const url = '/read?id='+noteid; // 设置要打开的URL
        this.$router.push(url)
        // window.open(url);
      },
      clickStarRepo(repo){
        const url = '/user/'+repo.ownerName; // 设置要打开的URL
        // this.$router.push(url)
        location.href = url;
        // window.open('http://10.26.142.228:8080'+url)
      },
      editNote(noteid){
        const url = '/edit?id='+noteid; // 设置要打开的URL
        this.$router.push(url)
        // window.open(url);
      },
      getStarNote(){
        const _this = this
        // TODO
        var token = localStorage.getItem('token');

        // var userid = 'test';
        request.get(BACKEND_URL+`/getStarNotes?token=${token}`).then(resp => {
          _this.staredNoteList = resp.data.data.map(note => {
            return { ...note, isStar: true };
          });
          // console.log(_this.staredNoteList)
        }).then(() => {

        })
      },
      getStarRepo(){
        const _this = this
        // TODO
        var token = localStorage.getItem('token');
        var userid = localStorage.getItem('userid');
        request.get(BACKEND_URL+`/getStarRepositoryList?token=${token}`).then(resp => {
          _this.staredRepoList = resp.data.data.map(repo => {
            return { ...repo, isStar: true };
          });
          console.log("starrepo");
          console.log(_this.staredRepoList)
        }).then(() => {
        })
      },
      createRepo(){
        var _this = this;
        request.post(BACKEND_URL+`/addRepository?auth=${1}&name=${this.createRepoForm.title}&token=${localStorage.getItem('token')}`,
            JSON.stringify(this.createRepoForm.tags), {
          headers: {
                'Content-Type': 'application/json'
              }}
         ).then(resp=>{
          alert("create successful!");
          _this.createRepoVisible = false;
          _this.createRepoForm = {title:"", description:"", tags:[]};
          _this.getRepoList();
        })
      },
      deleteRepo(repoId){
        var _this = this;
        var formData = new FormData();
        formData.append('repoId', repoId);
        formData.append('token', localStorage.getItem('token'));
        request.post(BACKEND_URL+`/deleteRepository`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }}
        ).then(resp=>{
          alert("delete successful!")
          _this.getRepoList();
        })
      },
      addTag(){
        this.createRepoForm.tags.push(this.newTag);
        this.newTag = "";
      },
      addNoteTag(){
        this.createNoteForm.tags.push(this.newTag);
        this.newTag = "";
      },
      starRepo(repo){
        const formData = new FormData();
        formData.append('repoId', repo.id)
        // TODO
        formData.append('token', localStorage.getItem('token'))
        if(repo.isStar){
          repo.isStar = false;
          request.post(BACKEND_URL+'/unStarRepository', formData)
              .then(response => {
                console.log("unstarrepo")
              })
              .catch(error => {
              });
        }else {
          repo.isStar = true;
          request.post(BACKEND_URL+'/starRepository', formData)
              .then(response => {
                console.log("starrepo")
              })
              .catch(error => {
              });
        }
      },
      starNote(note){
        // console.log(note)
        // note.isStar = ~note.isStar;
        const formData = new FormData();
        formData.append('note', note.id)
        // TODO
        formData.append('token', localStorage.getItem('token'))
        // console.log(note)
        if(note.isStar){
          note.isStar = false;
          request.post(BACKEND_URL+'/unstarNote', formData)
              .then(response => {
              })
              .catch(error => {
              });
        }else {
          note.isStar = true;
          request.post(BACKEND_URL+'/starNote', formData)
              .then(response => {
              })
              .catch(error => {
              });
        }

      },
      createNote(){
        var _this = this;
        request.post(BACKEND_URL+`/addNote?auth=${1}&description=${this.createNoteForm.description}&title=${this.createNoteForm.title}&token=${localStorage.getItem('token')}&repoId=${this.curRepoId}`,
            JSON.stringify(this.createNoteForm.tags), {
              headers: {
                'Content-Type': 'application/json'
              }}
        ).then(resp=>{
          _this.createNoteVisible = false;
          _this.createNoteForm = {title:"", description:"", tags:[]};
          alert("create successful!");

          _this.getNoteList(_this.curRepoId);
        })
      },
      deleteNote(noteId){
        var _this = this;
        var formData = new FormData();
        formData.append('noteId', noteId);
        formData.append('token', localStorage.getItem('token'));
        request.post(BACKEND_URL+`/deleteNote`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }}
        ).then(resp=>{
          alert("delete successful!")
          _this.getNoteList(_this.curRepoId);
        })
      },
      checkRepoStar(repo){
        request.get(BACKEND_URL+`/checkRepoStar?repoId=${repo.id}&token=${localStorage.getItem('token')}`)
            .then(response => {
              repo.isStar = JSON.parse(response.data.data);
              // console.log(repo.isStar)
            })
            .catch(error => {
            });
      }

    }
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
    width: 350px;
    height: 100%;
    margin-top: 10px;
    margin-left: 8px;
    margin-right: 8px;
  }



  .rightpanel {
    display: flex;
    flex-direction: column;
    width: 800px;
    height: 100%;
    margin-top: 10px;
    margin-left: 7px;
  }
  .repo-language-color {
    position: relative;
    top: 1px;
    display: inline-block;
    width: 12px;
    height: 12px;
    border: 1px solid var(rgba(31,35,40,0.1));
    border-radius: 50%;
}

  </style>
