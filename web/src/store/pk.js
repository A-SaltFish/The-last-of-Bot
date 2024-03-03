
//import $ from 'jquery';

export default {
  state: {
    status: "matching",    //matching表示匹配界面，playing表示对战
    socket: null,
    opponent_username: "",
    opponent_photo: "",
    opponent_rating: "",
    gamemap: null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
    gameObject: null,
    loser: "none"
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
    updateGame (state, game) {
      state.gamemap = game.map;
      state.a_id = game.a_id;
      state.a_sx = game.a_sx;
      state.a_sy = game.a_sy;
      state.b_id = game.b_id;
      state.b_sx = game.b_sx;
      state.b_sy = game.b_sy;
    },
    updateGameObject (state, obj) {
      state.gameObject = obj;
    },
    updateLoser (state, loser) {
      state.loser = loser
    }

  },
  //用actions里的函数，我们采用store.dispatch。主要负责处理异步操作，比如这里找服务器拉取信息
  actions: {

  },
  modules: {
  }
}