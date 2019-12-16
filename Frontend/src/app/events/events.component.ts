import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../model/currentUser';
import { SharedService } from '../services/shared/shared.service';
import { EventsService } from '../services/events/events.service';
import { AlertService } from '../services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from '../alert-box/alert-box.component';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[];
  locations: Location[];

  activeTab: String;

  field: string='';

  loggedUser: CurrentUser;

  startDate: string='';

  endDate: string='';

  message: String = '';

  type = '';

  constructor(private modalService: NgbModal, private alertService: AlertService, private sharedService: SharedService, private eventsService: EventsService) {}

  ngOnInit() {
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));

    this.sharedService.events.subscribe(
      events => (this.events = events)
    );

    this.sharedService.locations.subscribe(locations => (this.locations = locations));
   
    if (this.events.length === 0 || this.locations.length===0) {
      this.sharedService.updateAll();
    }
  }
  onTabChange($event: any) {
    this.activeTab = $event.nextId;
  }

  deleteEvent(event){
    console.log('delete event called');
    console.log(event.id);
    this.eventsService.delete(event.id).subscribe(data => {
        console.log(data);
        this.sharedService.updateAll();
      
    }
    );

    const modalRef = this.modalService.open(AlertBoxComponent);
    modalRef.componentInstance.message="Event is successfully deleted.";
    
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
      console.log('SEARCH CLICKED');
      console.log(this.field);
      console.log(this.startDate);
      console.log(this.endDate);
      /*
      this.sharedService.searchEvents(this.field, this.startDate, this.endDate);
      console.log('STATUUUUUS');
      console.log(this.sharedService.status);
      */
  
     this.eventsService.search(this.field, this.startDate,this.endDate).subscribe(data => {
       console.log('nadjeni eventovi');
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

    
    

  
}

