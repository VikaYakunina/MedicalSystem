<template>
  <div class="auth-inner">
    <form @submit.prevent="handleSubmit">

      <error v-if="error" :error="error"></error>

      <h3>Вход в систему</h3>
      <div class="form-group">
        <label>Пользователь</label>
        <input type="username" class="form-control" v-model="username" placeholder="имя пользователя"/>
      </div>
      <div class="form-group">
        <label>Пароль</label>
        <input type="password" class="form-control" v-model="password" placeholder="пароль"/>
      </div>
      <button class="btn btn-primary btn-block">Войти</button>

    </form>
  </div>
</template>

<script>
  import axios from 'axios'
  import Error from "@/components/Error";

  export default {
    name: 'Login',
    components: {
      Error
    },
    data(){
      return {
        username: '',
        password: '',
        error: ''
      }
    },
    methods: {
      async handleSubmit(){
        try {
          const response = await axios.post('/login', {
            username: this.username,
            password: this.password
          });

          //console.log(response);
          //localStorage.setItem('token', response.data.token);   // сохраняем полученный токен в localStorage
          //localStorage.setItem('user', response.data.user);   // сохраняем полученный user в localStorage

          this.$store.dispatch('token', response.data.token);  // записываем токен в vuex
          //this.$store.dispatch('user', response.data.user);   // записываем user в vuex (хранилище)  user -строка
          this.$store.dispatch('user', JSON.parse(response.data.user));   // записываем user в vuex (хранилище)

          switch(this.username) {
            case 'admin':
              this.$router.push('/film')   // если admin переходим на страницу фильмов
              break
            case 'nurse':
              this.$router.push('/medicine')   // медсестра - переход к лекарствам
              break
            case 'head':
              this.$router.push('/medicinelist')   // зав отделением  - переход к списку лекарств
              break
            default:
              this.$router.push('/filmsessionchoice')   // переходим на страницу киносеансов
              break
          }
        }catch (e){
          this.error = "Неверный логин или пароль!!!"
        }
      }
    }

  }
</script>
