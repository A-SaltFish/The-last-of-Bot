<template>
  <NavBar></NavBar>
<router-view> </router-view>
</template>

<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import $ from 'jquery'
export default{
  components:{
    NavBar
  },
  setup(){
    $.ajax({
      url:"http://127.0.0.1:3000/user/account/token/",
      type:"post",
      data:{
        username:"lhc",
        password:"123456",
      },
      success(res){
        console.log(res)
      },
      error(res){
        console.log(res)
      }
    })
  }
}
</script>

<style>
body{
  background-image: url("@/assets/images/background.jpg");
  font-size: large;
  background-size:cover
}
</style>
