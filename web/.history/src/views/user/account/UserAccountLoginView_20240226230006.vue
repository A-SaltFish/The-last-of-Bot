<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form @submit.prevent="login">
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
            v-model="username"
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
            v-model="password"
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
            <div class="error-msg">{{error_msg}}</div>
          </div>
          <div class="mb-3 form-check">
            <input
              type="checkbox"
              class="form-check-input"
              id="exampleCheck1"
            />
            <label class="form-check-label" for="exampleCheck1">自动登录</label>
          </div>
          <button type="submit" class="btn btn-primary" style="width:100%;">登录</button>
        </form>
      </div>
    </div>
  </ContentField>
</template>
    <script>
import ContentField from "@/components/ContentField.vue";
import {useStore} from 'vuex';
import {ref} from 'vue';
import router from '../../../router/index'

export default {
  components: {
    ContentField,
  },
  setup(){
    const store=useStore();
    let username=ref('');
    let password=ref('');
    let error_msg=ref('');

    const login=()=>{
        error_msg.value="";
        store.dispatch("login",{
            username:username.value,
            password:password.value,
            success(res){
                store.dispatch("getinfo",{
                    success(res){
                        router.push({name:'home'});
                    }
                })

            },
            error(){
                error_msg.value="用户名或密码错误！";
            }
        })
    }
    return {
        username,
        password,
        error_msg,
        login,
    }

  }
};
</script>>
    <style scoped>
    div.error-msg{
        color:red;
        font-size: 0.7vw;
    }
</style>
    