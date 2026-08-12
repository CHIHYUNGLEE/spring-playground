import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'


function BoardWrite() {

  const navigate = useNavigate()

  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')


  async function save() {

    if (!title.trim()) {
      alert('제목은 필수 항목입니다.')
      return
    }

    if (!content.trim()) {
      alert('내용은 필수 항목입니다.')
      return
    }


    const post = {
      title: title,
      content: content
    }


    await axios.post(
      'http://localhost:9090/api/posts',
      post
    )


    navigate('/board')

  }


  function goBack() {

    navigate('/board')

  }


  return (
    <div>

      <h1>게시글 작성</h1>

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


      <button onClick={save}>
        등록
      </button>

    </div>
  )
}

export default BoardWrite