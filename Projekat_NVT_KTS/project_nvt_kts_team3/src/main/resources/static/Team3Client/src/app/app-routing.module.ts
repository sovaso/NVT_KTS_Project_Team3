import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterAdminComponent } from './pages/register-admin/register-admin.component';
import { RegisterUserComponent } from './pages/register-user/register-user.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';




const routes: Routes = [
 {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'registerAdmin',
    component: RegisterAdminComponent,
  },
  {
    path: 'registerUser',
    component: RegisterUserComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
   
  },
  
  {path: '**', redirectTo: 'dashboard'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }