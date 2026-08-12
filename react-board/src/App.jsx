import { Routes, Route } from 'react-router-dom'
import BoardList from './pages/BoardList'
import BoardDetail from './pages/BoardDetail'
import BoardWrite from './pages/BoardWrite'
import BoardEdit from './pages/BoardEdit'

function App() {

  return (
    <Routes>

      <Route
        path="/board"
        element={<BoardList />}
      />

      <Route
        path="/"
        element={<BoardList />}
      />

	  <Route
	    path="/board/:id"
	    element={<BoardDetail />}
	  />
	  
	  <Route
	    path="/board/write"
	    element={<BoardWrite />}
	  />
	  
	  <Route
	    path="/board/edit/:id"
	    element={<BoardEdit />}
	  />
	  
    </Routes>
  )
}

export default App