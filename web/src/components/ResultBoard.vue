<template>
  <div class="result-board">
    <div class="result-board-text" v-if="$store.state.pk.loser =='all'">
      平局!
    </div>
    <div class="result-board-text"
      v-else-if="$store.state.pk.loser=='a' && $store.state.pk.a_id===parseInt($store.state.user.id)">
      Lose
    </div>
    <div class="result-board-text"
      v-else-if="$store.state.pk.loser=='b' && $store.state.pk.b_id===parseInt($store.state.user.id)">
      Lose
    </div>
    <div class="result-board-text" v-else>
      Win
    </div>
    <div class="result-board-btn">
      <button @click="restart_btn" type="button" class="btn btn-warning btn-lg">再来一局</button>
    </div>

  </div>
</template>

<script>
  import { useStore } from 'vuex'
  export default {
    setup () {
      const store = useStore();
      const restart_btn = () => {
        console.log("重来")
        store.commit("updateStatus", "matching");
        store.commit("updateLoser", "none")
      };
      return {
        restart_btn,
      };
    }

  }


</script>
<style scoped>
  div.result-board {
    height: 30vh;
    width: 40vw;
    position: absolute;
    top: 30vh;
    left: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
  }

  div.result-board-text {
    padding-top: 5vh;
    text-align: center;
    color: white;
    font-size: 5vw;
    font-style: italic;
    font-weight: 600;

  }

  div.result-board-btn {
    padding-top: 7vh;
    text-align: center;

  }
</style>