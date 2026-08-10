<template>

  <div class="edit-container">

    <div class="edit-header">

      <h1>게시글 수정</h1>

      <p>게시글 내용을 수정해주세요.</p>

    </div>


    <div class="edit-card">

      <!-- 제목 -->

      <div class="form-group">

        <label>제목</label>

        <input
          v-model="title"
          type="text"
          placeholder="제목을 입력하세요"
        />

      </div>


      <!-- 내용 -->

      <div class="form-group">

        <label>내용</label>

        <textarea
          v-model="content"
          placeholder="내용을 입력하세요"
        ></textarea>

      </div>


      <!-- 버튼 -->

      <div class="button-area">

        <button
          class="cancel-button"
          @click="goBack"
        >
          취소
        </button>


        <button
          class="update-button"
          @click="update"
        >
          수정 완료
        </button>

      </div>

    </div>

  </div>

</template>


<script setup>

import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBoardStore } from '@/stores/board'


const route = useRoute()
const router = useRouter()


const id = route.params.id


const boardStore = useBoardStore()


const post = boardStore.posts.find(
  p => p.id == route.params.id
)


const title = ref(post.title)
const content = ref(post.content)


async function update() {

  if (title.value.trim() === '') {
    alert('제목은 필수 항목입니다.')
    return
  }


  if (content.value.trim() === '') {
    alert('내용은 필수 항목입니다.')
    return
  }


  await boardStore.updatePost(
    id,
    {
      title: title.value,
      content: content.value
    }
  )


  router.push('/board')

}


function goBack() {

  router.push('/board')

}

</script>


<style scoped>

.edit-container {
  width: 94%;
  max-width: 1400px;
  margin: 50px auto;
}


/* =========================
   헤더
========================= */

.edit-header {
  margin-bottom: 30px;
}


.edit-header h1 {
  margin: 0;
  font-size: 32px;
}


.edit-header p {
  margin: 8px 0 0;
  color: #999;
  font-size: 14px;
}


/* =========================
   수정 카드
========================= */

.edit-card {
  width: 100%;
  box-sizing: border-box;

  padding: 50px 60px;

  border: 1px solid #eee;
  border-radius: 14px;

  background: white;

  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);
}


/* =========================
   입력
========================= */

.form-group {
  width: 100%;
  margin-bottom: 30px;
}


.form-group label {
  display: block;
  margin-bottom: 10px;

  font-size: 15px;
  font-weight: 600;
}


.form-group input {
  display: block;

  width: 100%;
  height: 56px;

  box-sizing: border-box;

  padding: 0 18px;

  border: 1px solid #ddd;
  border-radius: 8px;

  font-size: 16px;

  outline: none;
}


.form-group textarea {
  display: block;

  width: 100%;
  min-height: 500px;

  box-sizing: border-box;

  padding: 18px;

  border: 1px solid #ddd;
  border-radius: 8px;

  font-size: 15px;
  line-height: 1.7;

  resize: vertical;

  outline: none;
}


.form-group input:focus,
.form-group textarea:focus {
  border-color: #888;
}


/* =========================
   버튼
========================= */

.button-area {
  display: flex;
  justify-content: flex-end;
  gap: 10px;

  margin-top: 35px;
}


.cancel-button,
.update-button {
  padding: 12px 24px;

  border-radius: 8px;

  font-size: 14px;

  cursor: pointer;
}


.cancel-button {
  border: 1px solid #ddd;
  background: white;
}


.update-button {
  border: none;
  background: #222;
  color: white;
}


.update-button:hover {
  background: #444;
}


/* =========================
   모바일
========================= */

@media (max-width: 600px) {

  .edit-container {
    width: 94%;
    margin: 30px auto;
  }


  .edit-card {
    padding: 30px 20px;
  }


  .edit-header h1 {
    font-size: 26px;
  }


  .form-group textarea {
    min-height: 350px;
  }

}

</style>