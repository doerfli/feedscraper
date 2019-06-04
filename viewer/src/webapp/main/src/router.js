import Vue from 'vue';
import Router from 'vue-router';
import Feeds from "./views/Home";
import store from "./store";
import * as localforage from "localforage";

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
  updateStoreFromLocalStorage().then(() => {
    if (to.name !== "login") {
      if (store.state.session.token == null) {
        console.log("token not set - redirecting to login");
        next({name: 'login'});
      } else {
        next();
      }
    } else {
      next();
    }
  });
});

function updateStoreFromLocalStorage() {
  return localforage.getItem('token').then(function (value) {
    console.log(value);
    if (value) {
      store.commit('session/setToken', {token: value});
    }
  });
}

export default router


