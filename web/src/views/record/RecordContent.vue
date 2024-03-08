<template>
  <ContentField>游戏录像</ContentField>
  <PlayGround></PlayGround>
</template>
<script>
  import ContentField from '@/components/ContentField.vue'
  import PlayGround from '@/components/PlayGround.vue'
  //import { useStore } from 'vuex'

  export default {
    components: {
      ContentField,
      PlayGround,
    },
    onUnmounted () {
      // const store = useStore();
      // store.commit("updateStatus", "matching")
      // store.commit("updateLoser", "none")
    },
    setup () {
      //const store = useStore();

    }
  }
</script>>
<style scoped>

</style>