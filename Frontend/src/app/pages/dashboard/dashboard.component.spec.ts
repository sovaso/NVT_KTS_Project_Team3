import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { HeaderComponent } from 'src/app/header/header.component';
import { MenuBarComponent } from 'src/app/menu-bar/menu-bar.component';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';
import { RouterOutlet, RouterModule, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/security/authentication-service.service';
import {RouterTestingModule} from '@angular/router/testing';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DashboardComponent,  HeaderComponent,  MenuBarComponent],
      imports: [
        NgbModule,
        RouterTestingModule
      ],
      providers: [
        NgbActiveModal,
        NgModule,
        RouterOutlet,
        {provide: AuthenticationService, useClass: AuthenticationServiceMock},
        {provide: Router, useClass: RouterMock},
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class AppRoutingModuleMock{

}

class AuthenticationServiceMock{

}

class RouterMock{

}