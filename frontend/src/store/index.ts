import {createStore} from 'vuex';
import createPersistedState from 'vuex-persistedstate';
import type {State} from '../lib/types.ts';

export default createStore<State>({
    state: {
        userToken: null,
        role: null,
        language: 'pl'
    },
    mutations: {
        setUserToken(state, token) {
            state.userToken = token;
        },
        clearUserToken(state) {
            state.userToken = null;
        },
        setRole(state, role) {
            state.role = role;
        },
        clearRole(state) {
            state.role = null;
        },
        setLanguage(state, language) {
            state.language = language
        }
    },
    actions: {
        setUserToken({commit}, token) {
            commit('setUserToken', token);
        },
        clearUserToken({commit}) {
            commit('clearUserToken');
        },
        setRole({commit}, role) {
            commit('setRole', role);
        },
        clearRole({commit}) {
            commit('clearRole');
        },
        setLanguage({commit}, language) {
            commit('setLanguage', language)
        }
    },
    getters: {
        getUserToken: state => state.userToken,
        getRole: state => state.role,
        getLanguage: state => state.language
    },
    plugins: [createPersistedState()],
});