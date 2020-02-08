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
import { DefaultValueAccessor } from '@angular/forms';
import { createInput } from '@angular/compiler/src/core';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

  maintenances:Maintenance[]=[];
  leasedZones: LeasedZone[]=[];
  ticketsAll: Ticket[]=[];
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
          let capa=0;
          lz.forEach(function (leasedZone) {
            
            let lz=leasedZone;
            let lzId;
            if(!lz.zone.matrix){
              
              
              self.ticketsService.getLeasedZoneReservedTickets(lz.id).subscribe(data=>{
                capa=lz.zone.capacity-data.length;
                lz.zone.capacity=capa;
                console.log("CAPA ",capa);
                self.leasedZones.push(lz);
                self.addModal(lz.id,lz.zone.capacity);
                lzId=self.printLeasedZone(lz,maintenanceDiv);
                self.ticketsService.getTickets(lz.id).subscribe(data3=>{
                  let tickets=data3;
                  tickets.forEach(function(ticket){
                      self.ticketsAll.push(ticket);
                  })
                  self.printTickets(tickets,lzId,lz);
                });
              });

            }
          });
        });
      });
    });
  }

  closeModal(modalId){
    let id=modalId;
    var model=document.getElementById(id);
    model.style.display="none";
  }

  addModal(idZone,capacity){
    let self=this;
    let zoneid = idZone;
    var modal=document.createElement('div');
    modal.id="myModal"+idZone;
    modal.style.display="none";
    modal.style.position="fixed";
    modal.style.zIndex="1";
    modal.style.left="0";
    modal.style.top="0";
    modal.style.width="100%";
    modal.style.height="100%";
    modal.style.overflow="auto";
    modal.style.backgroundColor="rgba(0,0,0,0.4)";
    

    var modal_content=document.createElement('div');
    modal_content.style.backgroundColor="grey";
    modal_content.style.margin="15% auto";
    modal_content.style.padding="20px";
    modal_content.style.border="1px solid grey";
    modal_content.style.width="40%";
    var h3=document.createElement("h3");
    var p=document.createElement("p");
    p.innerText="Number of tickets you want to reserve: ";
    p.style.display="inline-block";
    p.className="inline";
    var input=document.createElement("input");
    input.id="input"+idZone;
    input.value="0";
    input.style.display="inline-block";
    input.className="inline";

    h3.innerText="Tickets left: "+ capacity;
    modal_content.append(h3);
    modal_content.append(p);
    modal_content.append(input);
    var br=document.createElement("br");
    modal_content.append(br);
    var button_cancel=document.createElement('button');
    button_cancel.innerText="Cancel";
    button_cancel.className="btn btn-danger";
    button_cancel.style.textAlign="center";
    button_cancel.onclick=function(e){
      self.closeModal("myModal"+zoneid);
    }
    var button_ok=document.createElement('ok');
    button_ok.onclick=function(e){
      self.reserveParter("input"+zoneid,zoneid);
    }
    button_ok.style.textAlign="center";
    button_ok.innerText="OK";
    button_ok.className="btn btn-primary";
    modal_content.append(button_cancel);
    modal_content.append(button_ok);
    modal.append(modal_content);
    var container=document.getElementById("container");
    container.append(modal);
  }

  reserveParter(inputId,zoneId){
    let self=this;
    let zoneid=zoneId;
    var input=(<HTMLInputElement>document.getElementById(inputId));
    let num=+input.value;
    if(num>=0){
    console.log("NUMBER: ",num);
    let tickets: Ticket[]=[];
    var pp=document.getElementById(zoneId+"ticket");
    pp.style.display="inline-block";
    pp.innerText=num+'';
    var pr=document.getElementById("total_price");
    
    self.ticketsAll.forEach(function(ticket){
      if(self.reservedTickets.indexOf(ticket.id+'') > -1 && ticket.zone.id==zoneid){
        let elInd=self.reservedTickets.indexOf(ticket.id+'');
        self.totalPrice-=ticket.price;
        self.reservedTickets.splice(elInd,1);
      }
    })

    let i=0;
    self.ticketsAll.forEach(function(ticket){
      if(ticket.reserved==false && ticket.zone.id==zoneid){
        if(i<num){
          self.reservedTickets.push(ticket.id+'');
          i++;
          self.totalPrice+=ticket.price;
        }
        

      }

    })
  }else{
    
    
  }
  pr.innerText="Total price: "+self.totalPrice;
  self.closeModal("myModal"+zoneid);
  console.log("LEN RESERVED: ",self.reservedTickets.length);

  }

  openModal(modalId){
    let id=modalId;
    var model=document.getElementById(id);
    model.style.display="block";

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
      var div=document.createElement("button");
      div.style.width="300px";
      div.style.height="200px";
      div.style.display="inline-block";
      div.className="ticketDisplay";
      div.style.padding="15px";
      div.style.borderColor="white";
      div.style.textDecoration="none";
      if(lz.capacity==0){
        div.style.backgroundColor="red";
      }else{
        div.style.backgroundColor="yellow";
        div.onclick= function(e){
          self.reserveNonMatrix(zone.id,zone.seatPrice,zone.zone.capacity);
        }

      }
      leasedZoneDiv.append(div);


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
    pp.style.display="inline-block";
    pp.style.display="inline-block";
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
    pp.style.display="inline-block";
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

  reserveNonMatrix(zoneId, zonePrice,capacity){
    this.openModal("myModal"+zoneId);
    //this.addModal(zoneId,capacity);

  }

}
