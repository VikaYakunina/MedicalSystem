<template>
  <div class="auth-inner">
    <form @submit.prevent="handleSubmit">

      <error v-if="error" :error="error"></error>

      <h3>Регистрация</h3>
      <div class="form-group">
        <label>Пользователь</label>
        <input type="text" class="form-control" v-model="username" placeholder="имя пользователя"/>
      </div>
      <div class="form-group">
        <label>Пароль</label>
        <input type="password" class="form-control" v-model="password" placeholder="пароль"/>
      </div>
      <div class="form-group">
        <label>Номер телефона</label>
        <input type="text" class="form-control" v-model="phonenumber" placeholder="телефонный номер"/>
      </div>
      <button class="btn btn-primary btn-block">Зарегистрироваться</button>
    </form>
  </div>
</template>

<script>
  import axios from 'axios'
  import Error from "@/components/Error";

  export default {
    name: 'Register',
    components: {Error},
    data() {
      return {
        username: '',
        password: '',
        phonenumber: '',
        error: ''
      }
    },
    methods: {
      async handleSubmit() {         // асинхронно
        try {
          const response = await axios.post('/register', {
            username: this.username,
            password: this.password,
            phonenumber: this.phonenumber
          });
          console.log(response);
          // здесь нужно проверить что регистрация прошла нормально
          // пользователь может уже существовать или короткие логин или пароль
          this.error=response.data.error;

          if(this.error==null) {  // если регистрация прошла нормально
            this.$router.push('/login');  // переход на логин после регистрации
          }
        }catch (e){
          this.error = "Ошибка регистрации";
        }
      }
    }
  }

</script>