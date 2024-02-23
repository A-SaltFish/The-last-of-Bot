import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '../views/pk/PkIndexView'
import RecordIndexView from '../views/record/RecordIndexView.vue'
import RanklistIndexView from '../views/ranklist/RanklistIndexView.vue'
import UserBotIndexView from '../views/user/bot/UserBotIndexView.vue'
import UserInfoIndexView from '../views/user/info/UserInfoIndexView.vue'
import NotFound from '../views/error/NotFound.vue'

const routes = [
  {
    path: "/",
    name: "home",
    redirect: "/pk/"
  },
  {
    path: "/pk/",
    name: "pk_index",
    component: PkIndexView,
  },
  {
    path: "/record/",
    name: "record_index",
    component: RecordIndexView,
  },
  {
    path: "/ranklist/",
    name: "ranklist_index",
    component: RanklistIndexView,
  },
  {
    path: "/user/bot/",
    name: "user_bot_index",
    component: UserBotIndexView,
  },
  {
    path: "/user/info/",
    name: "user_info_index",
    component: UserInfoIndexView,
  },
  {
    path: "/404/",
    name: "404",
    component: NotFound,
  }, 
  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
