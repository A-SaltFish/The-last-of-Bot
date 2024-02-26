<template>
  <NavBar></NavBar>
  <router-view> </router-view>
</template>

<script>
import NavBar from "./components/NavBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
import $ from "jquery";
export default {
  components: {
    NavBar,
  },
  setup() {
    $.ajax({
      url: "http://127.0.0.1:3000/user/account/token/",
      type: "post",
      data: {
        username: "user",
        password: "123456",
      },
      success(res) {
        console.log(res);
      },
      error(res) {
        console.log(res);
      },
    });
    $.ajax({
      url: "http://127.0.0.1:3000/user/account/info/",
      type: "get",
      headers: {
        Authorization:
          "Bearer " +
          "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NTc1ZmMwM2E5MmY0MGU1ODI2NmFjMDczNmFhMDdmNyIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTcwODk1NTI3NSwiZXhwIjoxNzEwMTY0ODc1fQ.4XIRyR2vjAzJhMJeMf1iIbkeJJ-0ufslErl11c7YPAM",
      },
      success(res) {
        console.log(res);
      },
      error(res) {
        console.log(res);
      },
    });
  },
};
</script>

<style>
body {
  background-image: url("@/assets/images/background.jpg");
  font-size: large;
  background-size: cover;
}
</style>
