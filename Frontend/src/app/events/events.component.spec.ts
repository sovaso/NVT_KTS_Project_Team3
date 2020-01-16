import { async, ComponentFixture, TestBed, tick, fakeAsync } from '@angular/core/testing';

import { EventsComponent } from './events.component';

import { AlertService } from '../services';
import { SharedService } from '../services/shared/shared.service';
import { EventsService } from '../services/events/events.service';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { NgbModule, NgbModal, NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap' ;
import { NgModule, DebugElement } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from '../app-routing.module';
import { Event } from '../model/event.model';
import { Location } from '../model/location.model';
import { Reservation } from '../model/reservation.model';
import { Maintenance } from '../model/maintenance.model';
import { LocationZone } from '../model/location_zone.model';
import { By } from '@angular/platform-browser';
import { log } from 'util';
import { share } from 'rxjs/operators';
import { MessageDto } from '../dto/message.dto';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { AlertBoxComponent } from '../alert-box/alert-box.component';
import { Alert } from '../model';
import { BrowserDynamicTestingModule } from '@angular/platform-browser-dynamic/testing';
import { EventDetailsComponent } from './event-details/event-details.component';
import { EventReportComponent } from './event-report/event-report.component';
import { EventReportDto } from '../dto/event_report.dto';

describe('EventsComponent', () => {
  let component: EventsComponent;
  let fixture: ComponentFixture<EventsComponent>;
  
  
  let modalService: NgbModal;
  let modalRef: NgbModalRef;

  


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventsComponent, AlertBoxComponent, EventDetailsComponent, EventReportComponent ],
      imports: [NgbModule, FormsModule],
      providers: [
        NgbActiveModal,
        NgbModal,
        {provide: SharedService, useClass: SharedServiceMock},
        {provide: EventsService, useClass: EventsServiceMock}
      ]
    })
    .compileComponents();

    TestBed.overrideModule(BrowserDynamicTestingModule, {
      set: {
        entryComponents: [AlertBoxComponent, EventDetailsComponent, EventReportComponent]
      }
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventsComponent);
    component = fixture.componentInstance;
    component.field = "";
    //this.sharedService = TestBed.get(SharedService);
  
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('ng on init', fakeAsync(() => {
      component.ngOnInit();
      fixture.detectChanges(); // tell angular that data are fetched
      expect(component.events.length).toEqual(3);
      let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
      tick();
      fixture.detectChanges();
      expect(eventRows.length).toBe(3); 
     
  }));

  it('delete event', fakeAsync(() => {
    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(AlertBoxComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let e1 = new Event("1");
    e1.name = "Event 1";
    e1.status = true;
    e1.type = "Sports";
    e1.locationInfo = null;
    e1.reservations = new Set<Reservation>();
    e1.maintenances = new Set<Maintenance>();

    component.deleteEvent(e1);
    fixture.detectChanges();
    expect(component.modalRef.componentInstance.message).toEqual("Header deleted");
   
    expect(modalRef.componentInstance.message).toEqual("Header deleted");
  }));

  
  it('show details', fakeAsync(() => {

    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(AlertBoxComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let e1 = new Event("1");
    e1.name = "Event 1";
    e1.status = true;
    e1.type = "Sports";
    e1.locationInfo = null;
    e1.reservations = new Set<Reservation>();
    e1.maintenances = new Set<Maintenance>();

    component.showDetails(e1);
    fixture.autoDetectChanges(true);
    expect(component.modalRef.componentInstance.event).toEqual(e1);

  }));

  it('see reports', fakeAsync(() => {

    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(EventReportComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let e1 = new Event("1");
    e1.name = "Event 1";
    e1.status = true;
    e1.type = "Sports";
    e1.locationInfo = null;
    e1.reservations = new Set<Reservation>();
    e1.maintenances = new Set<Maintenance>();

    component.seeReport(e1);
    fixture.autoDetectChanges(true);

    let eventReportDto = new EventReportDto();
    eventReportDto.dailyLabels = new Array<string>();
    eventReportDto.dailyValues = new Array<number>();
    eventReportDto.weeklyLabels = new Array<string>();
    eventReportDto.weeklyValues = new Array<number>();
    eventReportDto.monthlyLabels = new Array<string>();
    eventReportDto.monthlyValues = new Array<number>();

    expect(component.modalRef.componentInstance.data).toEqual(eventReportDto);

  }));

  it('sort by date acs', fakeAsync(() => {
    component.sortByDateAcs();
    fixture.detectChanges(); // tell angular that data are fetched
    expect(component.events.length).toEqual(3);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(3); 
  }));

  it('sort by date desc', fakeAsync(() => {
    component.sortByDateDesc();
    fixture.detectChanges(); // tell angular that data are fetched
    expect(component.events.length).toEqual(3);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(3); 
  }));

  it('sort by name', fakeAsync(() => {
    component.sortByName();
    fixture.detectChanges(); // tell angular that data are fetched
    expect(component.events.length).toEqual(3);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(3); 
  }));

  it('reset', fakeAsync(() => {
    component.reset();
    fixture.detectChanges(); // tell angular that data are fetched
    expect(component.events.length).toEqual(3);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(3); 
  }));

  it('getIncome', fakeAsync(() => {
    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(AlertBoxComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let e1 = new Event("1");
    e1.name = "Event 1";
    e1.status = true;
    e1.type = "Sports";
    e1.locationInfo = null;
    e1.reservations = new Set<Reservation>();
    e1.maintenances = new Set<Maintenance>();

    component.getIncome(e1);
    fixture.detectChanges(); // tell angular that data are fetched

    expect(component.modalRef.componentInstance.message).toEqual('Income for Event 1 is: 1000');
    
  }));

  it('search', fakeAsync(() => {
    component.startDate='';
    component.field='';
    component.endDate='';
    component.search();
    fixture.detectChanges(); // tell angular that data are fetched

    expect(component.events.length).toEqual(3);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(3); 
  }));

  it('search case 2', fakeAsync(() => {
    component.startDate='2019-01-01';
    component.field='sports';
    component.endDate='2019-12-12';
    component.search();
    fixture.detectChanges(); // tell angular that data are fetched

    expect(component.events.length).toEqual(2);
    let eventRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(eventRows.length).toBe(2); 
  }));
  
});



class AlertServiceMock{

}

class SharedServiceMock{

  locationSource = new BehaviorSubject<Location[]>([]);
  locations = this.locationSource.asObservable();
  eventsSource = new BehaviorSubject<Event[]>([]);
  events = this.eventsSource.asObservable();

  updateAll() {
    this.updateEvents();
    this.updateLocation();
  }

  searchEvents(field : string, startDate : string, endDate : string) {
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
    this.eventsSource.next(events);
    }

  updateEvents() {

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

    this.eventsSource.next(events);

    
  }

  updateLocation() {
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

    this.locationSource.next(locations);
  }

  sortEventsByDateAcs(){
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

    this.eventsSource.next(events);

  }

  sortEventsByDateDesc(){
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

    this.eventsSource.next(events);

  }

  sortEventsByName(){
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

    this.eventsSource.next(events);

  }
  
}

class EventsServiceMock{

  http: HttpClient;
  delete = (id: string): Observable<MessageDto> =>{
  
    let messageDto : MessageDto = {
      header: "Header deleted",
      message: "Message deleted"
    };
    
    return of(messageDto);
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

    return of(events);
  }
    

  getById(id: String): Observable<Event> {
    let e1 = new Event("1");
    e1.name = "Event 1";
    e1.status = true;
    e1.type = "Sports";
    e1.locationInfo = null;
    e1.reservations = new Set<Reservation>();
    e1.maintenances = new Set<Maintenance>();

    return of(e1);
  }

  getIncome(id:string): Observable<any> {
    return of(1000);
  }

  seeReport(id: string): Observable<EventReportDto> {
    let eventReportDto = new EventReportDto();
    eventReportDto.dailyLabels = new Array<string>();
    eventReportDto.dailyValues = new Array<number>();
    eventReportDto.weeklyLabels = new Array<string>();
    eventReportDto.weeklyValues = new Array<number>();
    eventReportDto.monthlyLabels = new Array<string>();
    eventReportDto.monthlyValues = new Array<number>();

    return of(eventReportDto);

  }
  
}
