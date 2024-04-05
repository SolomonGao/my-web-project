<template>
    <div class="change-password-container">
    <el-form
      :model="changePasswordForm"
      ref="formRef"
      label-width="150px"
      class="change-password-form"
      :rules="changePasswordRules"
    >
      <h2>Change your password</h2>
      <el-form-item label="username" prop="username">
        <el-input
          v-model="changePasswordForm.username"
          autocomplete="off"
          ref="username"
          name="username"
          placeholder="please enter your username"
        ></el-input>
      </el-form-item>
      <el-form-item label="current password" prop="userOldPwd">
        <el-input
          type="password"
          v-model="changePasswordForm.userOldPwd"
          ref="userOldPwd"
          name="userOldPwd"
          autocomplete="off"
          placeholder="please enter your current password"
        ></el-input>
      </el-form-item>
      <el-form-item label="password" prop="userPwd">
        <el-input
          type="password"
          v-model="changePasswordForm.userPwd"
          ref="userPwd"
          name="userPwd"
          autocomplete="off"
          placeholder="please enter your new password"
        ></el-input>
      </el-form-item>
      <!-- prop="confirmPassword" -->
      <el-form-item label="comfirm password"  prop="confirmPassword">
        <el-input
          type="password"
          v-model="changePasswordForm.confirmPassword"
          autocomplete="off"
          ref="confirmPassword"
          name="confirmPassword"
          placeholder="please comfirm your new password"
        ></el-input>
      </el-form-item>
      <el-form-item class="change-passowrd-buttons">
        <el-button type="success" @click="handleComfirm">Comfirm</el-button>
        <el-button type="danger" @click="resetForm">Reset</el-button>
        <el-button type="primary" @click="handleCancel">Cancel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
 import { defineComponent } from 'vue'
  export default  defineComponent({
    name:'ChangePassword'
  })
</script>
<script lang="ts" setup>
import { ref } from "vue"
import { useRouter } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus';
import { registerValidateApi, registerApi, changePasswordValidateApi, changePasswordApi, isUserOverdue } from "../../api/index"
const router = useRouter()
// 初始化注册参数
const changePasswordForm = ref({
  username: "",
  userPwd: "",
  confirmPassword: "",
  userOldPwd:''
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
const validateOldPassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error('Length of password must be greater than 6 digets'))
  } else {
    callback()
}
}
// 校验规则
const changePasswordRules = {
  userOldPwd: [{ required: true, trigger: 'blur', validator: validateOldPassword }],
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
  confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }]
}


const handleComfirm = async () => {
  await formRef.value?.validate()
    //发送请求判断用户是否token过期
  await isUserOverdue()
  if (changePasswordForm.value.userPwd == changePasswordForm.value.confirmPassword && changePasswordForm.value.userPwd != changePasswordForm.value.userOldPwd) {
    // 调用用户名校验接口
    await changePasswordValidateApi(changePasswordForm.value.username, changePasswordForm.value.userOldPwd)
    const obj = {
        username: "",
        userPwd: ""
    }
        obj.username = changePasswordForm.value.username,
        obj.userPwd = changePasswordForm.value.userPwd

    await changePasswordApi(obj)
    ElMessage.success("change password success")
    router.push({ name: "HeadlineNews" });
  } else if (changePasswordForm.value.userPwd != changePasswordForm.value.confirmPassword) {
    ElMessage.error("Your new password doesn't match the comfirm password")
  } else {
    ElMessage.error("Your new password is same as your current password")
  }
}

const handleCancel = () => {
    router.back()
}

const resetForm = () => {
  //重置表单
  formRef.value?.resetFields()
}
  
</script>

<style scoped>
.change-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}
.change-password-form {
  width: 400px;
  text-align: center;
}
.change-passowrd-buttons {
    width: 800px;
}
</style>
