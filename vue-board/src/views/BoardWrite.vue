<template>

  <h1>게시글 등록</h1>


  <div>
    제목:
    <input v-model="title">
  </div>


  <div>
    내용:
    <textarea v-model="content"></textarea>
  </div>


  <button @click="save">
    저장
  </button>


</template>


<script setup>

import { ref } from 'vue'
import { useBoardStore } from '@/stores/board'
import { useRouter } from 'vue-router'


const title = ref("")
const content = ref("")
const boardStore = useBoardStore()
const router = useRouter()

function save(){

    const post = {
        title:title.value,
        content:content.value
    }

    boardStore.addPost(post)

    router.push('/board')

}

</script>