<template>
    <div class="container">
      <div class="listItem">
        <div class="containerItem" v-for="item in pageData" :key="item.hid">
          <div>
            <span class="text">{{ item.title }}</span>
          </div>
          <div class="detail">
            <span>{{ item.type == 1 ? "NEWS":item.type == 2 ? "SPORT": item.type == 3 ? "ET": item.type == 4 ? "TECH" : "OTHERS" }}</span>
            <span>Viewed at {{item.viewDate}}</span>
          </div>
          <div>
            <el-button @click="toDetail(item.hid, userId)" size="small"
              style="background: #198754; margin-left: 15px; color: #bbd3dc">View detail</el-button>
          </div>
        </div>

        <!-- 分页器 -->
        <div style="margin-top: 20px">
          <el-pagination 
          v-model:current-page="historyPageInfo.pageNum"
          v-model:page-size="historyPageInfo.pageSize" 
          @size-change="getPageList"
          @current-change="getPageList"
          :page-sizes="[5,7,10]" 
          background
          layout="prev, pager, next , ->, sizes, total" 
          :total="totalSize" />
        </div>
      </div>
    </div>
</template>
<script>
import {getUserHistoryInfo} from "../../api/index"
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'ShowHistory'
})

</script>
<script setup>
    import {ref, onMounted, watch} from "vue"
    import pinia from "../../stores/index"
    import { useUserInfoStore } from "../../stores/userInfo"
    import { useRouter } from 'vue-router'

    const userInfoStore = useUserInfoStore(pinia)
    const userId = userInfoStore.uid
    const totalSize = ref(0)
    const router = useRouter()

    const pageData = ref([{
        uid: null,
        hid: null,
        viewDate: null,
        title: "",
        type: null
    }])

    const historyPageInfo = ref(
      {
        pageNum: 1,
        pageSize: 5,
        uid: 1
      }
    )

    // 初始化请求分页列表数据
    const getPageList = async () => {
        let result = await getUserHistoryInfo(historyPageInfo.value)
        pageData.value = result.pageInfo.pageData
        historyPageInfo.value.pageNum = result.pageInfo.pageNum
        historyPageInfo.value.pageSize = result.pageInfo.pageSize
        totalSize.value = +result.pageInfo.totalSize
    }

    // 监视初始化参数type的变化,当type发生改变的时候重新发送请求获取列表数据
    watch(() => historyPageInfo.value, () => {
      getPageList()
    }, {
      deep: true,
    })

    onMounted(() => {
        if (userId == -1) {
          router.push({name: "Login"})
        }
        getPageList()
    })

    // 点击查看全文的回调
    const toDetail = (hid, uid) => {
      router.push({ name: "Detail" ,query:{ "hid":hid, "uid":uid }});
    }

</script>

<style lang="less" scoped>
.container {
  width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;

  .listItem {
    .containerItem {
      margin-top: 20px;
      border-radius: 10px;
      border: 2px solid #ebebeb;
      width: 600px;
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
}
</style>