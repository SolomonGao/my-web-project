<template>
    <div class="demo-basic--circle" :style="{marginTop: `${props.marginTop}%`, marginBottom: `${props.marginBottom}%`}">
        <div class="block">
        <el-avatar :src="circleUrl" />
        </div>
    </div>
  </template>
  <script lang="ts" setup>
  import { reactive, toRefs } from 'vue'

  const props = defineProps({
  alignment: {
    type: String,
    default: 'center'
  },
  avatarSize: {
    type: Number,
    default: 30
  },
  marginTop: {
    type: Number,
    default: 70
  },
  marginBottom: {
    type: Number,
    default: 70
  }
})
  
  const state = reactive({
    circleUrl:
      'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
  })
  
  const { circleUrl } = toRefs(state)
  </script>
  
  <style scoped>

  .demo-basic--circle{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  </style>
  