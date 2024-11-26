<template>
    <div class="galmaetgil-info">
      <h1>갈맷길 안내</h1>
  
      <!-- 갈맷길 정의 -->
      <section v-if="definition">
        <h2>정의</h2>
        <p>{{ definition }}</p>
      </section>

      <!-- 지도 이미지 -->
      <div class="image-gallery">
        <img src="/map.png" alt="갈맷길 지도" />
      </div>

      <!-- 갈맷길 코스 종류 -->
      <section v-if="courses.length > 0">
        <h2>코스 종류</h2>
        <ul>
          <li class="list" v-for="(course, index) in courses" :key="index">
            <strong>{{ course.name }}</strong>: {{ course.description }}<br />
            <span><strong>구간:</strong> {{ course.range }}</span><br />
            <span><strong>난이도:</strong> {{ course.difficulty }}</span>
          </li>
        </ul>
      </section>
    </div>
  </template>
  
  <script>
  export default {
    name: "InformPage",
    data() {
      return {
        definition: "",
        courses: [],
      };
    },
    mounted() {
      this.fetchGalmaegilCourseData();
    },
    methods: {
      async fetchGalmaegilCourseData() {
        try {
          const response = await fetch("/api/getGalmaegilCourse");
          if (!response.ok) {
            throw new Error("API 호출 실패");
          }
          const data = await response.json();
        
          this.definition = "갈맷길은 부산의 자연을 경험하며 걷는 도보 여행길로, 아름다운 해변과 산책로를 포함한 다양한 코스로 구성되어 있습니다."; // 정의는 고정 값으로 설정
          this.courses = data.response.map(item => ({
            name: item.gugan_nm, 
            description: item.gm_course, 
            range: item.gm_range, 
            difficulty: item.gm_degree, 
          }));
        } catch (error) {
          console.error("API 호출 중 오류 발생:", error);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .galmaetgil-info {
    padding: 20px;
    font-family: Arial, sans-serif;
  }
  
  h1 {
    text-align: center;
    font-size: 2em;
    margin-bottom: 20px;
  }
  
  h2 {
    margin-top: 20px;
    font-size: 1.5em;
  }
  
  ul {
    list-style-type: none;
    padding-left: 0;
  }
  
  li {
    margin-bottom: 10px;
  }
  
  .image-gallery {
    display: flex;
    justify-content: center;
  }
  
  .image-gallery img {
    width: 700px;
    height: 600px;
  }
  </style>
  