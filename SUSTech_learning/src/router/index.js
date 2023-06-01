import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/F1index.vue'
import NProgress from "nprogress";
import "nprogress/nprogress.css";

NProgress.configure({
  showSpinner: false,
});

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../pages/login/F1index.vue'),
    meta: {
      title: 'Login'
    }
  },
  {
    path: '/test',
    name: 'test',
    component: () => import('../components/test'),
    meta: {
      title: 'test'
    }
  },
  {
    path: '/exportPDF',
    meta: {
      title: `导出 PDF | `
      // title: `导出 PDF | ${window.$appTitle}`
    },
    component: () => import('../components/edit/ExportPdf')
    // component: resolve => require(['../components/edit/ExportPdf'], resolve)
  },

  {
    path: '',
    name: 'layout',
    component: Layout,
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('../pages/home/F1index')
      },
      {
        path: '/forum/:type/:content',
        name: 'forum',
        component: () => import('../pages/forum/F1index')
      },
      {
        path: '/user/:username',
        name: 'user',
        component: () => import('../pages/user/F1index')
      },
      {
        path: '/edit',
        name: 'edit',
        component: () => import('../pages/edit/F1index'),
        children: [

        ]
      },
      {
        path: '/read',
        name: 'read',
        component: () => import('../pages/read/F1index'),
        children: [

        ]
      },
      {
        path: ':pathMatch(.*)*',
        name: 'notFound',
        component: () => import('../pages/error/F1index.vue')
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    }
    return { x: 0, y: 0 };
  },
  routes: routes,
})

router.beforeEach((to, from, next) => {
  NProgress.start();
  next();
})

router.afterEach(() => {
  NProgress.done();
});

export default router
