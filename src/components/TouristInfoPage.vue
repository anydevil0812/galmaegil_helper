<template>
  <div class="tourist-info-page">
    <h1>부산 관광 안내소 정보 조회</h1>
    
    <!-- 각 구별로 버튼을 생성 -->
    <div class="buttons-container">
      <button v-for="(district, index) in activeDistricts" :key="index" @click="fetchTouristInfo(district.pageNo)">
        {{ district.name }}
      </button>
    </div>

    <!-- 반환된 데이터 출력 -->
    <div v-if="touristInfo.length > 0">
      <table>
        <thead>
          <tr>
            <th>한국어</th>
            <th>English</th>
            <th>日本語</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(info, index) in touristInfo" :key="index">
            <td>
              <strong>{{ info.NM }}</strong><br />
              {{ info.ADDR1 }}<br />
              전화: {{ info.INQRY_TEL }}<br />
              운영시간: {{ info.OP_TIME }}<br />
              <br />
              <span v-html="formatWithConditionalBreaks(info.INFOFC_INTRCN)"></span>
            </td>
            <td>
              <strong>{{ info.NM_EN }}</strong><br />
              {{ info.ADDR1_EN }}<br />
              Phone: {{ info.INQRY_TEL }}<br />
              Hours: {{ info.OP_TIME }}<br />
              <br />
              <span v-html="formatWithConditionalBreaks(info.INFOFC_INTRCN_EN)"></span>
            </td>
            <td>
              <strong>{{ info.NM_JP }}</strong><br />
              {{ info.ADDR1_JP }}<br />
              電話: {{ info.INQRY_TEL }}<br />
              営業時間: {{ info.OP_TIME }}<br />
              <br />
              <span v-html="formatWithConditionalBreaksJp(info.INFOFC_INTRCN_JP)"></span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <p v-else>지역구를 선택해주세요.</p>
  </div>
</template>

<script>
export default {
  name: "TouristInfoPage",
  data() {
    return {
      activeDistricts: [
        { name: "강서구", pageNo: 10 },
        { name: "금정구", pageNo: 11 },
        { name: "남구", pageNo: 7 },
        { name: "동구", pageNo: 17 },
        { name: "동래구", pageNo: 2 },
        { name: "사하구", pageNo: 6 },
        { name: "서구", pageNo: 3 },
        { name: "수영구", pageNo: 25 },
        { name: "영도구", pageNo: 5 },
        { name: "중구", pageNo: 4 },
        { name: "해운대구", pageNo: 16 },
      ],
      touristInfo: [], // API에서 받은 관광 정보
    };
  },
  methods: {
    async fetchTouristInfo(pageNo) {
      try {
        const response = await fetch(`/api/getTourCenterInfo?pageNo=${pageNo}`);
        const data = await response.json();
        this.touristInfo = data.response || [];
      } catch (error) {
        console.error("API 요청 실패:", error);
        this.touristInfo = [];
      }
    },
    formatWithConditionalBreaks(text) {
      if (!text) return "";

      return text.replace(/\.(?=\s|$)/g, ".<br><br>")
                 .replace(/(\d)\.(?=\s)/g, "$1."); 
    },
    formatWithConditionalBreaksJp(text) {
      if (!text) return "";
        return text.replace(/。(?=\s|$)/g, ".<br><br>") 
                   .replace(/。(?!\d)/g, ".<br>"); 
    },

  },
};
</script>

<style scoped>
.tourist-info-page {
  margin: 2rem;
  text-align: center;
}

.buttons-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 2rem;
}

button {
  margin: 0.5rem;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  background-color: #004d99;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #003366;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

th, td {
  border: 1px solid #ddd;
  padding: 1rem;
  text-align: center;
}

th {
  background-color: #f4f4f4;
  font-weight: bold;
}

td {
  vertical-align: top;
  text-align: left;
}

th, td {
  width: 33.33%;
}
</style>
