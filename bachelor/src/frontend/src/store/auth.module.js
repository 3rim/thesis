import AuthService from '../services/authService';
import VueJwtDecode from 'vue-jwt-decode'
import jwt_decode from 'jwt-decode';
import { faL } from '@fortawesome/free-solid-svg-icons';


const user = JSON.parse(localStorage.getItem('user'));
const initialState = user
  ? { status: { loggedIn: true,autoLogout:false }, user }
  : { status: { loggedIn: false ,autoLogout:true}, user: null };

export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit,dispatch}, user) {
      return AuthService.login(user).then(
        user => {
          if(user.initialLogin){
            commit('loginFailure');
            return Promise.resolve(user);
            
          }
          else{
            commit('loginSuccess', user);

            const decoded =jwt_decode(user.jwt)
            let expirationTime = decoded.exp *1000;
            console.log(expirationTime);
            setTimeout(()=>{
              dispatch("auto_logout")
            },3*1000)
  
            return Promise.resolve(user);
          }
          
        },
        error => {
          commit('loginFailure');
          return Promise.reject(error);
        }
      );
    },
    logout({ commit }) {
      console.log("logout")
      AuthService.logout();
      commit('logout');
    },
    auto_logout({commit,dispatch}){
      commit('autoLogout')
      dispatch('logout');
    }
  },
  mutations: {
    loginSuccess(state, user) {
      state.status.loggedIn = true;
      state.status.autoLogout = false;
      state.user = user;
    },
    loginFailure(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    autoLogout(state,user){
      state.status.loggedIn = false;
      state.status.autoLogout = true;
      state.user = null;
    }
  }
};