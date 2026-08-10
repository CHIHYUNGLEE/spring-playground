<template>

<div class="board-container">

  <!-- 게시판 상단 -->
  <div class="board-header">

    <div>
      <h1>게시판</h1>
      <p>Spring Playground Board</p>
    </div>

    <button
      class="write-button"
      @click="goWrite"
    >
      + 등록
    </button>

  </div>


  <!-- 검색 -->
  <div class="search-area">

    <input
      v-model="keyword"
      placeholder="검색어를 입력하세요"
    />

    <button class="search-button">
      검색
    </button>

  </div>


  <!-- 게시글 테이블 -->
  <table class="board-table">

    <thead>

      <tr>
        <th class="number-column">번호</th>
        <th>제목</th>
        <th class="manage-column">관리</th>
      </tr>

    </thead>


    <tbody>

      <tr
        v-for="post in filteredPosts"
        :key="post.id"
      >

        <td class="number-column">
          {{ post.id }}
        </td>


        <td
          class="title-column"
          @click="goDetail(post.id)"
        >
          {{ post.title }}
        </td>


        <td class="manage-column">

        <button
        class="edit-button"
        @click.stop="goEdit(post.id)"
        >
        수정
        </button>

        <button
        class="delete-button"
        @click.stop="remove(post.id)"
        >
        삭제
        </button>

        </td>

      </tr>


      <!-- 게시글이 없을 때 -->

      <tr v-if="filteredPosts.length === 0">

        <td
          colspan="3"
          class="empty-row"
        >
          게시글이 없습니다.
        </td>

      </tr>

    </tbody>

  </table>

</div>

</template>


<script setup>

import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useBoardStore } from '@/stores/board'
import { onMounted } from 'vue'

const keyword = ref("")
const router = useRouter()
const boardStore = useBoardStore()

onMounted(() => {

  boardStore.fetchPosts()

})

const filteredPosts = computed(() => {

    const search = keyword.value.toLowerCase()

    return boardStore.posts.filter(post =>
        post.title.toLowerCase().includes(search)
    )

})


function goDetail(id){

    router.push(`/board/detail/${id}`)

}

function goWrite(){

    router.push('/board/write')

}

function goEdit(id){

    router.push(`/board/edit/${id}`)

}

async function remove(id){

    if(confirm("삭제하시겠습니까?")){

        await boardStore.deletePost(id)

    }

}

</script>

<style scoped>

.board-container {
  width: 90%;
  max-width: 1000px;
  margin: 60px auto;
}


/* =========================
   게시판 헤더
========================= */

.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}


.board-header h1 {
  margin: 0;
  font-size: 32px;
}


.board-header p {
  margin: 8px 0 0;
  color: #999;
  font-size: 14px;
}


/* =========================
   등록 버튼
========================= */

.write-button {
  border: none;
  border-radius: 8px;
  padding: 11px 18px;
  background: #222;
  color: white;
  font-size: 14px;
  cursor: pointer;
}


.write-button:hover {
  background: #444;
}


/* =========================
   검색
========================= */

.search-area {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-bottom: 20px;
}


.search-area input {
  width: 280px;
  padding: 11px 14px;
  border: 1px solid #ddd;
  border-radius: 7px;
  outline: none;
  font-size: 14px;
}


.search-area input:focus {
  border-color: #888;
}


.search-button {
  border: none;
  border-radius: 7px;
  padding: 0 18px;
  background: #555;
  color: white;
  cursor: pointer;
}


.search-button:hover {
  background: #333;
}


/* =========================
   게시판
========================= */

.board-table {
  width: 100%;
  border-collapse: collapse;
  border-top: 2px solid #222;
}


.board-table th {
  padding: 15px 12px;
  background: #fafafa;
  border-bottom: 1px solid #ddd;
  font-size: 14px;
  font-weight: 600;
}


.board-table td {
  padding: 16px 12px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}


/* =========================
   번호
========================= */

.number-column {
  width: 90px;
  text-align: center;
  color: #888;
}


/* =========================
   제목
========================= */

.title-column {
  cursor: pointer;
  font-weight: 500;
}


.title-column:hover {
  text-decoration: underline;
}


/* =========================
   관리
========================= */

.manage-column {
  width: 150px;
  text-align: center;
}


.edit-button,
.delete-button {
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 6px 10px;
  margin: 0 2px;
  background: white;
  font-size: 12px;
  cursor: pointer;
}


.edit-button:hover {
  background: #f5f5f5;
}


.delete-button {
  color: #d44;
}


.delete-button:hover {
  background: #fff5f5;
}


/* =========================
   게시글 없음
========================= */

.empty-row {
  padding: 60px !important;
  text-align: center;
  color: #aaa;
}


/* =========================
   모바일
========================= */

@media (max-width: 600px) {

  .board-container {
    width: 94%;
    margin: 30px auto;
  }


  .board-header h1 {
    font-size: 26px;
  }


  .board-header p {
    font-size: 12px;
  }


  .search-area input {
    width: 100%;
  }


  .number-column {
    width: 60px;
  }


  .manage-column {
    width: 120px;
  }

}

</style>
