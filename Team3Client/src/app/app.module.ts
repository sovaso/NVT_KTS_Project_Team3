import { BrowserModule } from '@angular/platform-browser';
import { NgModule, HostListener } from '@angular/core';

import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AuthenticationService } from './security/authentication-service.service';
import { CanActivateAuthGuard } from './security/can-activate-auth.guard';
import { JwtUtilsService } from './security/jwt-utils.service';
import { TokenInterceptorService } from './security/token-interceptor.service';
import { RegisterAdminComponent } from './pages/register-admin/register-admin.component';
import { RegisterUserComponent } from './pages/register-user/register-user.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

import { HeaderComponent } from './header/header.component';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { UserComponent } from './user/user.component';

import { EditProfileComponent } from './user/edit-profile/edit-profile.component';

import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { EventsComponent } from './events/events.component';
import { LocationsComponent } from './locations/locations.component';
import { MyReservationsComponent } from './my-reservations/my-reservations.component';
import { ReportsComponent } from './reports/reports.component';
import { CreateLocationComponent } from './locations/create-location/create-location.component';
import { EditLocationComponent } from './locations/edit-location/edit-location.component';
import { MaintenacesComponent } from './maintenaces/maintenaces.component';
import { CreatemaintenacesComponent } from './maintenaces/createmaintenaces/createmaintenaces.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterAdminComponent,
    RegisterUserComponent,
    DashboardComponent,
    HeaderComponent,
    MenuBarComponent,
    UserComponent,
    EditProfileComponent,
    EventsComponent,
    LocationsComponent,
    MyReservationsComponent,
    ReportsComponent,
    CreateLocationComponent,
    EditLocationComponent,
    MaintenacesComponent,
    CreatemaintenacesComponent
  ],
  imports: [
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
  ],
  providers: [
    CanActivateAuthGuard,
    JwtUtilsService,
    AuthenticationService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptorService,
      multi: true
    },
    
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    EditProfileComponent
  ],
  exports: [ EditProfileComponent ]
})
export class AppModule { 
  @HostListener("window:onbeforeunload",["$event"])
  clearLocalStorage(event){
      localStorage.clear();
  }
}
