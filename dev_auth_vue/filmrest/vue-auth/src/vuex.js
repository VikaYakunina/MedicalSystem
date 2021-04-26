import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const state = {
    user: null,
    token: null
};
const store =new Vuex.Store({
    state,
    getters: {
        user: (state) => {
            return state.user;
        },
        token: (state) => {
            return state.token;
        }
    },
    actions: {
        user(context, user) {
            context.commit('user', user);
        },
        token(context, token) {
            context.commit('token', token);
        }
    },
    mutations: {
        user(state, user){
            state.user = user;
        },
        token(state, token){
            state.token = token;
        }
    }
});
export default store;
