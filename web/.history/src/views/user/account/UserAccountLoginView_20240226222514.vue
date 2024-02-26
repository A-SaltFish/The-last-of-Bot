<template>
  <ContentField>
    <div class="row justify-content-md-center">
      <div class="col-3">
        <form>
          <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input
              type="text"
              class="form-control"
              id="username"
              placeholder="请输入用户名"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              type="password"
              class="form-control"
              id="password"
              placeholder="请输入密码"
            />
            <div class="error-msg">密码错误</div>
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
export default {
  components: {
    ContentField,
  },
};
</script>>
    <style scoped>
    div.error-msg{
        color:red;
        font-size: 0.7vw;
    }
</style>
    