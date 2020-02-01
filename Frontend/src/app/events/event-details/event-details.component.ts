import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/model/event.model';
import { Reservation } from 'src/app/model/reservation.model';
import { ReservationsService } from 'src/app/services/reservations/reservations.service';
import { MaintenancesService } from 'src/app/services/maintenances/maintenances.service';
import { Maintenance } from 'src/app/model/maintenance.model';
import { MaintRepr } from 'src/app/model/maintRepr.model';
import { CurrentUser } from 'src/app/model/currentUser';
import { NgbModal,NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {
  loggedUser: CurrentUser;
  event : Event;
  reservedTickets: number=0;
  paidTickets: number = 0;
  reservations: Reservation[]=[];
  maintenances: Maintenance[]=[];
/*   maintenancesStart: String[]=[];
  maintenancesEnd: String[]=[];
  maintenancesString : String[] = [];
  expiries: String[]; */
  maintRepr : MaintRepr[]=[];
  constructor(private modalService: NgbModal,private reservationsService: ReservationsService, private maintenancesService: MaintenancesService) { }

  ngOnInit() {
    /*
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    this.reservationsService.getByEventId(this.event.id).subscribe(data => {
      
      this.reservations=data;

      for (let res in this.reservations){
        console.log('uslo u for loop');
        let reservation : Reservation;
        reservation = this.reservations[res];
        if (reservation.paid==true){
          this.paidTickets+=1;
        }else{
          this.reservedTickets+=1;
        }
  
      }
    });

    this.maintenancesService.getByEventId(this.event.id).subscribe(data=>{
     
      this.maintenances=data;
      for (let m in this.maintenances){
        console.log('uslo u main loop');
        let maintenance : Maintenance;
        maintenance=this.maintenances[m];


        let repr  = new MaintRepr();
        repr.from = maintenance.maintenanceDate.toString().split("T")[0]+ " " + maintenance.maintenanceDate.toString().split("T")[1]
        repr.to =  maintenance.maintenanceEndTime.toString().split("T")[0]+ " " + maintenance.maintenanceEndTime.toString().split("T")[1]
        repr.expiry = maintenance.reservationExpiry.toString().split("T")[0]+ " "+
        maintenance.reservationExpiry.toString().split("T")[1];

        this.maintRepr.push(repr);
        
    
      }
      
    });
    
    */
    
   // this.soldTickets=this.reservations.size
    /*
    for (let res in this.event.reservations){
      this.soldTickets+=1;
    }*/
  }

  close(){
    this.modalService.dismissAll();
  }

}
