<template>
    <div class="playground">
        <GameMap/>
    </div>
</template>
    
<script>
import GameMap from './GameMap.vue';
export default {
    components:{
        GameMap,
    }
}
</script>>
<style scoped>
div.playground{
    width: 50vw;
    height: 60vh;
    margin:auto;
    margin-top:2vh;
}
</style>