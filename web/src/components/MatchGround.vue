<template>
  <div class="matchground">
    <div class="row">
      <div class="col">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="">

        </div>
        <div class="user-name">{{$store.state.user.username}}</div>
      </div>
      <div class="col">
        <div class="user-select-option">
          <select v-model="select_option" class="form-select" aria-label="Default select example">
            <option value="-1" selected>御驾亲征</option>
            <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{bot.title}}</option>
          </select>
        </div>
        <div class="user-status" v-if="$store.state.pk.status==='match-success'">
          匹配成功！
        </div>
        <div class="user-status" v-else>
          等待匹配...
        </div>
      </div>
      <div class="col">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="">

        </div>
        <div class="user-name">{{$store.state.pk.opponent_username}}</div>
      </div>
      <div class="col-12" style="text-align: center;padding-top: 15vh;">
        <button type="button" :class="btn_style" @click="click_match_btn">{{match_btn_info}}</button>
      </div>
    </div>


  </div>
</template>

<script>

  import { ref } from 'vue'
  import { useStore } from 'vuex'
  import $ from 'jquery'

  export default {
    setup () {
      const store = useStore();

      let match_btn_info = ref("开始匹配");
      let btn_style = ref("btn btn-warning btn-lg");
      let bots = ref([]);
      let select_option = ref("-1");

      const click_match_btn = () => {
        if (match_btn_info.value === "开始匹配") {
          match_btn_info.value = "取消"
          console.log(select_option.value)
          btn_style.value = "btn btn-danger btn-lg"
          store.state.pk.socket.send(JSON.stringify({
            event: "start-matching",
            bot_id: select_option.value
          }))

        } else {
          match_btn_info.value = "开始匹配"
          btn_style.value = "btn btn-warning btn-lg"
          store.state.pk.socket.send(JSON.stringify({
            event: "stop-matching",
          }))
        }
      };

      const refresh_bots = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/user/bot/getlist/",
          type: "get",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (res) {
            bots.value = res;
          },
          error (res) {
            console.log(res);
          },
        });
      };
      refresh_bots();   //从云端动态获取bots

      return {
        match_btn_info,
        click_match_btn,
        btn_style,
        bots,
        select_option,
      }
    }
  }
</script>>
<style scoped>
  div.matchground {
    width: 60vw;
    height: 70vh;
    margin: 40px auto;
    margin-top: 2vh;
    background-color: rgba(50, 50, 50, 0.5);
  }

  div.user-photo {
    text-align: center;

  }

  div.user-status {
    margin-top: 5vh;
    text-align: center;
    font-size: 2vw;
    font-weight: 600;
    color: white;

  }

  div.user-select-option {
    padding-top: 20vh;
  }

  div.user-select-option>select {
    width: 40%;
    margin: 0 auto;
    text-align: center;
  }

  div.user-photo>img {
    border-radius: 50%;
    width: 20vh;
    height: 20vh;
    margin-top: 10vh;
  }

  div.user-name {
    text-align: center;
    font-size: 2vw;
    font-weight: 600;
    color: white;
  }
</style>