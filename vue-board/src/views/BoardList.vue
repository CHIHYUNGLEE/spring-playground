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


	  <!-- 검색 및 정렬-->
	  <div class="search-area">

	    <input
	      v-model="keyword"
	      placeholder="검색어를 입력하세요"
	      @keyup.enter="search"
	    />

	    <select
	      v-model="boardStore.sort"
	      @change="changeSort"
	      class="sort-select"
	    >
	      <option value="id,desc">
	        최신순
	      </option>

	      <option value="id,asc">
	        오래된순
	      </option>
	    </select>

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
	  <div class="pagination-area">

	    <!-- 페이지당 게시글 수 -->

	    <div class="page-size">

	      <select
	        v-model.number="boardStore.pageSize"
	        @change="changePageSize"
	      >
	        <option :value="10">10개</option>
	        <option :value="20">20개</option>
	        <option :value="50">50개</option>
	        <option :value="100">100개</option>
	      </select>

	      <span>씩 보기</span>

	    </div>


	    <!-- 페이지 번호 -->

		<div class="pagination">

		  <!-- 처음 -->

		  <button
		    :disabled="boardStore.currentPage === 0"
		    @click="changePage(0)"
		  >
		    «
		  </button>


		  <!-- 이전 -->

		  <button
		    :disabled="boardStore.currentPage === 0"
		    @click="changePage(boardStore.currentPage - 1)"
		  >
		    ‹
		  </button>


		  <!-- 첫 페이지 -->

		  <button
		    v-if="startPage > 1"
		    @click="changePage(0)"
		  >
		    1
		  </button>


		  <!-- 앞쪽 ... -->

		  <span
		    v-if="startPage > 2"
		    class="ellipsis"
		  >
		    ...
		  </span>


		  <!-- 페이지 번호 -->

		  <button
		    v-for="page in pageNumbers"
		    :key="page"
		    :class="{
		      active: boardStore.currentPage === page
		    }"
		    @click="changePage(page)"
		  >
		    {{ page + 1 }}
		  </button>


		  <!-- 뒤쪽 ... -->

		  <span
		    v-if="endPage < boardStore.totalPages - 2"
		    class="ellipsis"
		  >
		    ...
		  </span>


		  <!-- 마지막 페이지 -->

		  <button
		    v-if="endPage < boardStore.totalPages - 1"
		    @click="changePage(boardStore.totalPages - 1)"
		  >
		    {{ boardStore.totalPages }}
		  </button>


		  <!-- 다음 -->

		  <button
		    :disabled="
		      boardStore.currentPage >= boardStore.totalPages - 1
		    "
		    @click="changePage(boardStore.currentPage + 1)"
		  >
		    ›
		  </button>


		  <!-- 마지막 -->

		  <button
		    :disabled="
		      boardStore.currentPage >= boardStore.totalPages - 1
		    "
		    @click="changePage(boardStore.totalPages - 1)"
		  >
		    »
		  </button>

		</div>
		<!-- 페이지 번호 끝-->

	  </div>

	</div>
	
</template>


<script setup>

import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useBoardStore } from '@/stores/board'

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


//페이지 이동 함수
async function changePage(page) {

  await boardStore.fetchPosts(
    page,
    keyword.value
  )

}


//페이지 당 게시글 수 변경
async function changePageSize() {

  await boardStore.fetchPosts(
    0,
    keyword.value
  )

}

//정렬
async function changeSort() {

  await boardStore.fetchPosts(
    0,
    keyword.value
  )

}

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

//페이지 계산
const startPage = computed(() => {

  const current = boardStore.currentPage
  const total = boardStore.totalPages

  if (total <= 7) {
    return 0
  }

  if (current <= 3) {
    return 1
  }

  if (current >= total - 4) {
    return total - 6
  }

  return current - 2

})

//페이지 계산
const endPage = computed(() => {

  const total = boardStore.totalPages

  if (total <= 7) {
    return total - 1
  }

  if (boardStore.currentPage <= 3) {
    return 5
  }

  if (boardStore.currentPage >= total - 4) {
    return total - 2
  }

  return boardStore.currentPage + 2

})

//페이지 계산
const pageNumbers = computed(() => {

  const pages = []

  for (
    let i = startPage.value;
    i <= endPage.value;
    i++
  ) {

    pages.push(i)

  }

  return pages

})

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

.pagination-area {
  position: relative;

  display: flex;
  justify-content: center;
  align-items: center;

  margin-top: 28px;
}


.page-size {
  position: absolute;
  right: 0;

  display: flex;
  align-items: center;
  gap: 6px;

  color: #777;
  font-size: 13px;
}


.page-size select {
  height: 34px;

  padding: 0 28px 0 10px;

  border: 1px solid #ddd;
  border-radius: 6px;

  background: white;

  font-size: 13px;

  cursor: pointer;
}


.pagination {
  display: flex;
  justify-content: center;
  align-items: center;

  gap: 5px;
}


.pagination button {
  width: 36px;
  height: 36px;

  border: 1px solid #e5e5e5;
  border-radius: 7px;

  background: white;
  color: #666;

  font-size: 13px;
  cursor: pointer;

  transition: 0.15s;
}


.pagination button:hover:not(:disabled) {
  background: #f5f5f5;
  border-color: #bbb;
}


.pagination button.active {
  background: #222;
  color: white;
  border-color: #222;
}


.pagination button:disabled {
  color: #ccc;
  background: #fafafa;
  cursor: default;
}

.sort-select {
  height: 40px;

  padding: 0 12px;

  border: 1px solid #ddd;
  border-radius: 7px;

  background: white;

  font-size: 14px;

  cursor: pointer;
}
</style>
