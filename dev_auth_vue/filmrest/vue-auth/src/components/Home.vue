<template>
  <div class="auth-inner">
    <div>
      <h3 v-if="user">Привет {{user.username}} {{user.id}}</h3>
<!--      <h3 v-if="user">Система продажи билетов</h3>-->
      <h3 v-if="!user">Необходимо войти в систему</h3>
    </div>
  </div>
</template>

<script>
  import {mapGetters} from 'vuex'

  export default {
    name: 'Home',
    computed: {
      ...mapGetters(['user'])
    }
  }
</script>