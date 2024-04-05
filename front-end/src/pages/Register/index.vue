<template>
  <div class="register-container">
    <el-form
      :model="registerForm"
      ref="formRef"
      label-width="80px"
      class="register-form"
      :rules="registerRules"
    >
      <h2>Sign up</h2>
          <el-form-item label="nickname" prop="nickName">
          <el-input
            v-model="registerForm.nickName"
            autocomplete="off"
            ref="nickName"
            name="nickName"
            placeholder="please enter a nickname"
          ></el-input>
        </el-form-item>
      <el-form-item label="username" prop="username">
        <el-input
          v-model="registerForm.username"
          autocomplete="off"
          ref="username"
          name="username"
          placeholder="please enter a username"
        ></el-input>
      </el-form-item>
      <el-form-item label="password" prop="userPwd">
        <el-input
          type="password"
          v-model="registerForm.userPwd"
          ref="userPwd"
          name="userPwd"
          autocomplete="off"
          placeholder="please enter a password"
        ></el-input>
      </el-form-item>
      <!-- prop="confirmPassword" -->
      <el-form-item label="comfirm password"  prop="confirmPassword">
        <el-input
          type="password"
          v-model="registerForm.confirmPassword"
          autocomplete="off"
          ref="confirmPassword"
          name="confirmPassword"
          placeholder="please comfirm your password"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="register">Sign up</el-button>
        <el-button type="danger" @click="resetForm">Reset</el-button>
        <el-button type="success" @click="goLogin">login</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'Register'
  })
</script>
<script lang="ts" setup>
import { ref } from "vue"
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus';
import { registerValidateApi, registerApi } from "../../api/index"
const router = useRouter()
// 初始化注册参数
const registerForm = ref({
  username: "",
  userPwd: "",
  confirmPassword: "",
  nickName:''
})
const formRef = ref<FormInstance>()
  // 校验规则
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value.length < 4) {
    callback(new Error('Length of username must be greater than 4 digets'))
  } else {
    callback()
  }
}
// 校验规则
const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('Length of password must be greater than 6 digets'))
  } else {
    callback()
  }
}
// 校验规则
const validateConfirmPassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('Length of password must be greater than 6 digets'))
  } else {
    callback()
  }
}
// 校验规则
const validateNickName = (rule: any, value: any, callback: any) => {
  if (value.length >= 2  && value.length  <= 8  ) {
    callback()
  } else {
    callback(new Error('Length of nickname must be between 2 and 8 digets'))
}
}
// 校验规则
const registerRules = {
  nickName: [{ required: true, trigger: 'blur', validator: validateNickName }],
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
  confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
}

//点击注册的回调
const register = async () => {
  await formRef.value?.validate()
  if (registerForm.value.userPwd == registerForm.value.confirmPassword) {
    // 调用用户名校验接口
    await registerValidateApi(registerForm.value.username)
    //  整理参数
    const obj = {
      username: "",
      userPwd: "",
      nickName: ''
    }
    obj.username = registerForm.value.username
    obj.userPwd = registerForm.value.userPwd
    obj.nickName = registerForm.value.nickName
    //  调用注册接口
    await registerApi(obj)
    formRef.value?.resetFields()
    ElMessage.success("Sign up success")
    } else {
      return ElMessage.error("your password doesn't match the comfirm password")
    }
    
}
//点击去登录的回调
const goLogin = () => {
  router.push({path:"/login"})
}

//点击重置的回调
const resetForm = () => {
  //重置表单
  formRef.value?.resetFields()
}
  
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.register-form {
  width: 400px;
  text-align: center;
}
</style>
