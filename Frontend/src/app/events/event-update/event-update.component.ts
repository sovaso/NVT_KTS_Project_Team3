import { Component, OnInit } from '@angular/core';
import { CurrentUser } from 'src/app/model/currentUser';
import { SharedService } from 'src/app/services/shared/shared.service';
import { EventsService } from 'src/app/services/events/events.service';
import { Location } from 'src/app/model/location.model';
import {LeasedZoneDto} from  'src/app/dto/leased_zone.dto';
import {MaintenanceDto} from  'src/app/dto/maintenance.dto';
import {EventDto} from  'src/app/dto/event.dto';
import { LocationZonesService } from 'src/app/services/location-zones/location-zones.service';
import { MaintenancesService } from 'src/app/services/maintenances/maintenances.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Event } from 'src/app/model/event.model';
import { LeasedZone } from 'src/app/model/leased_zone.model';
import { LocationZone } from 'src/app/model/location_zone.model';
import { Router } from '@angular/router';
import { LeasedZonesService } from 'src/app/services/leased-zones/leased-zones.service';
import { LeasedZoneUpdatableDto } from 'src/app/dto/leased_zone_updatable.dto';
import { DatePipe } from '@angular/common';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { Media } from 'src/app/model/media.model';
import { LocationsService } from 'src/app/services/locations/locations.service';
import { Maintenance } from 'src/app/model/maintenance.model';
import { Ticket } from 'src/app/model/ticket.model';
import { CompileShallowModuleMetadata } from '@angular/compiler';

@Component({
  selector: 'app-event-update',
  templateUrl: './event-update.component.html',
  styleUrls: ['./event-update.component.css']
})
export class EventUpdateComponent implements OnInit {
  event : any;
  loggedUser: CurrentUser;
  locationUpatable : boolean;

  picture:string;
  selectedFiles: FileList;
  currentFileUpload: File;
  strings: String[]=[];
  locations: Location[]=[];
  maintenances: Maintenance[]=[];
  leasedZones: LeasedZone[]=[];
  location : Location;
  locationZones : LocationZone[];
  locationChanges: number = 0;

  constructor(private modalService: NgbModal, 
    public sharedService: SharedService,
    private eventsService: EventsService,
    private maintenancesService: MaintenancesService,
    private locationZonesService:LocationZonesService,
    private locationService:LocationsService,
    private leasedZonesService:LeasedZonesService,
    private router: Router,
    public datepipe: DatePipe) { }

  ngOnInit() {
    let eventId = JSON.parse(localStorage.getItem("eventForUpdate"));
    let self = this;
    self.locationService.getActive().subscribe(data=>{
      self.locations=data;
    });

    self.eventsService.getById(eventId).subscribe(data => {
      self.event = data;
      self.location = self.event.locationInfo;
      self.locationZonesService.getLocationZones(self.location.id).subscribe(lzones => {
        self.locationZones = lzones;
      })
      self.leasedZonesService.getEventLeasedZones(self.event.id).subscribe(leasedZones => {
        self.leasedZones=leasedZones;
      });
      self.getMedia();

      //self.locationService.locationUpdatable(self.event.locationInfo.id).subscribe(bol => {
      //  self.locationUpatable = bol;
      //})

      self.maintenancesService.getByEventId(self.event.id).subscribe(maintenances => {
        self.maintenances=maintenances;
      });
    });

    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    //this.sharedService.events.subscribe(events => (this.events = events));
  }

  selectFile(event) {
    this.selectedFiles = event.target.files;
  
  }

  save() {
    if (this.selectedFiles) this.currentFileUpload = this.selectedFiles.item(0);
    console.log("FILE: ",this.currentFileUpload);
    this.eventsService.uploadFile(this.currentFileUpload,this.event.id).subscribe(data => {
      this.strings = data;
      console.log(this.strings);
      this.getMedia();
    }
    );
  }

  getMedia(){
    document.getElementById('media_container1').innerHTML = "";
    this.eventsService.getAllMedia(this.event.id).subscribe(data => {
      let div= document.getElementById('media_container1');
      data.forEach(function (media) {
        var element=document.createElement("img");
        let el=media.link.split("/");
        let fixedLink=el[0]+"//"+el[2]+"/"+"uc?export=view&id="+el[5];
        element.src=fixedLink;
        element.style.height = "200px";
        element.style.display = "inline-block";
        element.style.padding = "10px";
        div.append(element);
      });
    });
  }

