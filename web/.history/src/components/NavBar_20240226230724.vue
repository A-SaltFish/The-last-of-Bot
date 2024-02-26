<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <router-link class="navbar-brand" :to="{ name: 'home' }"
        >The Last Bot</router-link
      >
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link
              :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'"
              :to="{ name: 'pk_index' }"
              >对战</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'record_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'record_index' }"
              >对局列表</router-link
            >
          </li>
          <li class="nav-item">
            <router-link
              :class="
                route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'
              "
              :to="{ name: 'ranklist_index' }"
              >排行榜</router-link
            >
          </li>
        </ul>
        <ul class="navbar-nav" v-if="$store.state.user.is_login">
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              role="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              {{ $store.state.user.username }}
            </a>
            <ul class="dropdown-menu">
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_info_index' }"
                  >账户信息</router-link
                >
              </li>
              <li>
                <router-link
                  class="dropdown-item"
                  :to="{ name: 'user_bot_index' }"
                  >我的Bot</router-link
                >
              </li>
              <li>
                <router-link class="dropdown-item" :to="{ name: '404' }"
                  >退出</router-link
                >
              </li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link class="dropdown-item" :to="{ name: 'user_account_login' }"
              >登录</router-link>
          </li>
          <li class="nav-item">
            <router-link class="dropdown-item" :to="{ name: 'user_account_register' }"
              >注册</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>
<script>
import { useRoute } from "vue-router";
import { computed } from "vue";
import {store} from "@/store";
export default {
  setup() {
    const route = useRoute();
    //computed（）里面是要传一个具有返回值的函数，作为计算属性的getter。
    //例如此处route_name=compute()...是让route.name的值绑定为计算属性route_name的值。route.name改变，route_name改变。使用computed具有缓存计算属性的功能，内部计算属性的响应式数据没有改变，该数据将一直存在缓存区中，不会被重新计算，加快运行速度。
    let route_name = computed(() => route.name);
    return { route_name };
  },
};
</script>
<style scoped>
</style>
