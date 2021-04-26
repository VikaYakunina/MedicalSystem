import Vue from 'vue'
import Router from "vue-router";
import Home from "@/components/Home";
import Login from "@/components/Login";
import Register from "@/components/Register";
import Medicine from "@/components/Medicine";
import MedicineIssueList from "@/components/MedicineIssueList";
import MedicineApply from "@/components/MedicineApply";
import MedicineList from "@/components/MedicineList";
import MedicineRequestList from "@/components/MedicineRequestList";
import MedicineType from "@/components/MedicineType";

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
        {path: '/', component: Home},
        {path: '/login', component: Login},
        {path: '/register', component: Register},
        {path: '/medicine', component: Medicine},
        {path: '/medicineissuelist', component: MedicineIssueList},
        {path: '/medicineapply', component: MedicineApply},
        {path: '/medicinelist', component: MedicineList},
        {path: '/medicinerequestlist', component: MedicineRequestList},
        {path: '/medicinetype', component: MedicineType}
    ]
})