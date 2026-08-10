<template>

	<div class="board-container">

	  <!-- 헤더 -->
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
	      @keyup.enter="search"
	    />

	    <button
	      class="search-button"
	      @click="search"
	    >
	      검색
	    </button>

	  </div>


	  <!-- ⭐ table 시작 -->
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
	        v-for="post in boardStore.posts"
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


	      <tr v-if="boardStore.posts.length === 0">

	        <td
	          colspan="3"
	          class="empty-row"
	        >
	          게시글이 없습니다.
	        </td>

	      </tr>

	    </tbody>

	  </table>
	  <!-- ⭐ table 끝 -->


	  <!-- ⭐ 페이징은 table 밖 -->
	  <div class="pagination">

	    <button
	      :disabled="boardStore.currentPage === 0"
	      @click="changePage(boardStore.currentPage - 1)"
	    >
	      이전
	    </button>


	    <button
	      v-for="page in boardStore.totalPages"
	      :key="page"
	      :class="{ active: boardStore.currentPage === page - 1 }"
	      @click="changePage(page - 1)"
	    >
	      {{ page }}
	    </button>


	    <button
	      :disabled="boardStore.currentPage >= boardStore.totalPages - 1"
	      @click="changePage(boardStore.currentPage + 1)"
	    >
	      다음
	    </button>

	  </div>

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

async function search() {

  await boardStore.fetchPosts(
    0,
    keyword.value
  )

}

async function changePage(page) {

  await boardStore.fetchPosts(
    page,
    keyword.value
  )

}

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

/* =========================
   페이지 버튼
========================= */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 5px;
  margin-top: 28px;
}

.pagination button {
  width: 36px;
  height: 36px;
  padding: 0;

  border: 1px solid #e5e5e5;
  border-radius: 7px;

  background: #fff;
  color: #666;

  font-size: 13px;
  cursor: pointer;
}

.pagination button.active {
  background: #222;
  color: #fff;
  border-color: #222;
}

</style>
