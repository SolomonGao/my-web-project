<template>
    <div class="container">
        <div class="left-container">
        <div class="listItem">
        <!-- 左上方框 -->
        <div class="title" style="font-size:1cm; color: black;">Your Profile</div>
        <div class="info">
            <div class="text">
              <span>nickname: {{ nickName }}</span>
            </div>
        </div>
        </div>
        <div class="listItem">
        <!-- 左下方框 -->
        <div class="title" style="font-size:1cm; color: black;">People you followed</div>
        <div class="friend">
            <span class="followed" v-for="item in followedList">
                nickname: {{ item.nickName }}
                <div v-if="userId != -1">
                  <div>
                    <el-button type="danger" @click="unfollow(userId, item.uid)" size="default" style="margin-bottom: 3px; color: #bbd3dc">unfollow</el-button>
                  </div>
                </div>
            </span>
        </div>
        </div>  
    </div>
        <div class="right-container">
              <div class="title" style="font-size:1cm; color: black;">Your articles</div>
            <!-- 右边框 -->
            <div class="article">
              <div class="listItem">
                <div class="containerItem" v-for="item in pageData" :key="item.hid">
                  <div>
                    <span class="text">{{ item.title }}</span>
                  </div>
                  <div class="detail">
                    <span>{{ item.type == 1 ? "NEWS":item.type == 2 ? "SPORT": item.type == 3 ? "ET": item.type == 4 ? "TECH" : "OTHERS" }}</span>
                  </div>
                  <div>
                    <el-button @click="toDetail(item.hid, userId)" size="small"
                      style="background: #198754; margin-left: 15px; color: #bbd3dc">View detail</el-button>

                      <el-popconfirm @confirm="handlerDelete(item.hid)" :title="`Are you sure you want to delete ${item.title}?`">
                        <template #reference>
                          <el-button    size="small" style="background: #dc3545; color: #bbd3dc">Delete</el-button>
                        </template>
                      </el-popconfirm>

                      <el-button @click="Modify(item.hid)" size="small" style="background: #212529; color: #bbd3dc">Edit</el-button>
                  </div>
                </div>

        <!-- 分页器 -->
          <div style="margin-top: 20px">
            <el-pagination 
            v-model:current-page="pageInfo.pageNum"
            v-model:page-size="pageInfo.pageSize" 
            @size-change="getPageList"
            @current-change="getPageList"
            :page-sizes="[5,7,10]" 
            background
            layout="prev, pager, next , ->, sizes, total" 
            :total="totalSize" />
            </div>
          </div>
        </div>
      </div>
    </div>
   
  

</template>

<script >
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'profile'
  })
</script>
<script setup>
    import Avatar from "../../components/Avatar.vue"
    import { useUserInfoStore } from '../../stores/userInfo'
    import pinia from '../../stores/index'
    import { ElMessage } from 'element-plus'
    import {ref, onMounted, watch} from "vue"
    import {getFollowedList, isUserOverdue, unfollowApi, getUserPages, removeByHid} from "../../api/index"
    import { useRouter } from 'vue-router'

    const userInfoStore = useUserInfoStore(pinia)
    const userId = ref("")
    const nickName = ref("")
    const totalSize = ref(0)
    const router = useRouter()

    const pageData = ref([{
        uid: null,
        hid: null,
        viewDate: null,
        title: "",
        type: null
    }])

    const pageInfo = ref(
      {
        pageNum: 1,
        pageSize: 5,
        uid: 1
      }
    )

    const Modify = (hid) => {
      router.push({ name: "addOrModifyNews", query: { hid } });
    }

    const handlerDelete = async (id) => {
      await removeByHid(id)
      ElMessage.success('Delete success!')
      //重新获取列表请求
      getPageList()
    }

    // 初始化请求分页列表数据
    const getPageList = async () => {
        let result = await getUserPages(pageInfo.value)
        pageData.value = result.pageInfo.pageData
        pageInfo.value.pageNum = result.pageInfo.pageNum
        pageInfo.value.pageSize = result.pageInfo.pageSize
        totalSize.value = +result.pageInfo.totalSize
    }

// 监视初始化参数type的变化,当type发生改变的时候重新发送请求获取列表数据
    watch(() => pageInfo.value, () => {
      getPageList()
    }, {
      deep: true,
    })

    // 点击查看全文的回调
    const toDetail = (hid, uid) => {
      router.push({ name: "Detail" ,query:{ "hid":hid, "uid":uid }});
    }
    
    const followedList = ref([{
      uid: null,
      nickName: null
    }])

    onMounted(() => {
        if (userInfoStore.uid == -1) {
          router.push("Login")
        }

        nickName.value = userInfoStore.nickName
        userId.value = userInfoStore.uid
        getFollowed(userId.value)
        getPageList()
    })

    const getFollowed = async (uid) => {
      const result = await getFollowedList(uid)
      followedList.value = result.followedInfo
    }

    const unfollow = async (uid, followed_id) => {
      console.log(uid)
      console.log(followed_id)
      await isUserOverdue()
      await unfollowApi(uid, followed_id)
      getFollowed(userId.value)
    }
</script>

<style lang="less" scope>
.container{
    display: flex;
    flex-direction: row;
}
.left-container {
  width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;

    .info {
        margin-top: 20px;
        border-radius: 10px;
        border: 2px solid #ebebeb;
        width: 300px;
        height: 100px;
        flex:1;
        align-items: center;
    }

    .friend{
        margin-top: 20px;
        border-radius: 10px;
        border: 2px solid #ebebeb;
        width: 300px;
        flex:3;
        align-items: center;
    }
}

.right-container {
  width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;

}

.listItem {
  /* 这里放置 listItem 的样式 */
}

.followed {
  margin-bottom: 20px;
  margin-top: 20px;
  border-radius: 10px;
  border: 2px solid #ebebeb;
  width: 100px;
  height: 70px;
  display: grid;
  align-content: center;
}

.title {
  margin-left: 15px;
  color: #59646b;
}

.detail span {
  margin-left: 15px;
  color: #8b778a;
  font-size: 14px;
}

.text {
  margin-top: 30px;
  margin-left: 15px;
  color: #8b778a;
  font-size: 20px;
}

.listItem {
    .containerItem {
      margin-top: 20px;
      border-radius: 10px;
      border: 2px solid #ebebeb;
      width: 500px;
      height: 120px;

      div {
        margin-top: 10px;
      }

      .text {
        margin-left: 15px;
        color: #353a3f;
      }

      .detail {
        span {
          margin-left: 15px;
          color: #8b778a;
          font-size: 14px;
        }
      }
    }
  }
</style>