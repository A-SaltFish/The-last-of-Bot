<template>
  <ContentField>游戏区域</ContentField>
  <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
  <MatchGround v-if="$store.state.pk.status==='matching'||$store.state.pk.status==='match-success'"></MatchGround>
  <ResultBoard v-if="$store.state.pk.loser !='none'" />
</template>
<script>
  import ContentField from '@/components/ContentField.vue'
  import PlayGround from '@/components/PlayGround.vue'
  import MatchGround from '@/components/MatchGround.vue'
  import ResultBoard from '@/components/ResultBoard.vue'
  import { onMounted, onUnmounted } from 'vue'
  import { useStore } from 'vuex'

  export default {
    components: {
      ContentField,
      PlayGround,
      MatchGround,
      ResultBoard,
    },
    setup () {
      const store = useStore();
      const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
      console.log("url" + socketUrl);

      let socket = null;
      onMounted(() => {
        store.commit("updateOpponent", {
          username: "我的对手",
          photo: "https://tse3-mm.cn.bing.net/th/id/OIP-C.E3zje7gEBNi6NaiHz78FDwHaFj?w=4607&h=3455&rs=1&pid=ImgDetMain",
        })
        socket = new WebSocket(socketUrl);

        socket.onopen = () => {
          console.log("connected!");
          store.commit("updateSocket", socket)
        }
        socket.onmessage = msg => {
          const data = JSON.parse(msg.data);
          if (data.event === "start-matching") {
            store.commit("updateOpponent", {
              username: data.opponent_username,
              photo: data.opponent_photo,
              rating: data.opponent_rating,
            })
            store.commit("updateStatus", "match-success")
            setTimeout(() => {
              store.commit("updateStatus", "playing")
            }, 2000)
            store.commit("updateGame", data.game);
          }
          //如果移动，代表都还活着
          else if (data.event === "move") {
            console.log(data)

            const gameObj = store.state.pk.gameObject;
            const [snake0, snake1] = gameObj.snakes;
            snake0.set_direction(data.a_direction);
            snake1.set_direction(data.b_direction);
          }
          else if (data.event === "result") {
            console.log(data)
            const gameObj = store.state.pk.gameObject;
            const [snake0, snake1] = gameObj.snakes;
            if (data.loser === "all" || data.loser === "a") {
              snake0.status = "die";
            }
            if (data.loser === "all" || data.loser === "b") {
              snake1.status = "die";
            }
            store.commit("updateLoser", data.loser);


          }
        }
        socket.onclose = () => {
          console.log("disconnected!");
        }

      });

      onUnmounted(() => {
        socket.close()
      })
    }
  }
</script>>
<style scoped>

</style>