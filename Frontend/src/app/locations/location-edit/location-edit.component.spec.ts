import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationEditComponent } from './location-edit.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LocationsService } from 'src/app/services/locations/locations.service';
import { AlertService } from 'src/app/services';
import { EventsService } from 'src/app/services/events/events.service';
import { LocationZonesService } from 'src/app/services/location-zones/location-zones.service';
import { of, Observable } from 'rxjs';
import { LocationZone } from 'src/app/model/location_zone.model';
import { Location } from 'src/app/model/location.model';
import { FormsModule } from '@angular/forms';

describe('LocationEditComponent', () => {
  let component: LocationEditComponent;
  let fixture: ComponentFixture<LocationEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationEditComponent ],
      imports: [NgbModule, FormsModule ],
      providers: [
        {provide: LocationsService, useClass: LocationServiceMock},
        {provide: AlertService, useClass: AlertServiceMock},
        {provide: EventsService, useClass: EventsServiceMock},
        {provide: LocationZonesService, useClass: LocationZonesServiceMock}
        
      ]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationEditComponent);
    component = fixture.componentInstance;
    component.location = new Location("1");
    component.location.name ="Name of location";
    component.location.status = true;
    component.location.description = "some description";
    component.location.address = "some address";
    //component.location.events = new Set<Event>();
    component.location.locationZones = new Set<LocationZone>();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class LocationServiceMock {

}

class AlertServiceMock{

}

class EventsServiceMock{
  
}

class LocationZonesServiceMock{
  getLocationZones(id: String): Observable<LocationZone[]> {
    return of([]);
  }
}