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
              aria-describedby="emailHelp"
            />
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">密码</label>
            <input
              type="password"
              class="form-control"
              id="password"
            />
          </div>
          <div class="mb-3 form-check">
            <input
              type="checkbox"
              class="form-check-input"
              id="exampleCheck1"
            />
            <label class="form-check-label" for="exampleCheck1">Check me out</label>
          </div>
          <button type="submit" class="btn btn-primary">Submit</button>
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
</style>
    