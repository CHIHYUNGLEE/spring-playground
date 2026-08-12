import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import axios from 'axios'


function BoardEdit() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')


  // 기존 게시글 조회
  useEffect(() => {

    async function fetchPost() {

      const response = await axios.get(
        `http://localhost:9090/api/posts/${id}`
      )

      setTitle(response.data.title)
      setContent(response.data.content)

    }

    fetchPost()

  }, [id])


  // 수정
  async function update() {

    if (!title.trim()) {
      alert('제목은 필수 항목입니다.')
      return
    }

    if (!content.trim()) {
      alert('내용은 필수 항목입니다.')
      return
    }


    await axios.put(
      `http://localhost:9090/api/posts/${id}`,
      {
        title: title,
        content: content
      }
    )


    navigate(`/board/${id}`)

  }


  function goBack() {

    navigate(`/board/${id}`)

  }


  return (
    <div>

      <h1>게시글 수정</h1>


      <div>

        <label>제목</label>

        <input
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="제목을 입력하세요"
        />

      </div>


      <div>

        <label>내용</label>

        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="내용을 입력하세요"
        />

      </div>


      <button onClick={goBack}>
        취소
      </button>


      <button onClick={update}>
        수정 완료
      </button>

    </div>
  )
}

export default BoardEdit