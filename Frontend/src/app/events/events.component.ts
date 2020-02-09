import { Component, OnInit,AfterViewChecked, ViewChild, ElementRef } from '@angular/core';
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
import { Router } from '@angular/router';


declare var payPal: any;

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit{
  @ViewChild('payPal',{static:true}) paypalElement: ElementRef;
  
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

  allActiveEvents: Event[]=[];

  addScript: boolean = false;
  paypalLoad: boolean = true;
  
  finalAmount: number = 1;


 
  

  constructor(private modalService: NgbModal, public sharedService: SharedService, private eventsService: EventsService,
    private router: Router) {}

  ngOnInit() {
    
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));


    this.sharedService.events.subscribe(
      events => (this.events = events)
    );

    

    this.sharedService.locations.subscribe(locations => (this.locations = locations));
   
    
    if (this.events.length === 0 || this.locations.length===0) {
      this.sharedService.updateAll();
    }

    this.eventsService.getActive().subscribe(
      events => (this.allActiveEvents = events)
    );

    


    //this.modalRef = this.modalService.open(AlertBoxComponent);


  }
  onTabChange($event: any) {
    this.activeTab = $event.nextId;
  }

  // paypalConfig = {
  //   env: 'sandbox',
  //   client: {
  //     sandbox: 'AQhPuCAwEEOLPE1EGRJPKQIZxv4-xsSx0AvcM7tLX1x1chXe3jXPXCZJ7F_gIK09W_DK3Sy3iNdRj3f6',
  //   },
  //   commit: true,
  //   payment: (data, actions) => {
  //     return actions.payment.create({
  //       payment: {
  //         transactions: [
  //           { amount: { total: this.finalAmount, currency: 'USD' } }
  //         ]
  //       }
  //     });
  //   },
  //   onAuthorize: (data, actions) => {
  //     return actions.payment.execute().then((payment) => {
  //       //Do something when payment is successful.
  //     })
  //   }
  // };
 
  // ngAfterViewChecked(): void {
  //   if (!this.addScript) {
  //     this.addPayPalScript().then(() => {
  //       payPal.Button.render(this.paypalConfig, '#paypal-checkout-btn');
  //       this.paypalLoad = false;
  //     });
  //   }
  // }
  
  // addPayPalScript(){
  //   this.addScript=true;
  //   return new Promise((resolve,reject)=>{
  //     let scriptTagElement=document.createElement('script');
  //     scriptTagElement.src='https://www.paypalobjects.com/api/checkout.js';
  //     scriptTagElement.onload=resolve;
  //     document.body.appendChild(scriptTagElement);
  //   });
  // };


  deleteEvent(event){
    this.eventsService.delete(event.id).subscribe(data => {
        this.sharedService.updateAll();
        this.modalRef = this.modalService.open(AlertBoxComponent);
        this.modalRef.componentInstance.message=data.header;
    });
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

    newEvent(){
      this.router.navigate(['dashboard/create-event']);
    }
    
    updateEvent(event){
      localStorage.setItem("eventForUpdate", event.id);
      this.router.navigate(['dashboard/event-update']);
    }

    reserve(event){
      localStorage.setItem("reservation",event.id);
      console.log("EVENT ID: ",event.id);
      this.router.navigate(['dashboard/reservation']);
    }
  
}

