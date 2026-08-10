<template>

  <div class="write-container">

    <div class="write-header">

      <h1>게시글 작성</h1>

      <p>새로운 게시글을 작성해주세요.</p>

    </div>


    <div class="write-card">

      <div class="form-group">

        <label>제목</label>

        <input
          v-model="title"
          type="text"
          placeholder="제목을 입력하세요"
        />

      </div>


      <div class="form-group">

        <label>내용</label>

        <textarea
          v-model="content"
          placeholder="내용을 입력하세요"
        ></textarea>

      </div>


      <div class="button-area">

        <button
          class="cancel-button"
          @click="goBack"
        >
          취소
        </button>


        <button
          class="save-button"
          @click="save"
        >
          등록
        </button>

      </div>

    </div>

  </div>

</template>

<script setup>

import { ref } from 'vue'
import { useBoardStore } from '@/stores/board'
import { useRouter } from 'vue-router'


const title = ref("")
const content = ref("")
const boardStore = useBoardStore()
const router = useRouter()

const goBack = () => {
  router.push('/board')
}

const save = async () => {

  if (!title.value.trim()) {
    alert('제목을 입력해주세요.')
    return
  }

  if (!content.value.trim()) {
    alert('내용을 입력해주세요.')
    return
  }

  const post = {
    title: title.value,
    content: content.value
  }
  
  await boardStore.addPost(post)

  router.push('/board')

}

</script>

<style scoped>

.write-container {
  width: 94%;
  max-width: 1400px;
  margin: 50px auto;
}


.write-header {
  margin-bottom: 30px;
}


.write-header h1 {
  margin: 0;
  font-size: 32px;
}


.write-header p {
  margin: 8px 0 0;
  color: #999;
  font-size: 14px;
}


.write-card {
  width: 100%;
  box-sizing: border-box;

  padding: 50px 60px;

  border: 1px solid #eee;
  border-radius: 14px;

  background: white;

  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.05);
}


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


.button-area {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 35px;
}


.cancel-button,
.save-button {
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}


.cancel-button {
  border: 1px solid #ddd;
  background: white;
}


.save-button {
  border: none;
  background: #222;
  color: white;
}

</style>