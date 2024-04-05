<template>
    
  <div >
    <Header v-show="isHeader"></Header>
    <router-view></router-view>
  </div>

</template>


<script >
import { defineComponent } from 'vue'
import Header from "./components/Header.vue"
  export default  defineComponent({
    name:'App'
  })
</script>
<script  setup>
import { computed } from "vue"
import { useRoute } from 'vue-router'
const route = useRoute() // 路由信息对象
// 判断是否显示header组件
 const isHeader =  computed(() => {
    return route.name !== "Login" && route.name !== "Register" && route.name !== "addNews"&& route.name !== "changePassword";
})
  
</script>

<style lang="less" scoped>
  
</style>