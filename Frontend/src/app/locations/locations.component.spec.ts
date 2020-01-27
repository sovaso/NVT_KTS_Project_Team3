import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationsComponent } from './locations.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppRoutingModule } from '../app-routing.module';
import { AlertService } from '../services';
import { SharedService } from '../services/shared/shared.service';
import { LocationsService } from '../services/locations/locations.service';
import { NgAnalyzeModulesHost } from '@angular/compiler';
import { NgbModal, NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap' ;
import { BehaviorSubject,Observable, of } from 'rxjs';
import { tick, fakeAsync } from '@angular/core/testing';
import { EventsService } from '../services/events/events.service';
import { NgModule, DebugElement } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
import { LocationDetailsComponent } from './location-details/location-details.component';
import { LocationReportComponent } from './location-report/location-report.component';
import { LocationReportDto } from '../dto/location_report.dto';
import { LocationCreateComponent } from './location-create/location-create.component';

describe('LocationsComponent', () => {
  let component: LocationsComponent;
  let fixture: ComponentFixture<LocationsComponent>;

  let modalService: NgbModal;
  let modalRef: NgbModalRef;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationsComponent,AlertBoxComponent,LocationDetailsComponent,LocationCreateComponent,LocationReportComponent ],
      imports: [NgbModule,FormsModule],
      providers: [
        NgbModal,
        {provide: AlertService, useClass: AlertServiceMock},
        {provide: SharedService, useClass: SharedServiceMock},
        {provide: LocationsService, useClass: LocationsServiceMock}
      ]
    })
    .compileComponents();
    TestBed.overrideModule(BrowserDynamicTestingModule, {
      set: {
        entryComponents: [AlertBoxComponent, LocationReportComponent,LocationCreateComponent,LocationDetailsComponent]
      }
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

  it('ng on init', fakeAsync(() => {
    component.ngOnInit();
    fixture.detectChanges(); // tell angular that data are fetched
    expect(component.locations.length).toEqual(3);
    let locationRows: DebugElement[] = fixture.debugElement.queryAll(By.css('table tr'));
    tick();
    fixture.detectChanges();
    expect(locationRows.length).toBe(3); 
   
  }));

  it('delete location', fakeAsync(() => {
    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(AlertBoxComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let l1 = new Location("1");
    l1.name = "Location 1";
    l1.address = "Address 1";
    l1.description="Description 1";
    l1.status=true;
    l1.events=new Set<Event>();
    l1.locationZones=new Set<LocationZone>();
    component.deleteLocation(l1);
    fixture.detectChanges();
    expect(component.modalRef.componentInstance.message).toEqual("header deleted");
    expect(modalRef.componentInstance.message).toEqual("header deleted");
  }));

  it('see reports', fakeAsync(() => {

    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(LocationReportComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);

    let l1 = new Location("1");
    l1.name = "Location 1";
    l1.address = "Address 1";
    l1.description="Description 1";
    l1.status=true;
    l1.events=new Set<Event>();
    l1.locationZones=new Set<LocationZone>();

    component.seeReport(l1);
    fixture.autoDetectChanges(true);

    let locationReportDto = new LocationReportDto();
    locationReportDto.dailyLabels = new Array<string>();
    locationReportDto.dailyValues = new Array<number>();
    locationReportDto.weeklyLabels = new Array<string>();
    locationReportDto.weeklyValues = new Array<number>();
    locationReportDto.monthlyLabels = new Array<string>();
    locationReportDto.monthlyValues = new Array<number>();

    expect(component.modalRef.componentInstance.data).toEqual(locationReportDto);

  }));


  it('new location', fakeAsync(() => {
    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(LocationCreateComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);
    component.newLocation();
    fixture.autoDetectChanges(true);
    expect(component.modalRef.componentInstance.name).toEqual('');
    expect(component.modalRef.componentInstance.address).toEqual('');
    expect(component.modalRef.componentInstance.description).toEqual('');
    expect(component.modalRef.componentInstance.status).toEqual(false);
    expect(component.modalRef.componentInstance.locationZones).toEqual([]);
    expect(component.modalRef.componentInstance.newZone).toEqual(false);
    expect(component.modalRef.componentInstance.lzname).toEqual('');
    expect(component.modalRef.componentInstance.rowNumber).toEqual(0);
    expect(component.modalRef.componentInstance.colNumber).toEqual(0);
    expect(component.modalRef.componentInstance.matrix).toEqual(false);
    expect(component.modalRef.componentInstance.capacity).toEqual(0);
    //expect(component.modalRef.componentInstance.location).toEqual(null);
    expect(component.modalRef.componentInstance.numOfLocationZones).toEqual(0);
    expect(component.modalRef.componentInstance.isModelShow).toEqual(false);
    expect(component.modalRef.componentInstance.done).toEqual(0);
  }));

  it('update location', fakeAsync(() => {

    modalService = TestBed.get(NgbModal);
    modalRef = modalService.open(AlertBoxComponent);
    spyOn(modalService, "open").and.returnValue(modalRef);
    let l1 = new Location("1");
    l1.name = "Location 1";
    l1.address = "Address 1";
    l1.description="Description 1";
    l1.status=true;
    l1.events=new Set<Event>();
    l1.locationZones=new Set<LocationZone>();

    component.updateLocation(l1);
    fixture.autoDetectChanges(true);
    expect(component.modalRef.componentInstance.location).toEqual(l1);

  }));


});



class AlertServiceMock{

}

class SharedServiceMock{

  locationSource = new BehaviorSubject<Location[]>([]);
  locations = this.locationSource.asObservable();

  updateAll() {
    this.updateLocation();
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
}



class LocationsServiceMock{
  http: HttpClient;
  delete = (id: string): Observable<MessageDto> =>{
  
    let messageDto : MessageDto = {
      header: "header deleted",
      message: "message deleted"
    };
    
    return of(messageDto);
  }

  seeReport(id: string): Observable<LocationReportDto> {
    let locationReportDto = new LocationReportDto();
    locationReportDto.dailyLabels = new Array<string>();
    locationReportDto.dailyValues = new Array<number>();
    locationReportDto.weeklyLabels = new Array<string>();
    locationReportDto.weeklyValues = new Array<number>();
    locationReportDto.monthlyLabels = new Array<string>();
    locationReportDto.monthlyValues = new Array<number>();

    return of(locationReportDto);

  }

  




}

