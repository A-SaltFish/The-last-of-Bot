<template>
<ContentField>我的bot</ContentField>
</template>
<script>
import ContentField from '@/components/ContentField.vue'
import {useStore} from 'vuex'
import $ from 'jquery'

export default {
    components:{
        ContentField
    },
    setup(){
        const store=useStore();
        console.log(store.state.user.token)
        $.ajax({
            url:"http://127.0.0.1:3000/user/bot/getlist/",
            type:"get",
            headers:{
                Authorization:"Bearer "+store.state.user.token,
            },
            success(res){
                console.log(res);
            },
            error(res){
                console.log(res)
            }
        })
    }
}
</script>>
<style scoped>

</style>
