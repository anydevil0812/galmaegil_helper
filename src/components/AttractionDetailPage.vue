<template>
  <div class="sight-page">
    <!-- 명소 정보 -->
    <div v-if="sightData.length > 0" class="sight-info">
      <h1>{{ sightData[0].sight_name }}</h1>
      <div v-if="sightData[0].sight_main_img_t">
        <img :src="getImageUrl(sightData[0].sight_main_img_t)" alt="삭제된 이미지" />
      </div>
      <p><strong>제목:</strong> {{ sightData[0].sight_title }}</p>
      <p><strong>위치:</strong> {{ sightData[0].sight_place }}</p>
      <p><strong>소개:</strong> {{ sightData[0].sight_itemcntnts }}</p>
      <p><strong>코스:</strong> {{ sightData[0].sight_course }}코스</p>
    </div>
    <div v-else>명소 정보를 불러오는 중입니다...</div>

    <!-- 맛집 정보 -->
    <div v-if="restaurantData.length > 0" class="restaurant-info">
      <h2>주변 맛집 정보</h2>
      <div v-for="(restaurant, index) in restaurantData" :key="index" class="restaurant-item">
        <h3>{{ restaurant.restaurant_name }}</h3>
        <div v-if="restaurant.restaurant_main_img_t">
          <img :src="getImageUrl(restaurant.restaurant_main_img_t)" alt="삭제된 이미지" />
        </div>
        <p><strong>주소:</strong> {{ restaurant.restaurant_addr1 }}</p>
        <p><strong>전화번호:</strong> {{ restaurant.restaurant_cntct_tel }}</p>
        <p><strong>영업 시간:</strong> {{ restaurant.restaurant_usage_day }}</p>
        <p><strong>설명:</strong> {{ restaurant.restaurant_itemcntnts }}</p>
        <p><strong>명소와의 거리:</strong> {{ restaurant.restaurant_distance }} km</p>
      </div>

      <!-- 맛집 더 보기 -->
      <button v-if="restaurantHasMore" @click="fetchMoreRestaurants">더 보기</button>
    </div>
    <p v-else-if="sightData.length > 0">맛집 정보를 불러오는 중입니다...</p>

    <!-- 숙박 정보 -->
    <h2>주변 숙박 정보</h2>
    <div v-if="accommodationData.length > 0" class="accommodation-info">
      <div v-for="(accommodation, index) in accommodationData" :key="index" class="accommodation-item">
        <h3>{{ accommodation.accom_name }}</h3>
        <p><strong>주소:</strong> {{ accommodation.accom_addr1 }}</p>
        <p><strong>전화번호:</strong> {{ accommodation.accom_cntct_tel }}</p>
        <p><strong>객실 수:</strong> {{ accommodation.accom_num_room }}</p>
        <p>
          <strong>관련 링크:</strong>
          <span v-if="accommodation.accom_homepage_u && accommodation.accom_homepage_u.trim()">
            <a :href="accommodation.accom_homepage_u" target="_blank" rel="noopener noreferrer">
              {{ accommodation.accom_homepage_u }}
            </a>
          </span>
          <span v-else>없음</span>
        </p>
        <p><strong>명소와의 거리:</strong> {{ accommodation.accom_distance }} km</p>
      </div>

      <!-- 숙박 더 보기 -->
      <button v-if="accommodationHasMore" @click="fetchMoreAccommodations">더 보기</button>
    </div>
    <p v-else-if="accommodationData.length === 0 && sightData.length > 0">
      가까운 거리에 숙박 시설이 존재하지 않습니다.
    </p>
  </div>
</template>

<script>
import { useRoute } from "vue-router";

