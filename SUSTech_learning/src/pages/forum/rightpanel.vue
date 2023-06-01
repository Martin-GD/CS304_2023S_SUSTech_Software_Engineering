<template>
    <div>
      <!-- 创作者中心 -->
        <t-card  :style="{ width:'100%' }">
            <div style="display:flex; flex-direction: row; justify-content: space-between;">
                <div>
                    <Edit1Icon />
                    <font style="margin-left: 5px; margin-right: 5px;">创作中心</font>
                    <t-tag variant="solid" shape="square" size="small" color="blue">{{ userLevel }}</t-tag>
                </div>
                <div>
                  <t-button shape="rectangle" variant="text" style="margin-top: -5px;" @click="openDraftboxDialog">
                    <font style="color: rgb(138, 138, 134);">草稿箱({{draftNum}})</font>
                  </t-button>
                  <t-dialog
                    v-model:visible="draftboxVisible"
                    :header="draftHeader"
                    attach="body"
                    :footer="false"
                    :on-esc-keydown="closeDraftboxDialog"
                    :on-close-btn-click="closeDraftboxDialog"
                    :on-close="closeDraftboxDialog"
                    width="800px"
                    placement="top"
                  >
                  <div style="overflow-y:scroll; height: 400px;">
                    <div>
                      <div style="margin-bottom: 8px;"><font style=" font-weight: 600; color: black;">未发布的问题</font></div> 
                      <div v-for="issue in issueDraftList" style="margin-bottom: 40px;">
                        <div>
                        <t-tag>Title:</t-tag> <font style="color: black;">{{issue.title}}</font>
                        <font style="float:right;">发布于{{ issue.time }}</font>
                        </div>
                        <div>
                          <div>
                            {{ issue.content.substring(0,50) }}
                            <font v-if="issue.content.length>50" style="color: cornflowerblue;">......</font>
                          </div>
                        
                          <div style="float: right; margin-bottom:5px;">
                          <t-button theme="primary" variant="outline" size="small" style="width: 50px; margin-right:5px ;" @click="modifyProblem(issue)"> 编辑 </t-button>
                          <t-button theme="primary" variant="outline" size="small" style="width: 50px; margin-right:5px ;" @click="deleteDraft(issue)"> 删除 </t-button>
                          </div>
                        </div>
                        <t-divider />
                      </div>
                    </div>
                    <div style="margin-top: 10px;">
                      <div ><font style=" font-weight: 600; color: black;">未发布的回答</font></div> 
                      <div v-for="answer in answerDraftList" style="margin-top: 8px;">
                        <div>
                        <t-tag>Title:</t-tag> <font style="color: black;">{{answer.title}}</font>
                        <font style="float:right;">发布于{{ answer.time }}</font>
                        </div>
                        <div>
                          {{ answer.content.substring(0,50) }}
                        </div>
                        <div style="display: flex; flex-direction: row; align-items: center; justify-content: space-between;">
                          <div><font style="font-size: small;">{{ answer.commentNum }} 条评论</font> <font style="font-size: small;">{{ answer.loveNume }} 点赞</font></div>
                          <div style="float: right;">
                          <t-button theme="primary" variant="outline" size="small" style="width: 50px; margin-right:5px ;"> 编辑 </t-button>
                          <t-button theme="primary" variant="outline" size="small" style="width: 50px; margin-right:5px ;"> 删除 </t-button>
                          </div>
                        </div>
                        <t-divider />
                      </div>
                    </div>
                    </div>
                  </t-dialog>
                </div>
            </div>
            <div style="margin-top:15px;display:flex; flex-direction: row; justify-content:space-around;">   
                <button class="mybutton" @click="this.openReplyListDialog">
                  <svg width="40" height="40" viewBox="0 0 40 40" class="GlobalWriteV2-navIcon" fill="currentColor">
                    <g fill="#06F" fill-rule="evenodd">
                      <circle cx="20" cy="20" r="20" opacity=".12"></circle>
                      <path fill-rule="nonzero" d="M23.487 10.463c1.896 0 2.583.193 3.277.555a3.824 3.824 0 0 1 1.607 1.573c.371.678.569 1.35.569 3.206v8.472c0 1.855-.198 2.527-.569 3.205a3.824 3.824 0 0 1-1.607 1.573c-.694.363-1.381.556-3.277.556h-6.96c-1.895 0-2.583-.193-3.276-.556a3.824 3.824 0 0 1-1.608-1.573c-.37-.678-.568-1.35-.568-3.205v-8.472c0-1.855.197-2.528.568-3.206.37-.678.915-1.21 1.608-1.573.693-.362 1.38-.556 3.277-.556h6.959Zm0 2.08h-6.96c-1.407 0-1.836.081-2.273.31a1.72 1.72 0 0 0-.735.72c-.234.427-.317.847-.317 2.224v8.472c0 1.377.083 1.796.317 2.224.172.316.412.551.735.72.437.229.866.31 2.274.31h6.959c1.407 0 1.836-.081 2.274-.31a1.72 1.72 0 0 0 .735-.72c.234-.428.317-.847.317-2.224v-8.472c0-1.377-.083-1.797-.317-2.225a1.72 1.72 0 0 0-.735-.72c-.438-.228-.867-.309-2.274-.309Zm-1.991 9.778v1.873h-5.955V22.32h5.955Zm2.977-3.328v1.872h-8.932v-1.872h8.932Zm0-3.33v1.873h-8.932v-1.872h8.932Z"></path>
                    </g>
                  </svg>
                  <div class="GlobalWriteV2-topTitle">回答问题</div>
                </button>
                <t-dialog
                    v-model:visible="replyListVisible"
                    :header="replyListHeader"
                    attach="body"
                    :footer="false"
                    :on-esc-keydown="closeReplyListDialog"
                    :on-close-btn-click="closeReplyListDialog"
                    :on-close="closeReplyListDialog"
                    width="800px"
                    placement="top"
                  >
                  <div style="overflow-y:scroll; height: 400px;">
                    <div v-for="replyTopic in replyTopicList"  style="margin-top: 8px;">
                      <div>
                      <t-avatar :image=replyTopic.hostImg size="40px"></t-avatar>
                      <span style="font-weight: bolder; margin-left: 10px; margin-right: 10px; color: black;"> {{replyTopic.host}} 邀请您回答</span>
                      <t-tag>Title:</t-tag> <font style="color: black;">{{replyTopic.title.substring(0,35)}}</font><font v-if="replyTopic.title.length>35" style="color:rgb(31, 147, 242);">...</font>
                      <font style="float:right;">发布于{{ replyTopic.time }}</font>
                      </div>
                      <div style="display: flex; flex-direction: row; align-items: center; justify-content: space-between;">
                        <div><font style="font-size: small;">{{ replyTopic.replyNum }} 条回答</font> <font style="font-size: small;">{{ replyTopic.loveNume }} 关注</font></div>
                        <div style="float: right;">
                        <t-button theme="primary" style="width: 80px; height:28px; margin-right:5px ; margin-top: -10px;"> 回答 </t-button>
                        </div>
                      </div>
                      <t-divider></t-divider>
                    </div>
                  </div>
                </t-dialog>
                <button class="mybutton">
                  <svg width="40" height="40" viewBox="0 0 40 40" class="GlobalWriteV2-navIcon" fill="currentColor">
                    <g fill="none" fill-rule="evenodd">
                      <circle cx="20" cy="20" r="20" fill="#F4C807" opacity=".12"></circle>
                      <path d="M6 6h28v28H6z"></path>
                      <path fill="#F4C807" fill-rule="nonzero" d="m20.406 11.772-2.172 2.176h-2.29c-1.438 0-1.875.085-2.322.324-.33.176-.575.422-.751.752-.24.448-.324.886-.324 2.326v7.12c0 1.44.085 1.878.324 2.326.176.33.421.576.75.752.421.225.834.314 2.08.323l7.35.001c1.438 0 1.876-.084 2.323-.324.33-.176.575-.422.751-.752.24-.448.324-.886.324-2.326v-4.905l2.172-2.175v7.08c0 1.94-.202 2.643-.58 3.352a3.95 3.95 0 0 1-1.643 1.645c-.708.379-1.41.58-3.346.58h-7.108c-1.936 0-2.639-.201-3.347-.58a3.95 3.95 0 0 1-1.642-1.645c-.378-.71-.58-1.413-.58-3.352v-7.12c0-1.94.202-2.643.58-3.352a3.95 3.95 0 0 1 1.642-1.645c.708-.379 1.41-.58 3.347-.58h4.462Zm6.908-2.053c.384.116.747.338 1.168.759l.188.189c.42.421.642.785.758 1.17a1.98 1.98 0 0 1 0 1.163c-.116.385-.337.749-.758 1.17l-6.9 6.911c-.62.622-.827.81-1.078 1.004-.251.193-.496.34-.784.47-.288.131-.553.226-1.392.48l-1.088.332a1.303 1.303 0 0 1-1.625-1.629l.33-1.09c.255-.84.35-1.104.48-1.393.13-.29.277-.534.47-.785.193-.252.381-.46 1.001-1.081l6.9-6.911c.42-.421.784-.643 1.168-.76a1.97 1.97 0 0 1 1.162 0Zm-3.204 4.096-4.797 4.805c-.547.548-.709.723-.852.91-.112.146-.19.276-.265.443-.097.214-.175.44-.4 1.182l-.094.31.31-.095c.74-.225.965-.303 1.179-.4.167-.076.297-.154.442-.266.187-.143.361-.305.909-.853l4.797-4.805-1.23-1.23Zm2.546-2.43c-.109.033-.23.11-.443.324l-.874.875 1.228 1.231.875-.876c.213-.213.29-.334.323-.444a.24.24 0 0 0 0-.153c-.033-.11-.11-.23-.323-.445l-.189-.188c-.213-.214-.334-.291-.443-.325a.238.238 0 0 0-.154 0Z"></path>
                    </g>
                  </svg>
                  <div class="GlobalWriteV2-topTitle">写文章</div>
                </button>
                <button class="mybutton" @click="openAskProblemDialog">
                  <svg width="40" height="40" viewBox="0 0 40 40" class="GlobalWriteV2-navIcon" fill="currentColor">
                    <g fill="#26BFBF" fill-rule="evenodd">
                      <circle cx="20" cy="20" r="20" opacity=".12"></circle>
                      <path fill-rule="nonzero" d="M21.987 11.686v2.169h-6.125c-1.43 0-1.863.064-2.297.306-.332.128-.574.383-.74.702-.255.447-.332.893-.332 2.297v7.018c0 1.442.09 1.876.332 2.297.166.345.408.587.74.766.434.23.868.319 2.297.319h7.018c1.43 0 1.863-.077 2.297-.32.345-.165.587-.408.766-.74.216-.408.296-.816.305-2.054l.001-6.316.025.025h2.17v6.074c0 1.914-.217 2.616-.587 3.318a3.92 3.92 0 0 1-1.634 1.62c-.689.383-1.403.575-3.317.575h-7.018c-1.915 0-2.616-.204-3.318-.575a3.891 3.891 0 0 1-1.62-1.62c-.384-.702-.575-1.404-.575-3.318v-7.018c0-1.914.204-2.629.574-3.318a3.996 3.996 0 0 1 1.62-1.633c.703-.383 1.404-.574 3.318-.574h6.1Zm1.889 6.954c1.059 1.06 1.059 2.807 0 3.88l-.039.038a2.719 2.719 0 0 1-3.879 0l-2.45-2.553a.801.801 0 0 0-1.123 0l-.05.052c-.32.357-.32.893 0 1.212a.75.75 0 0 0 .726.217c.51-.128 1.047.23 1.149.74a.946.946 0 0 1-.727 1.148 2.649 2.649 0 0 1-2.527-.74 2.796 2.796 0 0 1 0-3.905l.038-.025c1.098-1.085 2.808-1.085 3.892 0l2.463 2.488a.764.764 0 0 0 1.11 0l.038-.025a.855.855 0 0 0 0-1.187.876.876 0 0 0-.74-.217c-.51.128-1.02-.204-1.148-.727-.128-.51.204-1.021.727-1.149l.013-.013a2.703 2.703 0 0 1 2.527.766Zm4.338-9.315v2.578h2.578v1.722h-2.578v2.59h-1.723v-2.602h-2.59v-1.71h2.59V9.325h1.723Z"></path>
                    </g>
                  </svg>
                  <div class="GlobalWriteV2-topTitle">发问题</div>
                </button>
                <t-dialog
                    v-model:visible="askProblemVisible"
                    :header="askProblemHeader"
                    attach="body"
                    :footer="false"
                    :on-esc-keydown="closeAskProblemDialog"
                    :on-close-btn-click="closeAskProblemDialog"
                    :on-close="closeAskProblemDialog"
                    width="800px"
                    placement="top"
                  >
                  <t-form ref="form" :data="askIssueForm">
                    <t-form-item 
                    label="Title:" 
                    :data="askIssueForm.title" 
                    name="title" 
                    :rules="[{ required: true, message: '此项必填' }]"
                    >
                      <t-input v-model="askIssueForm.title" placeholder="请输入标题" />
                    </t-form-item>
                    <t-form-item label="Content:" :data="askIssueForm.content" name="title" >
                      <t-textarea v-model="askIssueForm.content" placeholder="请输入内容" autofocus autosize={minRows:5} />
                    </t-form-item>
                    <div style="margin-top:10px ; display: flex; justify-content: center;">
                    <t-button size="large" style="margin-right: 5px;" @click="saveDraft">存草稿</t-button>
                    <t-button size="large" style=" margin-left: 2px;" theme="success" @click="askIssue">确认</t-button>
                    </div>
                  </t-form>
                </t-dialog>
            </div> 
            <div :style="{ 'width': '100%', 'margin-top':'10px' }">
              <t-swiper :duration="800" :interval="2000">
                <t-swiper-item>
                  <div :style="{ height: '130px', background: 'var(--td-success-color-7)' }" />
                </t-swiper-item>
                <t-swiper-item>
                  <div :style="{ height: '130px', background: 'var(--td-warning-color-7)' }" />
                </t-swiper-item>
                <t-swiper-item>
                  <div :style="{ height: '130px', background: 'var(--td-error-color-7)' }" />
                </t-swiper-item>
              </t-swiper>
            </div>   
        </t-card>

      <!-- 推荐列表 -->
        <t-card  :style="{ width:'100%', 'margin-top':'10px' }">
          <div class="css-cd61lo"><svg width="1.2em" style="margin-right: 4px;" height="1.2em" viewBox="0 0 24 24"  fill="currentColor"><path fill-rule="evenodd" d="M5.5 7.5A5.5 5.5 0 0 1 11 2a5.5 5.5 0 0 1 5.5 5.5A5.5 5.5 0 0 1 11 13a5.5 5.5 0 0 1-5.5-5.5Zm8.11 9.498c.404-.408.91-1 1.17-1.51.067-.133.13-.284.165-.442.034-.15.058-.373-.033-.602a.872.872 0 0 0-.545-.509 1.37 1.37 0 0 0-.604-.043c-.657.082-1.518.184-2.373.24-.867.055-1.68.058-2.254-.041-1.189-.204-2.045-.19-2.781.087-.722.272-1.25.773-1.804 1.302-1.533 1.462-2.434 3.311-2.65 4.831-.11.78.535 1.339 1.199 1.339h8.1a.96.96 0 0 0 .955-.929c.06-1.767.7-2.96 1.456-3.723Zm5.596-2.292a.706.706 0 0 0-1.412 0v2.588h-2.588a.706.706 0 0 0 0 1.412h2.588v2.588a.706.706 0 1 0 1.412 0v-2.588h2.588a.706.706 0 0 0 0-1.412h-2.588v-2.588Z" clip-rule="evenodd"></path></svg>推荐关注</div>
          <t-list split v-for="up in recommendList">
              <div style="display:flex; flex-direction: row; justify-content: space-between; margin-top: 5px; align-items: center;">
                <t-avatar :image="userImg" size=42px shape="round" :hideOnLoadFailed="false" @error="onError"></t-avatar>
                <div style="margin-left: -32px;">
                  <div>
                    名字
                  </div>
                  <div style="font-size: 10px; margin-top: -3px; color: grey;">
                    「人工智能」领域答主
                  </div> 
                </div>
                <t-button style="float: right" ghost size="small">
                    <AddIcon />
                    <span> 关注</span>
                </t-button>
            
              </div>
          </t-list>
        </t-card>
      <!-- 广告 -->
        <div :style="{ 'width': '100%', 'margin-top':'10px' }">
            <t-swiper :duration="800" :interval="2000">
              <t-swiper-item>
                <div :style="{ height: '130px', background: 'var(--td-success-color-7)' }" />
              </t-swiper-item>
              <t-swiper-item>
                <div :style="{ height: '130px', background: 'var(--td-warning-color-7)' }" />
              </t-swiper-item>
              <t-swiper-item>
                <div :style="{ height: '130px', background: 'var(--td-error-color-7)' }" />
              </t-swiper-item>
            </t-swiper>
          </div> 

      <!-- 网站信息 -->
           <footer class="Footer" role="contentinfo"><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="/question/19581624">Atom指南</a><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="/term/zhihu-terms">Atom协议</a><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="/term/privacy">Atom隐私保护指引</a><br><a class="Footer-item" target="_blank" href="/app">应用</a><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://app.mokahr.com/apply/zhihu">工作</a><span class="Footer-dot"></span><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://zhuanlan.zhihu.com/p/28852607">侵权举报</a><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="http://www.12377.cn">网上有害信息举报专区</a><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://tsm.miit.gov.cn/dxxzsp/">京 ICP 证 xxxxxx 号</a><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://beian.miit.gov.cn/">京 ICP 备 xxxxxxx 号 - 1</a><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010802020088"><img src="https://pic3.zhimg.com/80/v2-d0289dc0a46fc5b15b3363ffa78cf6c7.png" alt="">京公网安备 xxxxxxxxxxxxxxx 号</a><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://www.zhihu.com/certificates">京网文[2023]2674-xxx 号</a><br><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://pic3.zhimg.com/v2-c280f8bce57f9b045b83185384d86027.png">药品医疗器械网络信息服务备案<br>（京）网药械信息备字（2023）第00000号</a><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="https://www.zhihu.com/certificates">广播电视节目制作经营许可证:（京）字第000xx0号</a><span class="Footer-item">服务热线：400-909-0000</span><span class="Footer-item">违法和不良信息举报：010-00000000</span><a class="Footer-item" href="mailto:jubao@zhihu.com">举报邮箱：12011527@mail.sutech.edu.cn</a><br><a class="Footer-item" target="_blank" href="/term/child-jubao">儿童色情信息举报专区</a><br><a class="Footer-item" target="_blank" href="/term/algorithm-recommend-report">互联网算法推荐举报专区</a><br><a class="Footer-item" target="_blank" href="/term/pension-swindle-report">养老诈骗举报专区</a><br><a class="Footer-item" target="_blank" href="/term/mcn/report">MCN 举报专区</a><br><a class="Footer-item" target="_blank" href="/term/info-sec">信息安全漏洞反馈专区</a><br><a class="Footer-item" target="_blank" href="/term/report-content-idustry">内容从业人员违法违规行为举报</a><br><a class="Footer-item" target="_blank" href="/term/net-report">网络谣言信息举报入口</a><br><a class="Footer-item" target="_blank" href="/term/self-media-chaos-report">自媒体乱象举报专区</a><br><a class="Footer-item" target="_blank" href="/term/cyber-communication-order">网络传播秩序举报专区</a><br><a class="Footer-item" target="_blank" href="/certificates">证照中心</a><span class="Footer-dot"></span><a class="Footer-item" target="_blank" rel="noopener noreferrer" href="//ir.zhihu.com">Investor Relations</a><br><a class="Footer-item" target="_blank" href="/contact">联系我们</a><span> © 2023 Atom</span><br><span class="Footer-item">SUSTech版权所有</span><br><img loading="lazy" width="80" height="38" src="https://pica.zhimg.com/80/v2-ccdb7828c12afff31a27e51593d23260_720w.png" alt="本站提供适老化无障碍服务" class="css-1170n61"></footer> 
  
      </div>
    
  