  format_date(date:Date){
    var d=this.datepipe.transform(date, 'yyyy-MM-ddTHH:mm');
    return d;
  }

  format_dateDto(date:Date){
    var d=this.datepipe.transform(date, 'yyyy-MM-dd HH:mm');
    return d;
  }

  locationZoneAdded(lozationZone,maintenanceId){
    let self = this;
    let locZone : LocationZone = lozationZone;
    let mid = maintenanceId;
    let retval:boolean = false;
    self.leasedZones.forEach(function(lz){
      if(lz.zone.id == locZone.id && lz.maintenance.id == mid){
        retval = true;
      }
    })
    return retval;
  }

  addZone(zoneId, maintenaceId){
    let self = this;
    let lzId = zoneId;
    let mId = maintenaceId;

    var input = (<HTMLInputElement>document.getElementById(zoneId+","+maintenaceId));
    var price_ = input.value;

    if(price_ == "" || typeof price_ === "undefined"){
      return;
    }
    let price = +input.value;;

    let dto : LeasedZoneDto = {id:"",zoneId:lzId,maintenanceId:mId,price:+price};
    self.leasedZonesService.createLeasedZone(dto).subscribe(data => {
      if(self.locationChanges == 0){
          if(typeof data.message === 'undefined'){
            self.leasedZones.push(data);
            var error = document.getElementById(mId+"error_message");
            error.innerText = "Location zone successfully added!";
            error.style.color="green";
          }
          else{
            var error = document.getElementById(mId+"error_message");
            error.innerText = data.header;
            error.style.color="red";
          }
      }
      else{
        if(price < 1 || price > 10000){
          var error = document.getElementById(mId+"error_message");
            error.innerText = "Invalid ticket price. Please set a number between 1 and 10000.";
            error.style.color="red";
        }
        else{
          let locationZone : LocationZone;
          let maintenance : Maintenance;
          self.locationZones.forEach(function(locz){
            if(locz.id == lzId){
              locationZone = locz;
            }
          })
          self.maintenances.forEach(function(mm){
            if(mm.id == mId){
              maintenance = mm;
            }
          })
          var newLeasedZone : LeasedZone = {id:"",seatPrice:price,zone:locationZone, maintenance:maintenance, tickets:new Set<Ticket>()};
          self.leasedZones.push(newLeasedZone);
        }
      }
    })
  }

  removeZone(zoneId, maintenaceId){
    let self = this;
    let lzId = zoneId;
    let mid = maintenaceId;

    if(self.locationChanges == 0){
      self.leasedZonesService.removeLeasedZone(lzId).subscribe(data => {
        if(data.message == 'Success'){
          self.leasedZones.forEach(function(lz, index){
            if(lz.id == lzId){
              self.leasedZones.splice(index,1);
            }
          })
          var error = document.getElementById(mid+"error_message");
          error.innerText = data.header;
          error.style.color="green";
        }
        else{
          var error = document.getElementById(mid+"error_message");
          error.innerText = data.header;
          error.style.color="red";
        }
      })
    }
    else{
      self.leasedZones.forEach(function(lz,index){
        if(lz.zone.id == lzId && lz.maintenance.id == mid){
          self.leasedZones.splice(index,1);
        }
      })
    }
  }

