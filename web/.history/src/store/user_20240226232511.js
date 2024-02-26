import $ from 'jquery';

export default{
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
    },
    getters: {
    },
    mutations: {
        updateUser(state,user){
            state.id=user.id;
            state.username=user.username;
            state.photo=user.photo;
            state.is_login=user.is_login
        },
        updateToken(state,token){
            state.token=token;
        },
        logout(state){
          state.id="";
          state.username="";
          state.photo="";
          state.token="";
          state.is_login=false
        }
    },
    actions: {
        login(context,data){
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(res) {
                  if(res.error==="success"){
                    context.commit("updateToken",res.token);
                    data.success(res)
                  }else{
                    data.error(res)
                  }

                },
                error(res) {
                    data.error(res)
                    console.log(res);
                },
              });
        },
        getinfo(context,data){
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/info/",
                type: "get",
                headers: {
                  Authorization:
                    "Bearer " +
                    context.state.token,
                },
                success(res) {
                    if(res.error==="success"){
                        context.commit("updateUser",{
                            ...res,
                            is_login:true,
                          });
                          data.success(res)
                    }else{
                        data.error(res)
                    }

                },
                error(res) {
                  data.error(res);
                },
              });
        }
    },
    modules: {
    }
}