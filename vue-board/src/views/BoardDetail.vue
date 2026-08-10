<template>

<h1>게시글 상세</h1>

<div v-if="post">

    <p>번호 : {{ post.id }}</p>
    <h2>제목 : {{ post.title }}</h2> 
    <h2>내용 : {{ post.content }}</h2>

</div>


<button @click="goList">
목록
</button>


</template>


<script setup>

import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'


const route = useRoute()
const router = useRouter()


const post = ref(null)

console.log(route.params);
async function fetchPost(){

    const response = await axios.get(
        `http://localhost:9090/api/posts/${route.params.id}`
    )


    post.value = response.data

}


function goList(){

    router.push('/board')

}


onMounted(() => {

    fetchPost()

})


</script>