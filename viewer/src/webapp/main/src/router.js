import Vue from 'vue';
import Router from 'vue-router';
import Feeds from "./views/Home";
import store from "./store";

Vue.use(Router);

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: Feeds
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    },
    {
      path: '/login',
      name: 'login',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/LoginPage.vue')
    }
  ],

});

router.beforeEach((to, from, next) => {
  console.log(to);
  if (to.name !== "login") {
    if (store.state.session.token == null) {
      next("/login");
    } else {
      next();
    }
  } else {
    next();
  }

});

export default router


