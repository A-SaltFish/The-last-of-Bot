
//import $ from 'jquery';

export default {
  state: {
    status: "matching",    //matching表示匹配界面，playing表示对战
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    opponent_rating: "",
    gamemap: null,
  },
  getters: {
  },
  //用mutations里的函数，我们采用store.commit。同步操作可以放mutation
  mutations: {
    updateSocket (state, socket) {
      state.socket = socket;
    },
    updateOpponent (state, opponent) {
      state.opponent_username = opponent.username;
      state.opponent_photo = opponent.photo;
      state.opponent_rating = opponent.rating;
      console.log(state.opponent_photo)
    },
    updateStatus (state, status) {
      state.status = status;
    },
    updateGameMap (state, gamemap) {
      state.gamemap = gamemap;
    }

  },
  //用actions里的函数，我们采用store.dispatch。主要负责处理异步操作，比如这里找服务器拉取信息
  actions: {

  },
  modules: {
  }
}