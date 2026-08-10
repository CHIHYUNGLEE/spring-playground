import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'


export const useBoardStore = defineStore('board', () => {

    const posts = ref([])

    async function fetchPosts(){

        const response = await axios.get(
            'http://localhost:9090/api/posts'
        )

        posts.value = response.data

    }

    async function addPost(post){

        await axios.post(
            'http://localhost:9090/api/posts',
            post
        )

        await fetchPosts()

    }

    async function updatePost(id, post){

        await axios.put(
            `http://localhost:9090/api/posts/${id}`,
            post
        )


        await fetchPosts()

    }

    async function deletePost(id){

        await axios.delete(
            `http://localhost:9090/api/posts/${id}`
        )


        await fetchPosts()

    }

    return {
        posts,
        fetchPosts,
        addPost,
        updatePost,
        deletePost
    }

})