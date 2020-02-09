
/*
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventDetailsComponent } from './event-details.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReservationsService } from 'src/app/services/reservations/reservations.service';
import { MaintenancesService } from 'src/app/services/maintenances/maintenances.service';
import { Observable, of } from 'rxjs';
import { Reservation } from 'src/app/model/reservation.model';
import { Maintenance } from 'src/app/model/maintenance.model';
import { Event } from 'src/app/model/event.model';

describe('EventDetailsComponent', () => {
  let component: EventDetailsComponent;
  let fixture: ComponentFixture<EventDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventDetailsComponent ],
      imports: [NgbModule],
      providers: [
        {provide: ReservationsService, useClass: ReservationsServiceMock},
        {provide: MaintenancesService, useClass: MaintenancesServiceMock},
        
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventDetailsComponent);
    component = fixture.componentInstance;
    component.event = new Event("1");

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class ReservationsServiceMock {

  getByEventId(eventId: String): Observable<Reservation[]> {
    return of([]);
  }
}

class MaintenancesServiceMock {

  getByEventId(eventId: String): Observable<Maintenance[]> {
    return of([]);
  }
}

*/
