import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterUserComponent } from './pages/register-user/register-user.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EventsComponent } from './events/events.component';
import { LocationsComponent } from './locations/locations.component';
import { MyReservationsComponent } from './my-reservations/my-reservations.component';
import { ReportsComponent } from './reports/reports.component';
import {LocationCreateComponent} from 'src/app/locations/location-create/location-create.component'
import { EventCreateComponent } from './events/event-create/event-create.component';
import { EventUpdateComponent } from './events/event-update/event-update.component';
import { ReservationComponent } from './reservation/reservation.component';




const routes: Routes = [
 {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'registerAdmin',
    component: RegisterUserComponent,
  },
  {
    path: 'registerUser',
    component: RegisterUserComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'events', component: EventsComponent },
      { path: 'locations', component: LocationsComponent},
      { path: 'myReservations', component: MyReservationsComponent},
      { path : 'reports' , component : ReportsComponent},
      { path : 'addNewAdmin', component : RegisterUserComponent},
      { path: 'create-event', component: EventCreateComponent },
      { path: 'event-update', component: EventUpdateComponent },
      {path: 'reservation',component: ReservationComponent}
    ] 
  },

  {path: '**', redirectTo: 'dashboard/events'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }