<template>

  <h1>게시글 수정</h1>


  <div>
    제목:
    <input v-model="title">
  </div>


  <button @click="update">
    수정 완료
  </button>


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

async function update(){

    await boardStore.updatePost(
        id,
        {
            title:title.value
        }
    )


    router.push('/board')

}
</script>