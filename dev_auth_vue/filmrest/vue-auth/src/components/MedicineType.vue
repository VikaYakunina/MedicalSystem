<template>
  <div>
    <table width="90%" cellpadding="5" style="margin-top: 10px;">
      <tr width="30%" style="vertical-align:top;">
        <td>
          <div class="medicinetypeadd-inner">
            <form @submit.prevent="handleSubmit">
              <error v-if="error" :error="error"></error>
              <h3>Добавление типа лекарства</h3>
              <div class="form-group">
                <label>Название типа лекарства</label>
                <input type="text" class="form-control" v-model="typename" placeholder="название типа лекарства"/>
              </div>
              <button class="btn btn-primary btn-block">Сохранить</button>
            </form>
          </div>
        </td>
        <td>
          <div class="medicinetype-inner">
            <h2>Типы лекарств</h2>
            <table class="table table-striped">
              <tbody class="table-striped">
              <tr v-for="medicinetype in medicinetypes" :key="medicinetype.id" :medicinetype="medicinetype">
                <td>{{ medicinetype.typename }}</td>
                <td><input class="btn btn-primary btn-sm mr-1" type="button" value="Изменить"
                           @click="editmedicinetype(medicinetype.id, medicinetype.typename)"/></td>
                <td><input class="btn btn-secondary btn-sm" type="button" value="Удалить"
                           @click="delmedicinetype(medicinetype.id)"/>
                </td>
              </tr>
              </tbody>
            </table>
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
  name: 'MedicineType',
  components: {Error},
  computed: {
    ...mapGetters(['token'])
  },
  data() {
    return {
      medicinetypes: [],
      typename: '',
      medicinetypeid: null,
      error: ''
    }
  },
  async created() {
    const response = await axios.get('/medtype', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(response.data);
    this.medicinetypes = response.data;
    this.medicinetypes = this.medicinetypes.sort((a, b) => -(a.typename - b.typename))  // сортируем по убыванию
  },
  methods: {
    async handleSubmit() {         // асинхронно   -- добавление типа лекарства
      if (this.typename==''){  // если поля не заполнены то выводим ошибку
        this.error = 'Заполните поля формы';
      }else {                     // если название типа лекарства не пустое то записываем в БД
        if (this.id==null) {    // id пустой у нового типа лекарства - значит добавляем
          const response = await axios.post('/medtype', {
            typename: this.typename
          }, {
            headers: {
              Authorization: 'Bearer' + this.token  // в запросе отправляем токен
            }
          });
          console.log(response);
          this.medicinetypes.push(response.data);   // добавляем тип лекарства в список на экране
          this.medicinetypes = this.medicinetypes.sort((a, b) => -(a.typename - b.typename))  // сортируем по убыванию
          this.typename = '';               // делаем пустым поле ввода типа лекарства
          this.error = '';
        }else{  // если id не пустой значит редактирование
          const response = await axios.put('/medtype/'+this.id, {
            typename: this.typename
          }, {
            headers: {
              Authorization: 'Bearer' + this.token  // в запросе отправляем токен
            }
          });
          console.log(response);
          var index = getIndex(this.medicinetypes, this.id); // получаем порядковый номер типа лекарства по его id
          this.medicinetypes.splice(index, 1, response.data);  //изменяем тип лекарства в списке на экране
          this.medicinetypes = this.medicinetypes.sort((a, b) => -(a.typename - b.typename))  // сортируем по убыванию
          this.typename = '';               // делаем пустым поле ввода тип лекарства
          this.id=null;
          this.error = '';
        }
      }
    },
    editmedicinetype(id, typename) {
      this.id=id;
      this.typename=typename;     // вставляем в поле формы для изменения
    },
    delmedicinetype: function (id) {
      axios.delete('/medtype/' + id, {
        headers: {
          Authorization: 'Bearer' + this.token  // в запросе отправляем токен
        }
      }).then(result => {
        if(result.data.error=="") {
          var index = getIndex(this.medicinetypes, id); // получаем порядковый номер типа лекарства по его id
          this.medicinetypes.splice(index, 1);  //удаляем тип лекарства в списке на экране
          this.medicinetypes = this.medicinetypes.sort((a, b) => -(a.typename - b.typename))  // сортируем по убыванию
          this.error = '';
        }else{
          this.error = result.data.error;
        }
      });
    }
  }
}

function getIndex(list, id) {   // функция получения порядкового номера в массиве  по id
  for (var i=0; i<list.length; i++){
    if (list[i].id == id){
      return i;
    }
  }
  return -1;
}
</script>

<style>
.medicinetype-inner {
  width: 650px;
  margin: auto;
  background: #f5eea2;
  box-shadow: 0px 14px 80px rgba(34, 35, 58, 0.2);
  padding: 20px 35px 25px 35px;
  border-radius: 15px;
  transition: all .3s;
}

.medicinetypeadd-inner {
  width: 450px;
  margin: auto;
  background: #fafbdc;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 20px 35px 25px 35px;
  border-radius: 10px;
  transition: all .3s;
}
</style>