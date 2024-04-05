<template>
  <el-card class="box-card AddNewsContainer">
    <el-form :rules="newsRules" :model="formData" ref="formRef" size="default">
      <el-form-item label="Title" prop="title">
        <el-input v-model="formData.title" placeholder="Title"></el-input>
      </el-form-item>
      <el-form-item style="margin: 50px 0;" label="Context" prop="article">
        <el-input v-model="formData.article" type="textarea" rows="8"></el-input>
      </el-form-item>
      <el-form-item label="Main"  prop="type">
        <el-select v-model="formData.type" placeholder="Please select article type">
          <el-option v-for="item in article" :label="item.name" :value="item.type">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-form-item>
      <el-button   @click="handlerCancel">Cancel</el-button>
      <el-button type="primary"  @click="handlerSave">Save</el-button>
    </el-form-item>
  </el-card>
</template>

<script>
import { defineComponent } from 'vue'
import { isUserOverdue } from '../../api/index'
export default defineComponent({
  name: 'AddNews'
})
</script>
<script  setup>
import { getFindHeadlineByHid , saveOrAddNews, issueNews } from "../../api/index"
import { ref, onMounted } from "vue"
import { useRoute } from 'vue-router'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
const router = useRouter() 
const route = useRoute() 


const formRef = ref()
// 校验规则
const validateType = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('No title'))
  }
}
// 校验规则
const validateArticle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('no context'))
  }
}
// 校验规则
const validateTitle = (rule, value, callback) => {
  if (value.length) {
    callback()
  } else {
    callback(new Error('no type'))
}
}
// 校验规则
const newsRules = {
  title: [{ required: true, trigger: 'blur', validator: validateTitle }],
  article: [{ required: true, trigger: 'blur', validator: validateArticle }],
  type: [{ required: true, validator: validateType }],
}


const formData = ref({
  hid:null,
  title: "",   // 文章标题
  article: "", // 文章内容
  type: ""     // 文章类别
})
//初始化文章类别数据
const article = [
  {
    type: "1",
    name: "News"
  },
  {
    type: "2",
    name: "Sport"
  },
  {
    type: "3",
    name: "ET"
  },
  {
    type: "4",
    name: "Tech"
  },
  {
    type: "5",
    name: "others"
  }
]
// 如果是点击修改的话 路由就会携带hid参数  就要发送请求 获取数据回显
const clickModifyEcho = async () => {
  if (!route.query.hid)  return
    let result = await getFindHeadlineByHid(route.query.hid)
    formData.value.title = result.headline.title
  formData.value.article = result.headline.article
     
    formData.value.type = result.headline.type === 1 ? "News" : result.headline.type === 2 ? "Sport" : result.headline.type === 3 ? "ET" : result.headline.type === 4 ? "Tech" : "others" 
}
//页面挂载生命周期
onMounted(() => {
  clickModifyEcho()
})
//点击取消的回调
const handlerCancel = () => {
  router.back()
}
//点击保存的回调
const handlerSave = async () => {
  await formRef.value?.validate()
    //发送请求判断用户是否token过期
  await isUserOverdue()
const Obj = {...formData.value}

  //整理请求参数
//  Obj.hid = userInfoStore.uid.toString()  //添加用户id 让后端知道谁添加的
 Obj.hid = route.query.hid  //添加用户id 让后端知道谁添加的
// 判断type类型
 if(Obj.type == "News" ) Obj.type = "1"
 if(Obj.type == "Sport" ) Obj.type = "2"
 if(Obj.type == "ET" ) Obj.type = "3"
 if(Obj.type == "Tech" ) Obj.type = "4"
 if (Obj.type == "Others") Obj.type = "5"
  //发送请求
  if (route.query.hid) {
    await saveOrAddNews(Obj)
    ElMessage.success("Edit Success")
  }
  else {
    await issueNews(formData.value)
    ElMessage.success("Add Success")
    }
    router.back();
}


</script>

<style lang="less" scoped>
.AddNewsContainer {
  width: 600px;
  margin: 150px auto;
}
</style>
