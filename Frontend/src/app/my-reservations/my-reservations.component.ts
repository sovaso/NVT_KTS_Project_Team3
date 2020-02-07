import { Component, OnInit } from '@angular/core';
import { Reservation } from '../model/reservation.model';
import {ReservationsService} from '../services/reservations/reservations.service';
import { CurrentUser } from '../model/currentUser';
import { Ticket } from '../model/ticket.model';
import { DatePipe, formatDate } from '@angular/common';
import {TicketsService} from '../services/tickets/tickets.service';
import { NgbModal,NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {AlertBoxComponent} from '../alert-box/alert-box.component';
import { Router } from '@angular/router';
@Component({
  selector: 'app-my-reservations',
  templateUrl: './my-reservations.component.html',
  styleUrls: ['./my-reservations.component.css']
})
export class MyReservationsComponent implements OnInit {

  myReservations: Reservation[]=[];
  myTickets: Ticket[]=[];
  loggedUser: CurrentUser;
  dateOfRes: string;

  constructor(private reservationsService: ReservationsService,public datepipe: DatePipe,private ticketsService: TicketsService,private modalService: NgbModal,private router:Router) { }

  ngOnInit() {
    var model=document.getElementById("myModal");
    model.style.display="none";
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    this.reservationsService.getAllReservations().subscribe(data=>{
      this.myReservations=data;
      });
  }

  formatDate(date:Date){
    var d=this.datepipe.transform(date, 'yyyy-MM-dd HH:mm');
    return d;
  }

  showTickets(reservation){
    let self=this;
    let modal=document.getElementById("modall");
    modal.innerHTML="";
    this.ticketsService.getReservationTickets(reservation.id).subscribe(data=>{
        self.myTickets=data;
        self.myTickets.forEach(function(ticket){
            console.log("Tickets",self.myTickets);
            var model=document.getElementById("myModal");
            
            

            model.style.display="block";
            var div_left=document.createElement("div");
            div_left.style.width="20%";
            div_left.style.display="inline-block";
            div_left.className="displayTicket";
            div_left.style.marginLeft="0%";
          
            var img=document.createElement('img');
            img.src="http://localhost:8080/api/getQRCodeImage/"+ticket.id;
            img.style.height="150px";
            



            div_left.append(img);

            var div_right=document.createElement("div");
            div_right.style.width="60%";
            div_right.className="displayTicket";
            div_right.style.marginLeft="20%";
            div_right.style.display="inline-block";

            var card_div=document.createElement("div");
            card_div.style.height="200px";
            card_div.style.padding="30px";
            card_div.style.width="100%";
            card_div.style.border="1px solid black";
          


            var table=document.createElement("table");

            var thead=document.createElement("thead");
            var th1=document.createElement("th");
            th1.style.width="15%";
            th1.innerText="Zone";
            thead.append(th1);

            var th2=document.createElement("th");
            th2.style.width="15%";
            th2.innerText="Price";
            thead.append(th2);

            var th3=document.createElement("th");
            th3.style.width="15%";
            th3.innerText="Event date";
            thead.append(th3);


            var th4=document.createElement("th");
            th4.style.width="15%";
            th4.innerText="Seat";
            thead.append(th4);

            var tr=document.createElement('tr');

            table.append(thead);
            var tbody=document.createElement("tbody");
            var tr1=document.createElement("td");
            tr1.style.width="15%";

            tr1.innerText=ticket.zone.zone.name;
            tr.append(tr1);

            var tr2=document.createElement("td");
            tr2.style.width="15%";
            tr2.innerText=ticket.price+'';
            tr.append(tr2);

            var tr3=document.createElement("td");
            tr3.style.width="15%";
            tr3.innerText=self.formatDate(ticket.zone.maintenance.maintenanceDate);
            tr.append(tr3);
            

            if(ticket.col!=0 && ticket.row!=0){
              var tr4=document.createElement("td");
              tr4.style.width="15%";
              tr4.innerText=ticket.col+"/"+ticket.row;
              tr.append(tr4);
            }else{
              var tr4=document.createElement("td");
              tr4.style.width="15%";
              tr4.innerText="";
              tr.append(tr4);

            }
            tbody.append(tr);
            table.append(tbody);
            div_right.append(table);
            card_div.append(div_left);
            card_div.append(div_right);
            modal.append(card_div);


        

      });

    });

    

  }

  closeModal(){
    var model=document.getElementById("myModal");
    model.style.display="none";
  }

  cancel(reservation){
    let self=this;
    this.reservationsService.cancelReservation(reservation.id).subscribe(data=>{
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message=data.message+"\n\n"+data.header;
      window.location.reload();
    })
  }

}