  updateZone(zoneId,maintenaceId){
    let self = this;
    let lzId = zoneId;
    let mid = maintenaceId;
    var input = (<HTMLInputElement>document.getElementById(zoneId+","+maintenaceId));
    let price = +input.value;
    self.leasedZones.forEach(function(data, i){
      let index = i;
      if(data.zone.id == lzId && data.maintenance.id == mid){
        let updatedLeasedZone = data;
        if(self.locationChanges == 0){
          var dto : LeasedZoneDto = {id:data.id,zoneId:data.zone.id,maintenanceId:data.maintenance.id,price:+price};
          self.leasedZonesService.updateLeasedZone(dto).subscribe(data => {
            if(typeof data.message === 'undefined'){
              updatedLeasedZone = data;
              var error = document.getElementById(mid+"error_message");
              error.innerText = "Location zone successfully updated!";
              error.style.color="green";
            }
            else{
              var error = document.getElementById(mid+"error_message");
              error.innerText = data.header;
              error.style.color="red";
            }
          })
        }
        else{
          if(price < 1 || price > 10000){
            var error = document.getElementById(mid+"error_message");
              error.innerText = "Invalid ticket price. Please set a number between 1 and 10000.";
              error.style.color="red";
          }
          else{
            self.leasedZones.forEach(function(oldz,inde){
              if(oldz.zone.id == lzId && oldz.maintenance.id == mid){
                self.leasedZones.splice(inde,1);
              }
            })
            let locationZone : LocationZone;
            let maintenance : Maintenance;
            self.locationZones.forEach(function(locz){
              if(locz.id == lzId){
                locationZone = locz;
              }
            })
            self.maintenances.forEach(function(mm){
              if(mm.id == mid){
                maintenance = mm;
              }
            })
            var newLeasedZone : LeasedZone = {id:"",seatPrice:price,zone:locationZone, maintenance:maintenance, tickets:new Set<Ticket>()};
            self.leasedZones.push(newLeasedZone);
            }
        }
      }
    })
  }

  changeLocation(locationId){
    this.locationChanges++;
    let self = this;
    self.locationZonesService.getLocationZones(locationId).subscribe(locZones => {
      self.locationZones=locZones;
    });
    self.locationService.getById(locationId).subscribe(loc => {
      self.location = loc;
    });
    self.leasedZones = [];
  }

  updateMaintenance(maintenaceId){
    let self = this;
    let mid = maintenaceId;
    let maintenance_ : Maintenance;
    var start: string = (<HTMLInputElement> document.getElementById(maintenaceId+"start")).value;
    start = start.replace("T", " ");
    var end: string = (<HTMLInputElement> document.getElementById(maintenaceId+"end")).value;
    end = end.replace("T", " ");

    if(start != "" && end != ""){
      let maintenance: MaintenanceDto = {startDate: start, endDate:end, id:mid, eventId:self.event.id, locationZones:[]}; 
      this.maintenancesService.updateMaintenance(maintenance).subscribe(data => {
      if(typeof data.message === "undefined"){
        self.maintenances.forEach(function(m,i){
          if(m.id == mid){
            self.maintenances.splice(i,1);
          }
        })
        var error = document.getElementById(mid+"error_message");
        error.innerText = "";
        self.maintenances.push(data);
      }
      else{
        var error = document.getElementById(mid+"error_message");
        error.style.color = "red";
        error.innerText = data.header;
      }
    });
    }
  }

  updateEvent(){
      let self = this;
      let maintenancesDto : MaintenanceDto[] = [];
      self.maintenances.forEach(function(m){
        let leasedZonesDTO : LeasedZoneDto[] = [];
        let maintenance = m;
        self.leasedZones.forEach(function(lz){
          if(lz.maintenance.id == maintenance.id){
            var locZone : LeasedZoneDto = {id:"",zoneId:lz.zone.id,maintenanceId:maintenance.id,price:lz.seatPrice}
            leasedZonesDTO.push(locZone);
          }
        })
        var mDto : MaintenanceDto = {id:maintenance.id, startDate:self.format_dateDto(m.maintenanceDate),
          endDate:self.format_dateDto(m.maintenanceEndTime),locationZones:leasedZonesDTO,eventId:self.event.id};
        maintenancesDto.push(mDto);
      })

      let eventDto : EventDto = {id:self.event.id, name:self.event.name,description:"",locationId:self.location.id,
        maintenance:maintenancesDto,locationZones:[],eventType:self.event.type};
      console.log("OVO JE");
      console.log(eventDto);
      self.eventsService.update(eventDto).subscribe(data => {
        if(data.message != "Success"){
          var error = document.getElementById("errorEvent");
          error.style.color="red";
          error.innerText = data.header;
        }else{
          var error = document.getElementById("errorEvent");
          error.style.color="green";
          error.innerText = data.header;
        }
      })
  }

}
