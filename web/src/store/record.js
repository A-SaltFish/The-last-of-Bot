
//import $ from 'jquery';

export default {
  state: {
    is_record: false,
    a_steps: "",
    b_steps: "",

    record_loser: "none"
  },
  getters: {
  },
  //用mutations里的函数，我们采用store.commit。同步操作可以放mutation
  mutations: {
    updateIsRecord (state, is_record) {
      state.is_record = is_record;
    },
    updateSteps (state, data) {
      console.log("data", data)
      state.a_steps = data.a_steps;
      state.b_steps = data.b_steps;
    },
    updateRecordLoser (state, loser) {
      state.record_loser = loser;
    }

  },
  //用actions里的函数，我们采用store.dispatch。主要负责处理异步操作，比如这里找服务器拉取信息
  actions: {

  },
  modules: {
  }
}