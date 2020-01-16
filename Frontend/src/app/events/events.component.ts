import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../model/currentUser';
import { SharedService } from '../services/shared/shared.service';
import { EventsService } from '../services/events/events.service';

import { NgbModal,NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from '../alert-box/alert-box.component';
import { EventDetailsComponent } from './event-details/event-details.component';
import { Event } from '../model/event.model';
import { Location } from '../model/location.model';
import { EventReportComponent } from './event-report/event-report.component';
import { Reservation } from '../model/reservation.model';
import { Maintenance } from '../model/maintenance.model';
import { log } from 'util';


@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[] = [];
  locations: Location[];
  
  activeTab: String;

  field: string='';

  loggedUser: CurrentUser;

  startDate: string='';

  endDate: string='';

  message: String = '';

  type = '';

  modalOption: NgbModalOptions = {};

  modalRef : any;

  constructor(private modalService: NgbModal, public sharedService: SharedService, private eventsService: EventsService) {}

  ngOnInit() {
    
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));

    this.sharedService.events.subscribe(
      events => (this.events = events)
    );

    this.sharedService.locations.subscribe(locations => (this.locations = locations));
   
    if (this.events.length === 0 || this.locations.length===0) {
      this.sharedService.updateAll();
    }
    this.modalRef = this.modalService.open(AlertBoxComponent);


  }
  onTabChange($event: any) {
    this.activeTab = $event.nextId;
  }

  deleteEvent(event){
    this.eventsService.delete(event.id).subscribe(data => {
        this.sharedService.updateAll();
        this.modalRef = this.modalService.open(AlertBoxComponent);
        this.modalRef.componentInstance.message=data.header;
    }
    );
  }

  showDetails(e){
    this.eventsService.getById(e.id).subscribe(data => {
      this.modalRef = this.modalService.open(EventDetailsComponent);
      this.modalRef.componentInstance.event = data;
    });
  }

  sortByDateAcs(){
    console.log('sort by date acs called');
    this.sharedService.sortEventsByDateAcs();
  }

  sortByDateDesc(){
    console.log('sort by date desc called');
    this.sharedService.sortEventsByDateDesc();
  }

  sortByName(){
    console.log('sort by name called');
    this.sharedService.sortEventsByName();
  }

  reset(){
    this.message='';
    console.log('reset called');
    this.sharedService.updateAll();
  }

  getIncome(event){
    this.eventsService.getIncome(event.id).subscribe(data => {
      this.modalRef = this.modalService.open(AlertBoxComponent);
      this.modalRef.componentInstance.message='Income for '+event.name+' is: '+data;
    });
  }
  search(){
    if ((this.field == '' || this.field == "***") && (this.startDate == '' || this.startDate == "***")
     && (this.endDate=='' || this.endDate=="***")){
      this.reset();
    }else {
      if (this.field==''){
        this.field="***";
      }
      if (this.startDate==''){
        this.startDate = "***";
      }
      if (this.endDate==''){
        this.endDate="***";
      }
      /*
      this.sharedService.searchEvents(this.field, this.startDate, this.endDate);
      console.log('STATUUUUUS');
      console.log(this.sharedService.status);
      */
  
     this.eventsService.search(this.field, this.startDate,this.endDate).subscribe(data => {
        this.sharedService.searchEvents(this.field, this.startDate, this.endDate);
        this.field='';
        this.startDate='';
        this.endDate='';
      }, 
      (err : Error)=>{
        this.message="No appropriate events found.";
        this.type = 'danger';
        console.log('error from events component');
       });
      
      document.getElementById("sortByDateAcs").hidden=true;
      document.getElementById("sortByDateDesc").hidden=true;
      document.getElementById("sortByName").hidden=true;
      
    }
    }

    

    seeReport(event){
    this.modalOption.backdrop = 'static';
    this.modalOption.keyboard = false;
      this.eventsService.seeReport(event.id).subscribe(data => {
          this.modalRef = this.modalService.open(EventReportComponent,this.modalOption);
          this.modalRef.componentInstance.data = data;
    });
    }

    
    

  
}