export default {
  name: "AttractionDetailPage",
  data() {
    return {
      sightData: [], // 명소 데이터
      restaurantData: [], // 맛집 데이터
      accommodationData: [], // 숙박 데이터
      rPage: 1, // 맛집 페이지 번호
      aPage: 1, // 숙박 페이지 번호
      pageSize: 3, // 한 페이지에 표시할 항목 수
      restaurantHasMore: true, // 맛집 데이터 더 가져올 수 있는지 여부
      accommodationHasMore: true, // 숙박 데이터 더 가져올 수 있는지 여부
      uc_seq: null, // uc_seq 값 저장
    };
  },
  methods: {
    // uc_seq 값을 계속 유지하고, 페이지 이동 없이 계속 사용할 수 있도록 함
    async fetchSightAndRestaurantData(uc_seq) {
      this.uc_seq = uc_seq; // uc_seq를 data에 저장하여 계속 사용
      try {
        const response = await fetch(`/api/getAttractionDetail?ucSeq=${this.uc_seq}&aPage=${this.aPage}&rPage=${this.rPage}&pageSize=${this.pageSize}`);
        if (!response.ok) throw new Error("API 호출 실패");
        const data = await response.json();

        // 명소 데이터 처리
        this.sightData = [
          {
            sight_name: data.name,
            sight_place: data.place,
            sight_title: data.title,
            sight_itemcntnts: data.itemcntnts,
            sight_course: data.course,
            sight_lat: data.lat,
            sight_lng: data.lng,
            sight_main_img_t: data.main_img_t,
          },
        ];

        // 초기 맛집 데이터 요청
        await this.fetchMoreRestaurants(uc_seq);

        // 초기 숙박 데이터 요청
        await this.fetchMoreAccommodations(uc_seq);
      } catch (error) {
        console.error("API 호출 중 오류 발생:", error);
      }
    },
    async fetchMoreRestaurants() {
      try {
        const response = await fetch(`/api/getAttractionDetail?ucSeq=${this.uc_seq}&aPage=${this.aPage}&rPage=${this.rPage}&pageSize=${this.pageSize}`);
        if (!response.ok) throw new Error("맛집 데이터 호출 실패");
        const { restaurants } = await response.json();

        // 맛집 데이터 갱신
        this.restaurantData = [...this.restaurantData, ...restaurants.map(item => ({
          restaurant_name: item.name,
          restaurant_addr1: item.addr1,
          restaurant_cntct_tel: item.cntct_tel,
          restaurant_usage_day: item.usage_day_,
          restaurant_itemcntnts: item.itemcntnts,
          restaurant_distance: item.distance.toFixed(2),
          restaurant_main_img_t: item.main_img_t,
        }))];
        
        this.restaurantHasMore = (restaurants.length == this.pageSize);
        this.rPage++;
      } catch (error) {
        console.error("맛집 데이터 불러오기 중 오류:", error);
      }
    },
    async fetchMoreAccommodations() {
      try {
        const response = await fetch(`/api/getAttractionDetail?ucSeq=${this.uc_seq}&aPage=${this.aPage}&rPage=${this.rPage}&pageSize=${this.pageSize}`);
        if (!response.ok) throw new Error("숙박 데이터 호출 실패");
        const { accommodations } = await response.json();

        // 숙박 데이터 갱신
        this.accommodationData = [...this.accommodationData, ...accommodations.map(item => ({
          accom_name: item.name,
          accom_addr1: item.addr1,
          accom_cntct_tel: item.cntct_tel,
          accom_num_room: item.num_room,
          accom_homepage_u: item.homepage_u,
          accom_distance: item.distance.toFixed(2),
        }))];

        this.accommodationHasMore = (accommodations.length == this.pageSize);
        this.aPage++;
      } catch (error) {
        console.error("숙박 데이터 불러오기 중 오류:", error);
      }
    },
    getImageUrl(imagePath) {
      return `https://www.visitbusan.net/${imagePath}`;
    },
  },
  mounted() {
    const route = useRoute();
    const uc_seq = route.params.uc_seq;
    if (uc_seq) {
      this.fetchSightAndRestaurantData(uc_seq);
    } else {
      console.error("uc_seq 값이 제공되지 않았습니다.");
    }
  },
};
</script>

<style>
.sight-page {
  margin: 2rem;
}

.sight-info,
.restaurant-info,
.accommodation-info {
  margin-bottom: 2rem;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.restaurant-item,
.accommodation-item {
  margin-bottom: 1.5rem;
  padding: 1rem;
  border: 1px solid #eee;
  border-radius: 5px;
  background-color: #fff;
}

.accommodation-item a {
  color: #003366;
  text-decoration: none;
}

.accommodation-item a:visited {
  color: #003366;
  text-decoration: none;
}

.accommodation-item a:hover {
  text-decoration: underline;
}

h1, h2, h3 {
  color: #333;
}

.pagination {
  margin-top: 1rem;
  text-align: center;
}

.page-buttons {
  margin-top: 1rem;
}

.page-buttons button {
  margin: 0.2rem;
  padding: 0.5rem;
  border: 1px solid #ddd;
  background-color: #f0f0f0;
  cursor: pointer;
}

.page-buttons button.active {
  background-color: #007bff;
  color: #fff;
}

/* 더 보기 버튼 */
button {
  padding: 0.6rem 1.2rem; 
  border: 2px solid #003366; 
  background-color: #003366; 
  color: white; 
  font-size: 1rem; 
  font-weight: bold; 
  border-radius: 5px;
  cursor: pointer; 
  transition: background-color 0.3s ease, border-color 0.3s ease; 
}

button:hover {
  background-color: #005cbf; 
  border-color: #005cbf; 
}

button:focus {
  outline: none; 
  box-shadow: 0 0 0 3px rgba(0, 92, 191, 0.5); 
}

@media (max-width: 768px) {
  img {
    width: 50%; 
  }

  .sight-page {
    margin: 1rem; 
  }

  .restaurant-info,
  .accommodation-info {
    padding: 0.8rem; 
  }

  .restaurant-item,
  .accommodation-item {
    padding: 0.8rem; 
  }
}

@media (max-width: 480px) {
  img {
    width: 50%; 
  }

  .sight-page {
    margin: 0.5rem; 
  }

  .restaurant-info,
  .accommodation-info {
    padding: 0.6rem; 
  }

  .restaurant-item,
  .accommodation-item {
    padding: 0.6rem; 
  }
} 

</style>
