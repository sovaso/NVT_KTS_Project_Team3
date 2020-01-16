import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationsComponent } from './locations.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from '../app-routing.module';
import { AlertService } from '../services';
import { SharedService } from '../services/shared/shared.service';
import { LocationsService } from '../services/locations/locations.service';
import { NgAnalyzeModulesHost } from '@angular/compiler';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

describe('LocationsComponent', () => {
  let component: LocationsComponent;
  let fixture: ComponentFixture<LocationsComponent>;
  let sharedService : any;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationsComponent ],
      imports: [NgbModule],
      providers: [
        {provide: AlertService, useClass: AlertServiceMock},
        {provide: SharedService, useClass: SharedServiceMock},
        {provide: LocationsService, useClass: LocationsServiceMock},
        {provide: Router, useValue: MockRouter }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationsComponent);
    component = fixture.componentInstance;
    /*
    sharedService = TestBed.get(SharedService);
    var locationSource = new BehaviorSubject<Location[]>([]);
    sharedService.locations = locationSource.asObservable();
    var eventsSource = new BehaviorSubject<Event[]>([]);
    sharedService.events = this.eventsSource.asObservable();
    */
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class AlertServiceMock{

}

class SharedServiceMock{

  locationSource = new BehaviorSubject<Location[]>([]);
  locations = this.locationSource.asObservable();

  updateAll() {
   
  }
}

class LocationsServiceMock{

}

class MockRouter {

}