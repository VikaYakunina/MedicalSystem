<template>
  <div>
    <table width="90%" cellpadding="5">
      <tr width="30%" style="vertical-align: top;">
        <td>
          <div class="medlist-inner">
            <div>
              <h2>Лекарства</h2>
              <table class="table table-striped">
                <tbody class="table-striped">
                <tr v-for="medlist in medlists" :key="medlist.id" :medlist="medlist">
                  <td>{{ medlist.medtype.typename }}</td>
                  <td>{{ medlist.name }}</td>
                  <td>{{ medlist.cnt }}</td>
                  <td><input class="btn btn-primary btn-sm mr-1" type="button" value="Выбрать" @click="medchoose(medlist.id, medlist.name)" /></td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </td>
        <td>
          <div class="med-inner">
            <form @submit.prevent="handleSubmit">
              <error v-if="error" :error="error"></error>
              <h3>Поиск лекарств</h3>
              <div class="form-group">
                <label>Тип лекарства</label><br/>
                <select v-model="medtypeid">
                  <option disabled value="">Выберите тип</option>
                  <option v-for="medtype in medtypes" :key="medtype.id" :medtype="medtype" :value="medtype.id">{{medtype.typename}}</option>
                </select>
                <!--    <span>Выбрано: {{ medicinetypeid }}</span>-->
                <br/>
                <label>Название</label>
                <input type="text" class="form-control" v-model="name" placeholder="название"/>
              </div>
              <button class="btn btn-primary btn-block">Найти</button>
            </form>
          </div>
          <br>
          <div class="medapply-inner">
            <h2>В заявке</h2>
            <table class="table table-striped">
              <tbody class="table-striped">
              <tr v-for="medapply in medapplys" :key="medapply.id" :medapply="medapply">
                <td>{{ medapply.name }}</td>
              </tr>
              </tbody>
            </table>
            <input class="btn btn-primary btn-sm mr-1" type="button" value="Заказать" @click="medrequest" />
          </div>
        </td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from "axios";
import Error from "@/components/Error";
import {mapGetters} from 'vuex';

export default {
  name: 'MedicineApply',
  components: {Error},
  computed: {
    ...mapGetters(['token'])
  },
  data() {
    return {
      medlists: [],
      medtypeid: '',
      name: '',
      cnt: '',
      error: '',
      selectedmedtype: '',
      medtypes: [],
      medapplys: []
    }
  },
  created: async function () {       // получаем лекарства при открытии страницы
    const response = await axios.get('/medstorage', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(response.data);
    this.medlists = response.data;
    this.medlists = this.medlists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
    //----------- получаем типы лекарств для выбора--------------------------------------------
    const responsemedtype = await axios.get('/medtype', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(responsemedtype.data);
    this.medtypes = responsemedtype.data;
    this.medtypes.push({id : 0, typename:"все"});
    this.medtypes = this.medtypes.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
  },
  methods: {
    async handleSubmit() {
      let urlstr='/medstorage';
      let fl=0;
      if (this.medtypeid!='' && this.medtypeid!="0"){ // если выбрать тип лекарства
        fl=1;
        urlstr=urlstr+'?medtypeid='+this.medtypeid;
      }
      if (this.name!=''){     // если указано название лекарства
        if (fl==0){
          urlstr=urlstr+'?name='+this.name;
        }else{
          urlstr=urlstr+'&name='+this.name;
        }
      }
      const response = await axios.get(urlstr, {
        headers: {
          Authorization: 'Bearer' + this.token  // в запросе отправляем токен
        }
      });
      console.log(response.data);
      this.medlists = response.data;
      this.medlists = this.medlists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
    },
    medchoose(id, name) { // добавление лекарства в заявку
      let now = new Date();
      let d = now.getDate();
      let m = now.getMonth();
      let y = now.getFullYear();
      d = (d < 10) ? '0' + d : d;
      m = (m < 10) ? '0' + m : m;
      y = (y < 10) ? '0' + y : y;
      let tekdate = d + '.' + m + '.' + y;
      this.medapplys.push({id: id, name: name, requestdate: tekdate , status: 0})
    },
    async medrequest(){
      for(let i=0; i<this.medapplys.length; i++){
        // -- Сохраняем в БД заявки
        const response = await axios.post('/medrequest', {
          medstorage: {id: this.medapplys[i].id},
          requestdate: this.medapplys[i].requestdate,
          status: 0
        },{
          headers: {
            Authorization: 'Bearer' + this.token   // в запросе отправляем токен
          }
        });
        console.log(response);
      }
      this.$router.push('/medicinerequestlist');     // переход на страницу списка заявок
    }
  }

}
</script>

<style>
.medlist-inner {
  width: 850px;
  margin: auto;
  background: #f5eea2;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 40px 55px 45px 55px;
  border-radius: 15px;
  transition: all .3s;
}

.med-inner {
  width: 450px;
  margin: auto;
  background: #fafbdc;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 20px 35px 25px 35px;
  border-radius: 10px;
  transition: all .3s;
}
.medapply-inner {
  width: 450px;
  margin: auto;
  background: #e9ecef;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 20px 35px 25px 35px;
  border-radius: 10px;
  transition: all .3s;
}
</style>