import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import BoardList from '../views/BoardList.vue'
import BoardWrite from '../views/BoardWrite.vue'
import BoardEdit from '../views/BoardEdit.vue'
import BoardDetail from '@/views/BoardDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
    },
    {
      path: '/board',
      name: 'board',
      component: BoardList
    },
    {
      path:'/board/write',
      name:'boardWrite',
      component:BoardWrite
    },
    {
      path:'/board/edit/:id',
      name:'boardEdit',
      component:BoardEdit
    },
    {
      path:'/board/detail/:id',
      component: BoardDetail
    }
  ],
})

export default router
