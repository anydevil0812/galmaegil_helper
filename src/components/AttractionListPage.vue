<template>
  <div class="attractions-page">
    <h1>부산 갈맷길 명소 85선</h1>

    <ul>
      <li class="list" v-for="attraction in attractions" :key="attraction.uc_seq">
        <router-link :to="{ name: 'AttractionDetailPage', params: { uc_seq: attraction.uc_seq } }">
          <strong>{{ attraction.name }}</strong> - {{ attraction.title }}
        </router-link>
      </li>
    </ul>

    <p v-if="loading">데이터를 불러오는 중입니다...</p>
    <p v-if="!loading && attractions.length === 0">표시할 명소가 없습니다.</p>

    <div class="bottom-controls">
      <!-- 페이지 크기 선택 -->
      <div class="page-size">
          <select v-model="pageSize" @change="handlePageSizeChange">
            <option value="10">10개</option>
            <option value="20">20개</option>
            <option value="30">30개</option>
          </select>
      </div>
      <!-- 더 보기 버튼 -->
      <div class="load-more" v-if="hasNextPage">
        <button @click="loadMore">더 보기</button>
      </div>
    </div>

  </div>
  
</template>


<script>
export default {
  name: "AttractionListPage",
  data() {
    return {
      attractions: [], // 현재까지 로드된 명소 리스트
      loading: false, // 데이터 로딩 상태
      currentPage: 1, // 현재 페이지 번호
      pageSize: 10, // 한 페이지에 표시할 데이터 수
      hasNextPage: true, // 다음 페이지가 존재하는지 여부
      isReset: false, // 데이터 출력 초기화 여부
    };
  },
  methods: {
    // 명소 데이터 가져오기
    async fetchAttractions(isReset) {
      try {
        if (isReset) {
          this.loading = true;
          this.attractions = []; // 데이터 초기화
          this.currentPage = 1;
          this.hasNextPage = true;
        }

        const response = await fetch(`/api/getAttractionListByPage?pageSize=${this.pageSize}&currentPage=${this.currentPage}`);
        const data = await response.json();
        
        if (data.response && data.response.body.items.item.length > 0) {
          // 기존 데이터에 새 데이터 추가
          this.attractions.push(
            ...data.response.body.items.item.map((sight) => ({
              uc_seq: sight.uc_seq,
              name: sight.name,
              title: sight.title,
              description: sight.itemcntnts,
              lat: sight.lat,
              lng: sight.lng,
            }))
          );

          // 페이지 크기와 데이터 개수를 비교하여 더 볼 데이터가 있는지 확인
          this.hasNextPage = (data.response.body.items.item.length == this.pageSize);
          
        } else {
          // 더 이상 데이터가 없는 경우
          this.hasNextPage = false;
          if (!isReset) {
            alert("모든 데이터를 조회하였습니다.");
          }
        }
      } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
      } finally {
        this.loading = false;
      }
    },
    // 더 보기 버튼 클릭 시
    loadMore() {
      if (this.hasNextPage) {
        this.currentPage++;
        this.fetchAttractions();
      }
    },
    // selectbox 변경 시
    handlePageSizeChange() {
      this.fetchAttractions(true); // 데이터를 초기화하고 새로 로드
    },
  },
  mounted() {
    // 초기 데이터 로드
    this.fetchAttractions();
  },
};
</script>

<style>
.attractions-page {
  margin: 2rem;
}

ul {
  list-style: none;
  padding: 0;
}

.list {
  margin: 0.5rem 0;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.list a{
  color: #003366;
  text-decoration: none;
}

.list a:visited{
  color: #003366;
}

.list a:hover{
  text-decoration: underline;
}

.pagination {
  margin-top: 1rem;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}

.pagination button {
  margin: 0.3rem;
  padding: 0.5rem 1rem;
  border: 1px solid #007bff;
  background-color: white;
  color: #007bff;
  cursor: pointer;
  border-radius: 5px;
  font-size: 0.9rem;
}

.pagination button.active {
  background-color: #007bff;
  color: white;
  font-weight: bold;
}

.pagination button:hover:not(.active) {
  background-color: #e6f0ff;
}

.page-size {
  margin-bottom: 1rem;
}

/* 페이지 크기 선택과 더 보기 버튼을 같은 줄에 배치 */
.bottom-controls {
  align-items: center;
  margin-bottom: 1rem;
}

select {
  padding: 8px 10px; 
  border: 2px solid #003366; 
  background-color: white; 
  color: #003366; 
  font-size: 1rem; 
  font-weight: bold; 
  border-radius: 5px; 
  cursor: pointer; 
  transition: border-color 0.3s ease, box-shadow 0.3s ease; 
  width: 10%; 
}


select:hover {
  border-color: #005cbf; 
}


select:focus {
  outline: none;
  border-color: #005cbf; 
  box-shadow: 0 0 0 3px rgba(0, 92, 191, 0.5); 
}



</style>
