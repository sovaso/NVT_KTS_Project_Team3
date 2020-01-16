import { TestBed, getTestBed } from "@angular/core/testing";
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { EventsService } from '../events/events.service';
import { SharedService } from './shared.service';
import { LocationsService } from '../locations/locations.service';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { Location } from 'src/app/model/location.model';
import { Event } from 'src/app/model/event.model';
import { Reservation } from 'src/app/model/reservation.model';
import { Maintenance } from 'src/app/model/maintenance.model';
import { LocationZone } from 'src/app/model/location_zone.model';



describe('EventsService', () => {
    let injector: TestBed;
    let service: SharedService;
    let httpMock: HttpTestingController;
    let eventsBeh = new BehaviorSubject<Event[]>([]);
    let events : Event[] = [];

    let locationsBeh = new BehaviorSubject<Location[]>([]);
    let locations : Location[] = [];

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [
            SharedService,
            {provide: EventsService, useClass: EventsServiceMock },
            {provide: LocationsService, useClass: LocationsServiceMock }
        ]
      });
  
      injector = getTestBed();
      service = injector.get(SharedService);
      httpMock = injector.get(HttpTestingController);


      let e1 = new Event("1");
      e1.name = "Event 1";
      e1.status = true;
      e1.type = "Sports";
      e1.locationInfo = null;
      e1.reservations = new Set<Reservation>();
      e1.maintenances = new Set<Maintenance>();
      events.push(e1);

      let e2 = new Event("2");
      e2.name = "Event 2";
      e2.status = true;
      e2.type = "Cultural";
      e2.locationInfo = null;
      e2.reservations = new Set<Reservation>();
      e2.maintenances = new Set<Maintenance>();
      events.push(e2);

      let e3 = new Event("3");
      e3.name = "Event 3";
      e3.status = true;
      e3.type = "Sports";
      e3.locationInfo = null;
      e3.reservations = new Set<Reservation>();
      e3.maintenances = new Set<Maintenance>();
      events.push(e3);

      eventsBeh.next(events);

      let l1 = new Location("1");
      l1.name = "Location1";
      l1.address = "Address1";
      l1.description = "Description1";
      l1.status = true;
      l1.events = new Set<Event>();
      l1.locationZones = new Set<LocationZone>();
      locations.push(l1);

      let l2 = new Location("2");
      l2.name = "Location2";
      l2.address = "Address2";
      l2.description = "Description2";
      l2.status = true;
      l2.events = new Set<Event>();
      l2.locationZones = new Set<LocationZone>();
      locations.push(l2);

      let l3 = new Location("3");
      l3.name = "Location3";
      l3.address = "Address3";
      l3.description = "Description3";
      l3.status = true;
      l3.events = new Set<Event>();
      l3.locationZones = new Set<LocationZone>();
      locations.push(l3);

      locationsBeh.next(locations);


    });

    describe('#updateEvents', () => {
        it('events should be updated>', () => {
            service.updateEvents();
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });

    describe('#updateLocation', () => {
        it('locations should be updated>', () => {
            service.updateLocation();
            expect(service.locations.forEach).toEqual(of(locationsBeh.asObservable()).forEach);
        })
        });
    
    describe('#updateAll', () => {
        it('locations and events should be updated', () => {
            service.updateAll();
            expect(service.locations.forEach).toEqual(of(locationsBeh.asObservable()).forEach);
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });
    
    describe('#sortEventsByDateAcs', () => {
        it('events should be sorted', () => {
            service.sortEventsByDateAcs();
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });

    describe('#sortEventsByDateDesc', () => {
        it('events should be sorted', () => {
            service.sortEventsByDateDesc();
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });

    describe('#sortEventsByName', () => {
        it('events should be sorted', () => {
            service.sortEventsByName();
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });

    describe('#searchEvents', () => {
        it('events should be sorted', () => {
            service.searchEvents("sports", "2020-01-01", "2020-12-12");
            expect(service.events.forEach).toEqual(of(eventsBeh.asObservable()).forEach);
        })
        });


});

class EventsServiceMock{

    getAll(): Observable<Event[]> {

        let events : Event[] = [];
        let e1 = new Event("1");
        e1.name = "Event 1";
        e1.status = true;
        e1.type = "Sports";
        e1.locationInfo = null;
        e1.reservations = new Set<Reservation>();
        e1.maintenances = new Set<Maintenance>();
        events.push(e1);

        let e2 = new Event("2");
        e2.name = "Event 2";
        e2.status = true;
        e2.type = "Cultural";
        e2.locationInfo = null;
        e2.reservations = new Set<Reservation>();
        e2.maintenances = new Set<Maintenance>();
        events.push(e2);

        let e3 = new Event("3");
        e3.name = "Event 3";
        e3.status = true;
        e3.type = "Sports";
        e3.locationInfo = null;
        e3.reservations = new Set<Reservation>();
        e3.maintenances = new Set<Maintenance>();
        events.push(e3);
    
        return of(events);
      }

      sortByName(): Observable<any> {
        let events : Event[] = [];
        let e1 = new Event("1");
        e1.name = "Event 1";
        e1.status = true;
        e1.type = "Sports";
        e1.locationInfo = null;
        e1.reservations = new Set<Reservation>();
        e1.maintenances = new Set<Maintenance>();
        events.push(e1);

        let e2 = new Event("2");
        e2.name = "Event 2";
        e2.status = true;
        e2.type = "Cultural";
        e2.locationInfo = null;
        e2.reservations = new Set<Reservation>();
        e2.maintenances = new Set<Maintenance>();
        events.push(e2);

        let e3 = new Event("3");
        e3.name = "Event 3";
        e3.status = true;
        e3.type = "Sports";
        e3.locationInfo = null;
        e3.reservations = new Set<Reservation>();
        e3.maintenances = new Set<Maintenance>();
        events.push(e3);
    
        return of(events);
      }
      sortByDateAcs(): Observable<any>{
        let events : Event[] = [];
        let e1 = new Event("1");
        e1.name = "Event 1";
        e1.status = true;
        e1.type = "Sports";
        e1.locationInfo = null;
        e1.reservations = new Set<Reservation>();
        e1.maintenances = new Set<Maintenance>();
        events.push(e1);

        let e2 = new Event("2");
        e2.name = "Event 2";
        e2.status = true;
        e2.type = "Cultural";
        e2.locationInfo = null;
        e2.reservations = new Set<Reservation>();
        e2.maintenances = new Set<Maintenance>();
        events.push(e2);

        let e3 = new Event("3");
        e3.name = "Event 3";
        e3.status = true;
        e3.type = "Sports";
        e3.locationInfo = null;
        e3.reservations = new Set<Reservation>();
        e3.maintenances = new Set<Maintenance>();
        events.push(e3);
    
        return of(events);
      }

      sortByDateDesc(): Observable<any> {
        let events : Event[] = [];
        let e1 = new Event("1");
        e1.name = "Event 1";
        e1.status = true;
        e1.type = "Sports";
        e1.locationInfo = null;
        e1.reservations = new Set<Reservation>();
        e1.maintenances = new Set<Maintenance>();
        events.push(e1);

        let e2 = new Event("2");
        e2.name = "Event 2";
        e2.status = true;
        e2.type = "Cultural";
        e2.locationInfo = null;
        e2.reservations = new Set<Reservation>();
        e2.maintenances = new Set<Maintenance>();
        events.push(e2);

        let e3 = new Event("3");
        e3.name = "Event 3";
        e3.status = true;
        e3.type = "Sports";
        e3.locationInfo = null;
        e3.reservations = new Set<Reservation>();
        e3.maintenances = new Set<Maintenance>();
        events.push(e3);
    
        return of(events);
      }


      search (field: string, startDate: string, endDate: string): Observable<any> {
        let events : Event[] = [];
        let e1 = new Event("1");
        e1.name = "Event 1";
        e1.status = true;
        e1.type = "Sports";
        e1.locationInfo = null;
        e1.reservations = new Set<Reservation>();
        e1.maintenances = new Set<Maintenance>();
        events.push(e1);

        let e2 = new Event("2");
        e2.name = "Event 2";
        e2.status = true;
        e2.type = "Cultural";
        e2.locationInfo = null;
        e2.reservations = new Set<Reservation>();
        e2.maintenances = new Set<Maintenance>();
        events.push(e2);

        let e3 = new Event("3");
        e3.name = "Event 3";
        e3.status = true;
        e3.type = "Sports";
        e3.locationInfo = null;
        e3.reservations = new Set<Reservation>();
        e3.maintenances = new Set<Maintenance>();
        events.push(e3);
    
        return of(events);
      }
}

class LocationsServiceMock{

    getAll(): Observable<Location[]> {
        let locations : Location[] = [];
        let l1 = new Location("1");
        l1.name = "Location1";
        l1.address = "Address1";
        l1.description = "Description1";
        l1.status = true;
        l1.events = new Set<Event>();
        l1.locationZones = new Set<LocationZone>();
        locations.push(l1);

        let l2 = new Location("2");
        l2.name = "Location2";
        l2.address = "Address2";
        l2.description = "Description2";
        l2.status = true;
        l2.events = new Set<Event>();
        l2.locationZones = new Set<LocationZone>();
        locations.push(l2);

        let l3 = new Location("3");
        l3.name = "Location3";
        l3.address = "Address3";
        l3.description = "Description3";
        l3.status = true;
        l3.events = new Set<Event>();
        l3.locationZones = new Set<LocationZone>();
        locations.push(l3);

        return of(locations);
    }
}