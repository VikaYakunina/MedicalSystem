<template>
  <div>
    <table width="90%" cellpadding="5">
      <tr width="30%" style="vertical-align: top;">
        <td>
          <div class="medicinelistadd-inner">
            <form @submit.prevent="handleSubmit">
              <error v-if="error" :error="error"></error>
              <h3>Добавление лекарств</h3>
              <div class="form-group">
                <label>Тип лекарства</label><br/>
                <select v-model="medicinetypeid">
                  <option disabled value="">Выберите тип</option>
                  <option v-for="medicinetype in medicinetypes" :key="medicinetype.id" :medicinetype="medicinetype" :value="medicinetype.id">{{medicinetype.typename}}</option>
                </select>
                          <!--    <span>Выбрано: {{ medicinetypeid }}</span>-->
                <br/>
                <label>Название</label>
                <input type="text" class="form-control" v-model="name" placeholder="название"/>
                <label>Количество</label>
                <input type="text" class="form-control" v-model="cnt" placeholder="количество"/>
              </div>
              <button class="btn btn-primary btn-block">Сохранить</button>
            </form>
          </div>
        </td>
        <td>
          <div class="medicinelist-inner">
            <div>
              <h2>Лекарства</h2>
              <table class="table table-striped">
                <tbody class="table-striped">
                <tr v-for="medicinelist in medicinelists" :key="medicinelist.id" :medicinelist="medicinelist">
                  <td>{{ medicinelist.medtype.typename }}</td>
                  <td>{{ medicinelist.name }}</td>
                  <td>{{ medicinelist.cnt }}</td>
                  <td><input class="btn btn-primary btn-sm mr-1" type="button" value="Изменить" @click="editmedicinelist(medicinelist.id, medicinelist.medtype.id, medicinelist.name, medicinelist.cnt)" /></td>
                  <td><input class="btn btn-secondary btn-sm"  type="button" value="Удалить" @click="delmedicinelist(medicinelist.id)" /></td>
                </tr>
                </tbody>
              </table>
            </div>
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
  name: 'MedicineList',
  components: {Error},
  computed: {
    ...mapGetters(['token'])
  },
  data() {
    return {
      medicinelists: [],
      medicinetypeid: '',
      name: '',
      cnt: '',
      error: '',
      selectedmedicinetype: '',
      medicinetypes: []
    }
  },
  async created() {       // получаем лекарства при открытии страницы
    const response = await axios.get('/medstorage', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(response.data);
    this.medicinelists = response.data;
    this.medicinelists = this.medicinelists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
    //----------- получаем типы лекарств для выбора--------------------------------------------
    const responsemedicinetype = await axios.get('/medtype', {
      headers: {
        Authorization: 'Bearer' + this.token  // в запросе отправляем токен
      }
    });
    console.log(responsemedicinetype.data);
    this.medicinetypes = responsemedicinetype.data;
    this.medicinetypes = this.medicinetypes.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
  },
  methods: {
    async handleSubmit() {         // асинхронно   -- добавление лекарства
      if (this.medicinetypeid == '' || this.name == '' || this.cnt == '') {
        this.error = 'Заполните все поля!';
      } else {
        if (this.id == null) {  //если id лекарства пустой то добавляем в БД
          const response = await axios.post('/medstorage', {
            medtype: {id: this.medicinetypeid},
            name: this.name,
            cnt: this.cnt
          }, {
            headers: {
              Authorization: 'Bearer' + this.token  // в запросе отправляем токен
            }
          });
          console.log(response);
          this.medicinelists.push(response.data);   // добавляем лекарство в список на экране
          this.medicinelists = this.medicinelists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
          this.medicinetypeid = '';               // делаем пустым поле ввода id типа лекарства
          this.name = '';
          this.cnt = '';
          this.error = '';          // нет сообщения об ошибке
        } else {  // если id лекарства не пустой то это редактирование
          const response = await axios.put('/medstorage/' + this.id, {
            medtype: {id: this.medicinetypeid, typename: this.typename},
            name: this.name,
            cnt: this.cnt
          }, {
            headers: {
              Authorization: 'Bearer' + this.token  // в запросе отправляем токен
            }
          });
          console.log("После изменения лекарства");
          console.log(response);
          var index = getIndex(this.medicinelists, this.id); // получаем порядковый номер лекарства по его id
          this.medicinelists.splice(index, 1, response.data);  //изменяем лекарство в списке на экране

          // получим название типа лекарства по id  т.к. в ответе название типа - пусто
          const responsemedicinetypename = await axios.get('/medtype/' + response.data.medtype.id, {
            headers: {
              Authorization: 'Bearer' + this.token  // в запросе отправляем токен
            }
          });
          this.medicinelists[index].medtype.typename = responsemedicinetypename.data.typename;

          this.medicinelists = this.medicinelists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
          this.medicinetypeid = '';               // делаем пустым поле ввода id типа лекарства
          this.name = '';
          this.cnt = '';
          this.id == null;
          this.error = '';          // нет сообщения об ошибке
        }
      }
    },
    editmedicinelist(id, medicinetypeid, name, cnt) { // вставка значений в форму для редактирования
      this.id = id;
      this.medicinetypeid = medicinetypeid;
      this.name = name;
      this.cnt = cnt;
    },
    delmedicinelist(id) {  // удаление лекарства
      axios.delete('/medstorage/' + id, {
        headers: {
          Authorization: 'Bearer' + this.token  // в запросе отправляем токен
        }
      }).then(result => {
        if (result.data.error == "") {
          var index = getIndex(this.medicinelists, id); // получаем порядковый номер лекарства по его id
          this.medicinelists.splice(index, 1);  //удаляем киносеанс в списке на экране
          this.medicinelists = this.medicinelists.sort((a, b) => -(a.id - b.id))  // сортируем по убыванию
          this.error = '';
        } else {
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
.medicinelist-inner {
  width: 850px;
  margin: auto;
  background: #f5eea2;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 40px 55px 45px 55px;
  border-radius: 15px;
  transition: all .3s;
}

.medicinelistadd-inner {
  width: 450px;
  margin: auto;
  background: #fafbdc;
  box-shadow: 0px 14px 80px rgba(34,35,58,0.2);
  padding: 20px 35px 25px 35px;
  border-radius: 10px;
  transition: all .3s;
}
</style>