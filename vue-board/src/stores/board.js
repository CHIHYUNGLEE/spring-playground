import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'


export const useBoardStore = defineStore('board', () => {

    // 게시글 목록 
	const posts = ref([]) 
	
	// 현재 페이지 
	const currentPage = ref(0) 
	
	// 전체 페이지 수 
	const totalPages = ref(0) 
	
	// 전체 게시글 수 
	const totalElements = ref(0) 
	
	// 페이지당 게시글 수 
	const pageSize = ref(10)
	
	//정렬
	const sort = ref('id,desc')

	
	// 게시글 목록 조회 
	async function fetchPosts( 
		page = 0, 
		keyword = '' 
	) { 
		const response = await axios.get( 
			'http://localhost:9090/api/posts', 
			{
				params: { 
					page: page, 
					size: pageSize.value, 
					keyword: keyword,
					sort: sort.value 
				} 
			} 
		)
		posts.value = response.data.content 
		currentPage.value = response.data.number 
		totalPages.value = response.data.totalPages 
		totalElements.value = response.data.totalElements 
	}
		
	// 게시글 등록	
    async function addPost(post){

        await axios.post(
            'http://localhost:9090/api/posts',
            post
        )

        await fetchPosts()

    }

	// 게시글 수정
    async function updatePost(id, post){

        await axios.put(
            `http://localhost:9090/api/posts/${id}`,
            post
        )


        await fetchPosts()

    }

	// 게시글 삭제
    async function deletePost(id){

        await axios.delete(
            `http://localhost:9090/api/posts/${id}`
        )


        await fetchPosts()

    }

    return {
		posts,
		
		currentPage,
		totalPages,
		totalElements,
		pageSize,
		sort,
		
		fetchPosts, 
		
		addPost,
		updatePost, 
		deletePost
    }

})