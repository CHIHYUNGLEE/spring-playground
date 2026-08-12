import { Routes, Route } from 'react-router-dom'
import BoardList from './pages/BoardList'
import BoardDetail from './pages/BoardDetail'


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
	  
    </Routes>
  )
}

export default App