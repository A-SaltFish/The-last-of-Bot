import $ from 'jquery';

export default{
    state: {
        id:"",
        username:"",
        photo:"",
        token:"",
        is_login:false,
        pulling_info:true,  //是否正在拉取信息
    },
    getters: {
    },
    //用mutations里的函数，我们采用store.commit。同步操作可以放mutation
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
        },
        updatePullingInfo(state,info){
          state.pulling_info=info;
        }
    },
        //用actions里的函数，我们采用store.dispatch。主要负责处理异步操作，比如这里找服务器拉取信息
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
                    localStorage.setItem("jwt_token",res.token);
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
        },
        logout(context){
          localStorage.removeItem("jwt_token");
          context.commit("logout");
        }
    },
    modules: {
    }
}