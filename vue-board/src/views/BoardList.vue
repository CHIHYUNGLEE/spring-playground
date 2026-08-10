<template>

<h1>게시글 목록</h1>

<button @click="goWrite">
  등록
</button>

<input 
  v-model="keyword"
  placeholder="검색어 입력"
/>


<table border="1">

    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>관리</th>
    </tr>


    <tr v-for="post in filteredPosts">
        <td>{{post.id}}</td>

        <td @click="goDetail(post.id)">
            {{post.title}}
        </td>

        <td>

        <button @click="goEdit(post.id)">
            수정
        </button>

        <button @click="remove(post.id)">
            삭제
        </button>

        </td>
    </tr>

</table>

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