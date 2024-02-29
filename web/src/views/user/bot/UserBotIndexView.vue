<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top: 4vh">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width: 100%" />
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top: 4vh">
          <div class="card-header">
            <span style="font-size: 130%">我的Bot</span>
            <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal"
              data-bs-target="#add-bot-btn">
              创建Bot
            </button>
            <!--Modal-->
            <div class="modal fade" id="add-bot-btn" tabindex="-1" aria-labelledby="exampleModalLabel"
              aria-hidden="true">
              <div class="modal-dialog modal-lg">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">
                      Bot Create
                    </h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="add-bot-title" class="form-label">
                        Bot名称
                      </label>
                      <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title"
                        placeholder="请输入Bot名称" />
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-description" class="form-label"> Bot简介</label>
                      <textarea v-model="botadd.description" class="form-control" id="add-bot-description"
                        placeholder="请输入Bot简介" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code" class="form-label"> Bot代码</label>
                      <VAceEditor v-model="botadd.content" @init="editorInit" lang="java" theme="github"
                        style="height: 300px" />
                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="error-msg">
                      {{botadd.error_msg}}
                    </div>
                    <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                      取消
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="body">
            <table class="table table-striped table-hover">
              <thead>
                <tr>
                  <th>名称</th>
                  <th>创建时间</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createTime }}</td>
                  <td>
                    <button type="button" class="btn btn-secondary" data-bs-toggle="modal"
                      :data-bs-target="'#update-bot-modal-'+bot.id">
                      修改
                    </button>
                    <button type="button" class="btn btn-danger" style="margin-left: 1vw" @click="remove_bot(bot)">
                      删除
                    </button>
                    <!--Modal-->
                    <div class="modal fade" :id="'update-bot-modal-'+bot.id" tabindex="-1"
                      aria-labelledby="exampleModalLabel" aria-hidden="true">
                      <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5" id="exampleModalLabel">
                              Bot Create
                            </h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <div class="mb-3">
                              <label for="add-bot-title" class="form-label">
                                Bot名称
                              </label>
                              <input v-model="bot.title" type="text" class="form-control" id="add-bot-title"
                                placeholder="请输入Bot名称" />
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-description" class="form-label">
                                Bot简介</label>
                              <textarea v-model="bot.description" class="form-control" id="add-bot-description"
                                placeholder="请输入Bot简介" rows="3"></textarea>
                            </div>
                            <div class="mb-3">
                              <label for="add-bot-code" class="form-label"> Bot代码</label>
                              <v-ace-editor v-model:value="bot.content" @init="editorInit" lang="java" theme="github"
                                style="height: 300px" />
                            </div>
                          </div>
                          <div class="modal-footer">
                            <div class="error-msg">
                              {{botadd.error_msg}}
                            </div>
                            <button type="button" class="btn btn-primary" @click="update_bot(bot)">保存修改</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                              @click="cancel_update_bot()">
                              取消
                            </button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import { useStore } from "vuex";
  import { ref, reactive } from "vue";
  import $ from "jquery";
  import { Modal } from 'bootstrap/dist/js/bootstrap';
  import { VAceEditor } from 'vue3-ace-editor';
  // import ace from 'ace-builds';
  import modeJava from "ace-builds/src-noconflict/mode-java?url";
  import githubTheme from "ace-builds/src-noconflict/theme-github?url";
  import { config } from "ace-builds";

  export default {
    components: {
      VAceEditor
    },
    setup () {
      //ace.config.set("basePath","https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

      config.setModuleUrl("ace/mode/java", modeJava);
      config.setModuleUrl("ace/theme/github", githubTheme);
      const store = useStore();
      let bots = ref([]);
      //console.log("https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
      const botadd = reactive({
        title: "",
        description: "",
        content: "",
        error_msg: "",
      });


      const refresh_bots = () => {
        $.ajax({
          url: "http://127.0.0.1:3000/user/bot/getlist/",
          type: "get",
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (res) {
            bots.value = res;
          },
          error (res) {
            console.log(res);
          },
        });
      };
      refresh_bots();

      const add_bot = () => {
        botadd.error_msg = "";
        $.ajax({
          url: "http://127.0.0.1:3000/user/bot/add/",
          type: "post",
          data: {
            title: botadd.title,
            description: botadd.description,
            content: botadd.content,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (res) {
            if (res.error === "success") {
              refresh_bots();
              botadd.description = "";
              botadd.title = "";
              botadd.content = "";
              botadd.error_msg = "";
              Modal.getInstance("#add-bot-btn").hide();
            } else {
              botadd.error_msg = res.error
            }
          }
        })
      };

      const remove_bot = (bot) => {
        $.ajax({
          url: "http://127.0.0.1:3000/user/bot/remove/",
          type: "post",
          data: {
            bot_id: bot.id,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (res) {
            if (res.error === "success") {
              refresh_bots()
            } else {
              botadd.error_msg = res.error
            }
          }
        })
      };

      const update_bot = (bot) => {
        botadd.error_msg = "";
        $.ajax({
          url: "http://127.0.0.1:3000/user/bot/update/",
          type: "post",
          data: {
            bot_id: bot.id,
            title: bot.title,
            description: bot.description,
            content: bot.content,
          },
          headers: {
            Authorization: "Bearer " + store.state.user.token,
          },
          success (res) {
            if (res.error === "success") {
              refresh_bots();
              Modal.getInstance("#update-bot-modal-" + bot.id).hide();
            } else {
              refresh_bots();
              botadd.error_msg = res.error
            }
          }
        })
      }

      const cancel_update_bot = () => {
        refresh_bots();
      }

      return {
        bots,
        add_bot,
        remove_bot,
        update_bot,
        botadd,
        cancel_update_bot,
      };
    },
  };
</script>>
<style scoped>
  div.error-msg {
    font-size: 1vw;
    color: red;
  }
</style>