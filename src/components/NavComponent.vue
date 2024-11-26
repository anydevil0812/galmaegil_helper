<template>
  <nav class="NavComponent">
    <div class="nav-container">
      <!-- 메뉴 리스트 -->
      <ul>
        <li class="home"><router-link to="/">홈</router-link></li>
        <li class="inform"><router-link to="/InformPage">갈맷길 안내</router-link></li>
        <li class="inform"><router-link to="/AttractionListPage">갈맷길 명소</router-link></li>
        <li class="tourcenter"><router-link to="/TouristInfoPage">부산 관광 안내소</router-link></li>
      </ul>
      <!-- 사이트 제목 -->
      <div class="helper-text">갈맷길 여행 도우미</div>
    </div>
  </nav>
</template>

<script>
export default {
  name: "NavComponent",
};
</script>

<style>
.NavComponent {
  background-color: #004d99;
  padding: 1rem;
}

.nav-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
}

/* 메뉴 스타일 */
.NavComponent ul {
  display: flex;
  list-style: none;
  gap: 50px;
  margin: 0;
  padding: 0;
  flex: 1;
  justify-content: center;
  margin-left: 200px;
  flex-wrap: wrap; 
}

/* 메뉴 항목 스타일 */
.home {
  min-width: 30px;
  text-align: center;
}

.inform {
  min-width: 100px;
  text-align: center;
}

.tourcenter {
  min-width: 130px;
  text-align: center;
}

/* 메뉴 링크 스타일 */
.NavComponent ul li a {
  color: white;
  text-decoration: none;
  font-size: 1rem;
}

.NavComponent ul li a:hover {
  text-decoration: underline;
}

/* 도우미 텍스트 스타일 */
.helper-text {
  font-size: 1.2rem;
  font-weight: bold;
  color: white;
  margin-left: auto;
}

@media (max-width: 768px) {
  .nav-container {
    flex-direction: column; 
    align-items: flex-start; 
  }

  .NavComponent ul {
    flex-direction: column; 
    gap: 10px;
    margin-left: 0;
    justify-content: flex-start;
  }

  .helper-text {
    margin-top: 20px; 
    text-align: center; 
  }
}
</style>
