<template>

  <div v-if="post" class="detail-container">

    <!-- 게시글 카드 -->

    <div class="detail-card">

      <!-- 게시글 번호 -->

      <div class="post-number">
        번호 {{ post.id }}
      </div>


      <!-- 제목 -->

      <h1 class="post-title">
        {{ post.title }}
      </h1>


      <!-- 구분선 -->

      <div class="divider"></div>


      <!-- 내용 -->

      <div class="post-content">
        {{ post.content }}
      </div>


      <!-- 버튼 -->

      <div class="button-area">

        <button
          class="list-button"
          @click="goList"
        >
          목록
        </button>


        <div>

          <button
            class="edit-button"
            @click="goEdit"
          >
            수정
          </button>


          <button
            class="delete-button"
            @click="remove"
          >
            삭제
          </button>

        </div>

      </div>

    </div>

  </div>

</template>

<script setup>

import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router' 
import { useBoardStore } from '@/stores/board'
import axios from 'axios'


const route = useRoute()
const router = useRouter()

const boardStore = useBoardStore()

const post = ref(null)

async function fetchPost(){

    const response = await axios.get(
        `http://localhost:9090/api/posts/${route.params.id}`
    )


    post.value = response.data

}


/* 목록 */ 
function goList() 
{ 
	router.push('/board') 
} 

/* 수정 */ 
function goEdit() 
{ 
	router.push(`/board/edit/${route.params.id}`) 
} 

/* 삭제 */ 
async function remove() 
{ 
	if (!confirm('게시글을 삭제하시겠습니까?')) 
	{ 
		return 
	} 
	
	await boardStore.deletePost(route.params.id) 
	
	router.push('/board') 

}

onMounted(() => {

    fetchPost()

})

</script>

<style scoped>

.detail-container {
  width: 94%;
  max-width: 1200px;
  margin: 50px auto;
}


/* 게시글 카드 */

.detail-card {
  width: 100%;
  box-sizing: border-box;

  padding: 45px 50px;

  background: white;

  border: 1px solid #eee;
  border-radius: 14px;

  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);
}


/* 번호 */

.post-number {
  margin-bottom: 12px;

  color: #999;

  font-size: 13px;
}


/* 제목 */

.post-title {
  margin: 0;

  font-size: 28px;
  font-weight: 600;

  line-height: 1.4;

  word-break: break-word;
}


/* 구분선 */

.divider {
  height: 1px;

  margin: 25px 0;

  background: #eee;
}


/* 내용 */

.post-content {
  min-height: 350px;

  color: #333;

  font-size: 15px;
  line-height: 1.8;

  white-space: pre-wrap;
  word-break: break-word;
}


/* 버튼 영역 */

.button-area {
  display: flex;

  justify-content: space-between;
  align-items: center;

  margin-top: 40px;
  padding-top: 20px;

  border-top: 1px solid #eee;
}


.button-area button {
  padding: 10px 18px;

  border-radius: 8px;

  font-size: 14px;

  cursor: pointer;
}


/* 목록 */

.list-button {
  border: 1px solid #ddd;

  background: white;
}


/* 수정 */

.edit-button {
  margin-right: 8px;

  border: 1px solid #ddd;

  background: white;
}


/* 삭제 */

.delete-button {
  border: 1px solid #f0cccc;

  background: white;

  color: #d44;
}


.list-button:hover,
.edit-button:hover {
  background: #f7f7f7;
}


.delete-button:hover {
  background: #fff5f5;
}


/* 모바일 */

@media (max-width: 600px) {

  .detail-container {
    width: 94%;
    margin: 30px auto;
  }


  .detail-card {
    padding: 30px 20px;
  }


  .post-title {
    font-size: 22px;
  }


  .post-content {
    min-height: 300px;
  }

}

</style>