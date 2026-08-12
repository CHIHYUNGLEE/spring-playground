import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'


function BoardList() {

  const navigate = useNavigate()


  // 게시글 목록
  const [posts, setPosts] = useState([])

  // 검색어
  const [keyword, setKeyword] = useState('')

  // 현재 페이지
  const [currentPage, setCurrentPage] = useState(0)

  // 전체 페이지 수
  const [totalPages, setTotalPages] = useState(0)

  // 페이지당 게시글 수
  const [pageSize, setPageSize] = useState(10)


  useEffect(() => {

    async function fetchPosts() {

      const response = await axios.get(
        'http://localhost:9090/api/posts',
        {
          params: {
            page: currentPage,
            size: pageSize,
            keyword: keyword
          }
        }
      )

      setPosts(response.data.content)
      setTotalPages(response.data.totalPages)

    }

    fetchPosts()

  }, [currentPage, pageSize, keyword])


  // 검색
  function search() {

    setCurrentPage(0)

  }


  // 페이지 이동
  function changePage(page) {

    setCurrentPage(page)

  }


  // 페이지당 게시글 수 변경
  function changePageSize(event) {

    setPageSize(Number(event.target.value))
    setCurrentPage(0)

  }


  return (
    <div>

      <h1>게시판</h1>


      {/* 검색 */}

      <div>

        <input
          value={keyword}
          onChange={(event) =>
            setKeyword(event.target.value)
          }
          onKeyDown={(event) => {

            if (event.key === 'Enter') {
              search()
            }

          }}
          placeholder="검색어를 입력하세요"
        />


        <button onClick={search}>
          검색
        </button>

      </div>


      {/* 등록 */}

      <button
        onClick={() => navigate('/board/write')}
      >
        + 등록
      </button>


      {/* 게시글 목록 */}

      <table>

        <thead>

          <tr>
            <th>번호</th>
            <th>제목</th>
          </tr>

        </thead>


        <tbody>

          {posts.map((post) => (

            <tr key={post.id}>

              <td>
                {post.id}
              </td>


              <td
                onClick={() =>
                  navigate(`/board/${post.id}`)
                }
              >
                {post.title}
              </td>

            </tr>

          ))}


          {posts.length === 0 && (

            <tr>

              <td colSpan="2">
                게시글이 없습니다.
              </td>

            </tr>

          )}

        </tbody>

      </table>


      {/* 페이지당 게시글 수 */}

      <select
        value={pageSize}
        onChange={changePageSize}
      >

        <option value="10">
          10개
        </option>

        <option value="20">
          20개
        </option>

        <option value="50">
          50개
        </option>

        <option value="100">
          100개
        </option>

      </select>


      {/* 페이징 */}

      <div>

        <button
          disabled={currentPage === 0}
          onClick={() =>
            changePage(currentPage - 1)
          }
        >
          이전
        </button>


        {Array.from(
          { length: totalPages },
          (_, index) => (

            <button
              key={index}
              onClick={() =>
                changePage(index)
              }
            >
              {index + 1}
            </button>

          )
        )}


        <button
          disabled={
            currentPage >= totalPages - 1
          }
          onClick={() =>
            changePage(currentPage + 1)
          }
        >
          다음
        </button>

      </div>

    </div>
  )
}


export default BoardList