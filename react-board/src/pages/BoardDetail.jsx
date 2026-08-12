import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import axios from 'axios'

function BoardDetail() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [post, setPost] = useState(null)


  useEffect(() => {

    async function fetchPost() {

      const response = await axios.get(
        `http://localhost:9090/api/posts/${id}`
      )

      setPost(response.data)

    }

    fetchPost()

  }, [id])


  if (!post) {
    return <div>게시글을 불러오는 중입니다...</div>
  }
  
  async function remove() {

    if (!confirm('게시글을 삭제하시겠습니까?')) {
      return
    }


    await axios.delete(
      `http://localhost:9090/api/posts/${id}`
    )


    navigate('/board')

  }


  return (
    <div>

      <h1>게시글 상세</h1>

      <p>번호 : {post.id}</p>

      <h2>{post.title}</h2>

      <div>
        {post.content}
      </div>

      <br />

      <button onClick={() => navigate('/board')}>
        목록
      </button>
	  <button
	    onClick={() => navigate(`/board/edit/${post.id}`)}
	  >
	    수정
	  </button>
	  
	  <button onClick={remove}>
	    삭제
	  </button>
    </div>
  )
}

export default BoardDetail