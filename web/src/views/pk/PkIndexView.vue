<template>
  <ContentField>游戏区域</ContentField>
  <PlayGround v-if="$store.state.pk.status==='playing'"></PlayGround>
  <MatchGround v-if="$store.state.pk.status==='matching'||$store.state.pk.status==='match-success'"></MatchGround>
</template>
<script>
  import ContentField from '@/components/ContentField.vue'
  import PlayGround from '@/components/PlayGround.vue'
  import MatchGround from '@/components/MatchGround.vue'
  import { onMounted, onUnmounted } from 'vue'
  import { useStore } from 'vuex'

  export default {
    components: {
      ContentField,
      PlayGround,
      MatchGround,
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
            }, 3000)
            store.commit("updateGameMap", data.gamemap);
          }
          console.log(data);
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