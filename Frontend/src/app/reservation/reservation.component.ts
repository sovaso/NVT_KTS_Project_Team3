import { Component, OnInit } from '@angular/core';
import { Maintenance} from '../model/maintenance.model';
import { LeasedZoneDto} from '../dto/leased_zone.dto';
import { Ticket} from '../model/ticket.model';
import {EventsService} from '../services/events/events.service'
import {MaintenancesService} from '../services/maintenances/maintenances.service'
import {LeasedZonesService}  from '../services/leased-zones/leased-zones.service'
import {TicketsService} from '../services/tickets/tickets.service'
import {SharedService} from '../services/shared/shared.service'
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { LeasedZone } from '../model/leased_zone.model';
import { DatePipe } from '@angular/common';
import {ReservationDTO} from '../dto/reservation.dto';
import {TicketDTO} from '../dto/ticket.dto';
import {ReservationsService} from '../services/reservations/reservations.service';
import {AlertBoxComponent} from '../alert-box/alert-box.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

  maintenances:Maintenance[]=[];
  leasedZones: LeasedZone[]=[];
  tickets: Ticket[]=[];
  eventId: String='';
  reservedTickets: String[]=[];
  totalPrice: number=0;

  constructor(private modalService: NgbModal, public sharedService: SharedService, private eventsService: EventsService,private maintenancesService: MaintenancesService,private ticketsService: TicketsService,private leasedZoneService: LeasedZonesService,
    public datepipe: DatePipe, private reservationsService: ReservationsService,private router:Router ) { }

  ngOnInit() {
    let self=this;
    self.eventId=localStorage.getItem('reservation');
    self.maintenancesService.getByEventId(this.eventId).subscribe(data1=>{
      self.maintenances=data1;
      self.maintenances.forEach(function (maintenance) {
        let maintenanceDiv=self.printMaintenance(maintenance);
        self.leasedZoneService.getMaintenanceLeasedZones(maintenance.id).subscribe(data2=>{
          let lz=data2;
          lz.forEach(function (leasedZone) {
            let lzId=self.printLeasedZone(leasedZone,maintenanceDiv);
            self.leasedZones.push(leasedZone);
            let leasedZone2=leasedZone;
            self.ticketsService.getTickets(leasedZone.id).subscribe(data3=>{
              let tickets=data3;
              self.printTickets(tickets,lzId,leasedZone2);
            });
          });
        });
      });
    });
  }

  printMaintenance(m){
    let self=this;
    var sd = self.datepipe.transform(m.maintenanceDate, 'dd-MM-yyyy HH:mm');
    var div=document.createElement("div");
    div.id="m"+m.id;
    
    var div2=document.getElementById('right_div');
    var b=document.createElement('b');
    var p=document.createElement('p');
    p.style.textAlign="center";
    b.innerText=sd;
    p.append(b);
    div.append(p);
    div2.append(div);
    return div.id;
  }

  printLeasedZone(lz,maintenanceDiv){
    console.log("LEASED ZONE USAO");
    var li=document.createElement("li");
    li.innerText=lz.zone.name+" : "+lz.seatPrice+"$";
    var ul=document.getElementById('zones');
    ul.append(li);

    var ul2=document.getElementById("selected");
    var li2=document.createElement("li");
    var p=document.createElement("p");
    p.id=lz.id+"lzt";
    p.innerText=lz.zone.name+" tickets: ";
    var p2=document.createElement("p");
    p2.id=lz.id+"ticket";
    li2.append(p);
    li2.append(p2);
    ul2.append(li2);

    

    var leasedZoneName=document.createElement("p");
    leasedZoneName.innerText=lz.zone.name;
    leasedZoneName.style.textAlign="center";
    var maintenance=document.getElementById(maintenanceDiv);
    var leasedZoneDiv=document.createElement("div");
    leasedZoneDiv.id=lz.id+"lz";
    leasedZoneDiv.append(leasedZoneName);
    maintenance.append(leasedZoneDiv);
    leasedZoneDiv.style.textAlign="center";
    return leasedZoneDiv.id;
  }

  printTickets(tickets,lzId,lz){
    let self=this;
    let leasedZoneDiv=document.getElementById(lzId);
    let matrix: boolean=lz.zone.matrix;
    let row: number=lz.zone.rowNumber;
    let col: number=lz.zone.colNumber;
    let i: number=0;
    let j:number=0;
    let zone=lz;
    console.log("ROW: ",row);
    console.log("COL ",col);
    console.log(tickets.length);
    if(!matrix){

    }
    else{
      tickets.forEach(function(ticket){
        var div=document.createElement("button");
        div.style.width="20px";
        div.style.height="20px";
        div.style.display="inline-block";
        div.className="ticketDisplay";
        div.style.padding="15px";
        div.style.borderColor="white";
        div.style.textDecoration="none";
        if(!ticket.reserved){
          div.style.backgroundColor="green";
          let seatId=ticket.id;
          div.id=seatId;
          div.name="green";
          
          div.onclick= function(e){
              self.reserveSeat(seatId,zone.id,zone.seatPrice);
          }
        }else{
          div.style.backgroundColor="red";
        }
        leasedZoneDiv.append(div);
        i++;
        if(i==row){
          var br=document.createElement('br');
          leasedZoneDiv.append(br);
          j++;
          i=0;
        }
        
      });
    
    }

  }

  reserveSeat(id,zoneId,price){
    let self=this;
    let ticketId=id;
    var div=document.getElementById(id);
    var pp=document.getElementById(zoneId+"ticket");
    let numReserved;
    if(pp.innerText==""){
      numReserved=0;
    }else{
      numReserved=+pp.innerText;
    }
    self.totalPrice+=price;
    var pr=document.getElementById("total_price");
    pr.innerText="Total price: "+self.totalPrice;
    numReserved++;
    pp.innerText=numReserved;
    div.onclick=function(e){
      self.unreserveSeat(ticketId,zoneId,price);
    }
    self.reservedTickets.push(id);
    div.style.backgroundColor="blue";

  }

  unreserveSeat(id,zoneId,price){
    var div=document.getElementById(id);
    var pp=document.getElementById(zoneId+"ticket");
    let numReserved;
    if(pp.innerText==""){
      numReserved=0;
    }else{
      numReserved=+pp.innerText;
    }
    numReserved--;
    this.totalPrice-=price;
    var pr=document.getElementById("total_price");
    pr.innerText="Total price: "+this.totalPrice;
    pp.innerText=numReserved;
    let self=this;
    let ticketId=id;
    div.onclick=function(e){
      self.reserveSeat(ticketId,zoneId,price);
    }
    self.reservedTickets.forEach(function(ticket,index){
      if(ticket==ticketId){
        self.reservedTickets.splice(index,1);
        div.style.backgroundColor="green";
        return;
      }
    })
  }

  proceed(){
    let self=this;
    this.eventsService.getById(localStorage.getItem('reservation')).subscribe(data=>{
      let e=data;
      let ticketDto: TicketDTO[]=[];
      self.reservedTickets.forEach(function(tic){
        var tickDto: TicketDTO={
          id: +tic 
        }
        ticketDto.push(tickDto);
      });
      var reservationDto: ReservationDTO={
        event: e,
        tickets: ticketDto,
        qrCode: "" 
      }
      this.reservationsService.createReservation(reservationDto).subscribe(data=>{
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message=data.message;
        if(data.message=="Success"){
          this.router.navigate(['dashboard/events']);
        }
      })
      


    });
    
  }

}
