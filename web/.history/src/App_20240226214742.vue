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
          "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlZTQ3YThjMWYxZDU0MjU1OGRhMWVkMzc2NWM5YzIxZSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcwODk1NDg0OSwiZXhwIjoxNzEwMTY0NDQ5fQ.OmbRJwM9ISTgJrKz7hNGcXYYgnpGoWqQUp6YNnoyzm8",
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
