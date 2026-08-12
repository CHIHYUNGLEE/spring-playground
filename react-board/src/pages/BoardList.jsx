import { useEffect, useState } from 'react'
import axios from 'axios'


function BoardList() {

  const [posts, setPosts] = useState([])


  useEffect(() => {

    async function fetchPosts() {

      const response = await axios.get(
        'http://localhost:9090/api/posts'
      )

      setPosts(response.data.content)

    }

    fetchPosts()

  }, [])


  return (
    <div>

      <h1>게시판</h1>

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

              <td>
                {post.title}
              </td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  )
}


export default BoardList