import Vue from 'vue';
import Router from 'vue-router';
import store from "./store";
import * as localforage from "localforage";

Vue.use(Router);

const router = new Router({
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import(/* webpackChunkName: "about" */ './views/Home.vue')
    },
    {
      path: '/login',
      name: 'login',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/LoginPage.vue')
    },
    {
      path: '/signup',
      name: 'signup',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/SignupPage.vue')
    },
    {
      path: '/confirmation/:token',
      name: 'confirmation',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/ConfirmationPage.vue')
    },
    {
      path: '/resetPassword/:token',
      name: 'resetPassword',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/ResetPasswordPage.vue')
    },
  ],

});

router.beforeEach((to, from, next) => {
  // console.log("navigation to: " + to);
  updateStoreFromLocalStorage().then(() => {
    let pagesAccessibleWithoutToken = ["login", "signup", "confirmation", "resetPassword"];
    if (pagesAccessibleWithoutToken.includes(to.name) || pagesAccessibleWithoutToken.includes(to.hash.substr(2))) {
      console.log("page does not require token");
      next();
    } else {
      console.log("page requires token");
      if (store.state.session.token == null) {
        console.log("token not set - redirecting to login");
        next({name: 'login'});
      } else {
        next();
      }
    }
  });
});

function updateStoreFromLocalStorage() {
  let getToken = localforage.getItem('token');
  let getUsername = localforage.getItem('username');
  return Promise.all([getToken, getUsername]).then((values) => {
    if (values[0] && values[1]) {
      store.commit('session/setToken', {token: values[0], username: values[1]});
    }
  });
}

export default router


