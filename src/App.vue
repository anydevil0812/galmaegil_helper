<template>
  <div id="app">
    <!-- 공통 네비게이션 -->
    <NavBarComponent />
    <!-- 라우터에 따라 렌더링되는 페이지 -->
    <router-view class="content" />
    <!-- 공통 푸터 -->
    <footer class="footer">
      <p>부산 갈맷길 관광 정보 제공 웹사이트 © 2024</p>
    </footer>
  </div>
</template>

<script>
import NavBarComponent from "./components/NavComponent.vue";

export default {
  name: "App",
  components: {
    NavBarComponent,
  },
};
</script>

<style>
/* 전체 애플리케이션에 적용되는 스타일 */
#app {
  font-family: "Arial", sans-serif;
  margin: 0;
  padding: 0;
  box-sizing: border-box;

  /* 화면 전체 사용 */
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* 화면 전체 높이를 최소 높이로 설정 */
}

/* 라우터 뷰가 화면을 채우도록 설정 */
.content {
  flex: 1; /* 남은 공간을 채우도록 설정 */
}

/* 푸터 스타일 */
.footer {
  text-align: center;
  background-color: #004d99;
  color: white;
  padding: 1rem;
}

</style>