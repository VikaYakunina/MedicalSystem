<template>
  <nav class="navbar navbar-expand navbar-light fixed-top">
    <div class="container">
      <router-link to="/" class="navbar-brand">Главная</router-link>
      <div v-if="user">
        <router-link to="/medicine" class="navbar-brand" v-if="user.username === 'nurse'">Лекарства</router-link>
        <router-link to="/medicineapply" class="navbar-brand" v-if="user.username === 'nurse'">Заказать</router-link>
        <router-link to="/medicinerequestlist" class="navbar-brand" v-if="user.username === 'nurse'">Заявки</router-link>
        <router-link to="/medicineissuelist" class="navbar-brand" v-if="user.username === 'nurse'">Назначения</router-link>
        <router-link to="/medicinelist" class="navbar-brand" v-if="user.username === 'head'">Препараты</router-link>
        <router-link to="/medicinerequestlist" class="navbar-brand" v-if="user.username === 'head'">Заявки</router-link>
        <router-link to="/medicinetype" class="navbar-brand" v-if="user.username === 'head'">Типы препаратов</router-link>
      </div>
      <div class="collapse navbar-collapse">
        <ul class="navbar-nav ml-auto" v-if="!user">
          <li class="nav-item">
            <router-link to="/login" class="nav-link">Войти</router-link>
          </li>
           <!-- <li class="nav-item">
            <router-link to="/register" class="nav-link">Зарегистрироваться</router-link>
          </li>-->
        </ul>
        <ul class="navbar-nav ml-auto" v-if="user">
          <li class="nav-item">
            <a href="/" @click="handleClick" class="nav-link">Выйти ({{user.username}})</a>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: 'Nav',
    methods: {
      handleClick() {
        localStorage.removeItem('token');             // удаляем токен если Выйти-Logout
        this.$store.dispatch('user', null);   // удаляем пользователя из vuex хранилища
        this.$router.push('/');
      }
    },
    computed: {
      ...mapGetters(['user'])
    }
  }
</script>