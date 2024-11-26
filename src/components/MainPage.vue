<template>
  <div class="main-container">
    <h1>갈맷길 여행 도우미</h1>
    <p>부산 갈맷길의 다양한 관광 명소와 근처 맛집 및 숙박 정보를 확인하세요!</p>
    <div class="button-group">
      <span><button @click="goToInformPage">갈맷길이란?</button></span>
      <span><button @click="goToAttractions">명소 보기</button></span>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainPage",
  methods: {
    goToInformPage() {
      this.$router.push("/InformPage"); // InformPage로 이동
    },
    goToAttractions() {
      this.$router.push("/AttractionListPage"); // 다른 페이지로 이동
    }
  }
};
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  justify-content: center; /* 수직 중앙 정렬 */
  align-items: center; /* 수평 중앙 정렬 */
  min-height: 100vh; /* 화면 전체 높이 사용 */
  text-align: center; /* 텍스트 가운데 정렬 */
}

h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

p {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

button {
  padding: 10px 20px;
  font-size: 1.2rem;
  cursor: pointer;
  background-color: #004d99;
  color: white;
  border: none;
  border-radius: 5px;
  transition: background-color 0.3s ease;
  margin-left: 15px;
}

button:hover {
  background-color: #003366;
}
</style>
