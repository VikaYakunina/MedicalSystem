<template>
  <div>
    <table width="90%" cellpadding="5">
      <tr width="30%" style="vertical-align: top;">
        <td>
          <div class="medlist-inner">
            <div>
              <h2>Выдача лекарств</h2>
              <table class="table table-striped">
                <tbody class="table-striped">
                <tr v-for="medissue in medissues" :key="medissue.id" :medlist="medissue">
                  <td>{{ medissue.medstorage.name }}</td>
                  <td>{{ medissue.medstorage.medtype.typename }}</td>
                  <td>{{ medissue.issuedate }}</td>
                  <td>{{ medissue.cnt }}</td>
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
              <h3>Поиск назначений на лекарства</h3>
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
  name: 'MedicineIssueList',
  components: {Error},
  computed: {
    ...mapGetters(['token'])
  },
  data() {
    return {
      medissues: [],
      medtypeid: '',
      name: '',
      cnt: '',
      error: '',
      selectedmedtype: '',
      medtypes: [],
    }
  },
  created: async function () {       // получаем выдачи при открытии страницы
    const response = await axios.get('/medissue', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(response.data);
    this.medissues = response.data;
    this.medissues = this.medissues.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
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
    async handleSubmit(){
      let urlstr='/medissue';
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
      this.medissues = response.data;
      this.medissues = this.medissues.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
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
</style>