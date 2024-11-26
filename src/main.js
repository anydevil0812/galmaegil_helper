import { createApp } from "vue"; 
import App from "./App.vue";
import { createRouter, createWebHistory } from "vue-router";
import MainPage from "./components/MainPage.vue";
import AttractionListPage from "./components/AttractionListPage.vue";
import AttractionDetailPage from "./components/AttractionDetailPage.vue";
import InformPage from "./components/InformPage.vue";
import TouristInfoPage from "./components/TouristInfoPage.vue";

// 라우터 설정
const routes = [
  { path: "/", component: MainPage },
  { path: "/AttractionListPage", component: AttractionListPage },
  { path: "/AttractionDetailPage/:uc_seq", name: "AttractionDetailPage", component: AttractionDetailPage },
  { path: "/TouristInfoPage", component: TouristInfoPage },
  { path: "/InformPage", component: InformPage },
];

const router = createRouter({
  history: createWebHistory(), 
  routes,
});

// Vue 애플리케이션 생성 및 라우터 연결
createApp(App).use(router).mount("#app");
