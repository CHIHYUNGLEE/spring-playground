import { Routes, Route } from 'react-router-dom'
import BoardList from './pages/BoardList'


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

    </Routes>
  )
}

export default App