</template>
  
<script>
import request from '@/utils/request';
 import { ArrowUpIcon,Edit1Icon, AddIcon} from 'tdesign-icons-vue-next';
 import {BACKEND_URL} from '@/utils/config';
  export default {
    components:{
        Edit1Icon,
        AddIcon,
        ArrowUpIcon
    },
    data: function(){
      return {
        draftExist:false,
        draftId:0,
        userLevel:"Lv.1",
        draftNum:5,
        userImg: "https://tdesign.gtimg.com/site/avatar.jpg",
        recommendList:[1,2,3,4], 
        draftboxVisible:false,    
        draftHeader:"草稿箱", 
        replyListVisible:false,
        replyListHeader:"回答列表",
        askProblemVisible:false,
        askProblemHeader:"提问",
        askIssueForm:{
          title:"",
          content:"",
        },
        issueDraftList:[
          {
            id:1,
            title:"第2个topic!",
            host:"Wyatt222",
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
          {
            id:1,
            title:"第2个topic!",
            host:"Wyatt222",
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
          {
            id:1,
            title:"第3个topic!",
            host:"Wyatt222",
            content:"问题是这样的,这是第2个问题。这个问题描述很长很长很长问题是这样的,这是第2个问题。这个问题描述很长很长很长问题是这样的,这是第2个问题。这个问题描述很长很长很长问题是这样的,这是第2个问题。这个问题描述很长很长很长问题是这样的,这是第2个问题。这个问题描述很长很长很长",
            time:"2020-10-11 20:30",
          },
          {
            id:1,
            title:"第4个topic!",
            host:"Wyatt222",
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
          
        ],
        answerDraftList:[
          {
            title:"第2个topic!",
            commentNum:140,
            loveNume:223,
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
          {
            title:"第2个topic!",
            commentNum:140,
            loveNume:223,
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
        ],

        replyTopicList:[
          {
            title:"第2个topic!",
            host:"Wyatt222",
            hostImg:"https://tdesign.gtimg.com/site/avatar.jpg",
            replyNum:140,
            loveNume:223,
            time:"2020-10-11 20:30",
          },
          {
            title:"第333333333333333333333333333333333333333333333333333333333个topic!",
            host:"Wyatt",
            hostImg:"https://tdesign.gtimg.com/site/avatar.jpg",
            replyNum:140,
            loveNume:223,
            content:"问题是这样的,这是第2个问题。。。。。。。。。。。。。。。。",
            time:"2020-10-11 20:30",
          },
        ]         
      }
    },
    methods: {
      closeDraftboxDialog(){
        this.draftboxVisible = false;
      },
      async openDraftboxDialog(){  
        var respond; 
        await request.post(BACKEND_URL+'/getDraftList?user='+localStorage.getItem("userid")).then(res=>{
          respond = res.data.data;
        })  
        this.issueDraftList=[];
        for(var i=0; i<respond.length; i++){
          var tmp = {id:respond[i].id, title:respond[i].title, content:respond[i].content, time:respond[i].createdAt};
          this.issueDraftList.push(tmp);
        }
        this.draftboxVisible = true;
      },
      closeReplyListDialog(){
        this.replyListVisible = false;
      },
      openReplyListDialog(){
        this.replyListVisible = true;
      },
      closeAskProblemDialog(){
        this.draftExist = false;
        this.askProblemVisible = false;
      },
      openAskProblemDialog(){
        this.askProblemVisible = true;
      },
      async askIssue(){
        if (this.draftExist){
          await request.post(BACKEND_URL+'/postDraft?'+"issue="+this.draftId).then((respond) => {
            console.log(respond.data.data);
          });
        } else{
          await request.post(BACKEND_URL+'/createIssue?content='+ this.askIssueForm.content+"&title="+this.askIssueForm.title+"&user="+localStorage.getItem("userid")).then((respond) => {
            console.log(respond.data.data);
          });
        }
        
        this.askProblemVisible = false;
        this.askIssueForm.content = "";
        this.askIssueForm.title = ""; 
        this.draftExist = false;
      },
      async saveDraft(){
        if (this.draftExist){
          await request.post(BACKEND_URL+'/modifyDraft?content='+ this.askIssueForm.content+"&issue="+this.draftId+"&title="+this.askIssueForm.title).then((respond) => {
            console.log(respond.data.data);
          });
        } else{
          await request.post(BACKEND_URL+'/initDraft?content='+ this.askIssueForm.content+"&title="+this.askIssueForm.title+"&user="+localStorage.getItem("userid")).then((respond) => {
            console.log(respond.data.data);
          });
        }
        this.askProblemVisible = false;
        this.askIssueForm.content = "";
        this.askIssueForm.title = ""; 
        this.draftExist = false;
      },
      modifyProblem(issue){
        this.askIssueForm.content = issue.content;
        this.askIssueForm.title = issue.title;
        this.draftExist = true;
        this.draftId = issue.id;
        this.draftboxVisible = false;
        this.askProblemVisible = true;
      },
      async deleteDraft(issue){
        await request.post(BACKEND_URL+'/deleteDraft?issue='+issue.id).then((respond) => {
            console.log(respond.data.data);
          });
        this.draftboxVisible = false;
      },
      
    }

  }
  

  </script>
  
  <style scoped>
  .GlobalWriteV2-navIcon{
    margin-bottom: 12px;
    color: #8590a6;
    margin: 0 auto;
    width: 50px;
  }



  .mybutton{
    background:transparent;	
    border-width:0px;	
    
  }
  .mybutton:active{
    background:gainsboro;	
   
  }
  .Footer, html[data-theme=dark] .Footer {
    color: #8590a6;
  }
  .Footer {
      font-size: 13px;
      line-height: 2;
      margin: 10px 0;
      padding-left: 5px;
  }

  footer {
      display: block;
  }
  .Footer-item {
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    display: -webkit-inline-box;
    display: -ms-inline-flexbox;
    display: inline-flex;
}

a {
    color: inherit;
    text-decoration: none;
}
.css-cd61lo {
    box-sizing: border-box;
    margin: 0px 0px ;
    min-width: 0px;
    -webkit-box-align: center;
    align-items: center;
    color: rgb(68, 68, 68);
    font-weight: 500;
    font-size: 14px;
    display: flex;
}



  
  

  
 
  </style>