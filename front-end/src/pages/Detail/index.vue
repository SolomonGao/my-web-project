<template>
  <div class="seeDetails">
    <div>
      <h4>{{ detailList.title }}</h4>
    </div>
    <div style="margin-right: 250px">
      <span>{{ detailList.typeName }}</span>
      <span>{{ detailList.pageViews }}Views</span>
      <span>{{ detailList.pastHours }}Hours ago</span>
    </div>

    <div style="width: 500px; margin: 20px 0px 0px 70px">
      <p>
        {{ detailList.article }}
      </p>
    </div>

    <div style="margin-left: 300px">
      <span>Author: {{ detailList.nickName }}</span>
      <div v-if="userId != -1 && userId != detailList.publisher">
        <div v-if="followed">
          <el-button type="danger" @click="unfollow(userId, detailList.publisher)" size="default" style="margin-bottom: 3px; color: #bbd3dc">unfollow</el-button>
        </div>
        <div v-else>
          <el-button type="primary" @click="follow(userId, detailList.publisher)" size="default" style="margin-bottom: 3px; color: #bbd3dc">follow</el-button>
        </div>
      </div>
      
      
    </div>
  </div>
</template>

<script >
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'Detail'
  })
</script>
<script  setup>
import { getshowHeadlineDetail, followApi, unfollowApi, checkFollowedApi } from "../../api/index"
import { ref , onMounted, onBeforeMount } from "vue"
import { useRoute, useRouter } from 'vue-router'
import pinia from '../../stores/index';
import { useUserInfoStore } from '../../stores/userInfo'
import { isUserOverdue } from '../../api/index'
const route = useRoute() // 路由信息对象
const router = useRouter()

const userInfoStore = useUserInfoStore(pinia)

const followed = ref(false)
const detailList = ref({}) //详情数据
const userId = ref("")
//获取详情初始化数据
const getDetailList = async () => {
  let result = await getshowHeadlineDetail(route.query.hid, route.query.uid)
  detailList.value = result.headline
}


// 页面初始化钩子
onMounted(async () => {
  await getDetailList()
  userId.value = userInfoStore.uid
  checkFollowed(userId.value, detailList.value.publisher)
})

const checkFollowed = async (uid, publisher) => {
  var result = await checkFollowedApi(uid, publisher)
  console.log(result.followed)
  followed.value = result.followed 
}

const follow = async (uid, followed_id) => {
    //发送请求判断用户是否token过期
  await isUserOverdue()
  await followApi(uid, followed_id)
  followed.value = ! followed.value
}

const unfollow = async (uid, followed_id) => {

  await isUserOverdue()
  await unfollowApi(uid, followed_id)
  followed.value = ! followed.value
}

  
</script>

<style lang="less" scoped>
.seeDetails {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  div {
    span {
      padding-right: 15px;
      font-size: 14px;
      color: #8d91aa;
    }
    p {
      font-size: 14px;
      color: #2b2e30;
    }
  }
}
</style>
