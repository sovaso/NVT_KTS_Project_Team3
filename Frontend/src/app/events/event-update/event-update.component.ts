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

@Component({
  selector: 'app-event-update',
  templateUrl: './event-update.component.html',
  styleUrls: ['./event-update.component.css']
})
export class EventUpdateComponent implements OnInit {
  event : any;
  loggedUser: CurrentUser;
  eventLocationId : string;
  leasedZonesUpdatable : LeasedZoneUpdatableDto[] = [];
  leasedZonesDto : LeasedZoneDto[] = [];
  maintenancesDto : MaintenanceDto[] = [];
  locations: Location[];
  events: Event[];
  locationId: string = "";
  daysNum = 1;
  errors: number = 0;
  picture:string;

  noNameError: string = "Name and type of event are both required.";
  nameExistsError: string = "Name of event already exists. Please choose another name.";
  noLocationError: string = "Location of event is required.";
  noLocationZoneError: string = "At least one of location zones must be selected.";
  noPriceError: string = "Please define the price for selected location zones.";
  invalidPriceError: string = "Defined price is not valid. Please insert a number between 1 and 10000.";
  maintenanceError = "You have to add at least one event day.";

  constructor(private modalService: NgbModal, 
    public sharedService: SharedService,
    private eventsService: EventsService,
    private maintenancesService: MaintenancesService,
    private locationZonesService:LocationZonesService,
    private leasedZonesService:LeasedZonesService,
    private router: Router,
    public datepipe: DatePipe) { }

  ngOnInit() {
    let eventId = JSON.parse(localStorage.getItem("eventForUpdate"));
    let self = this;
    this.leasedZonesService.getEventLeasedZonesUpdatable(eventId).subscribe(leasedZones => {
      self.leasedZonesUpdatable = leasedZones;
    })
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    this.sharedService.events.subscribe(events => (this.events = events));
    
    this.eventsService.getById(eventId).subscribe(data => {
      self.event = data;
      self.locationId = self.event.locationInfo.id;
      self.maintenancesService.getByEventId(eventId).subscribe(maintenances=> {
        maintenances.forEach(function(m){
          let m__ = m;
          var sd = self.datepipe.transform(m__.maintenanceDate, 'yyyy-MM-ddTHH:mm');
          var ed = self.datepipe.transform(m__.maintenanceEndTime, 'yyyy-MM-ddTHH:mm');
          let main : MaintenanceDto = {id: m__.id, startDate: sd, 
            endDate:ed, eventId: self.event.id, locationZones:[]};
            self.leasedZonesService.getMaintenanceLeasedZones(m__.id).subscribe(lesdZones=> {
              lesdZones.forEach(function(lesdZone){
                var liz : LeasedZoneDto = {id:lesdZone.id, zoneId:lesdZone.zone.id, maintenanceId:lesdZone.maintenance.id,price:lesdZone.seatPrice};
                main.locationZones.push(liz);
              })
          })
          self.maintenancesDto.push(main);
        })
        self.initMaintenances();
        self.initLocationZones(self.event.locationInfo.id);
      })
    });
    this.sharedService.locations.subscribe(locations => {
      self.locations = locations
      var selector = document.getElementById("location_selector");
      self.locations.forEach(function (location) {
        if(location.status == true){
          var option = document.createElement("option");
          option.value = location.id;
          option.innerText = location.name + ", " + location.address;
          selector.append(option);
        }
      });
    });
  }

  initLocationZones(locationId){
    let choosen = locationId;
    this.locationId = locationId;
    let self = this;
    let printedZones = [];
    this.locationZonesService.getLocationZones(choosen).subscribe(locationZones => {
      let _location_zones = locationZones;
      self.maintenancesDto.forEach(function(m){
        let _m = m;
        if(choosen != ""){
          document.querySelector('div#zoneDisplay'+m.id).innerHTML = "";
  
          var div = document.getElementById("zoneDisplay"+m.id);
          var table = document.getElementById("zoneTable"+m.id);
          table.innerHTML = ""
  
          var p = document.createElement("p");
          p.innerText = "Location zones:";
          div.append(p);
  
          var th = document.createElement("tr");
          
          var td1 = document.createElement("td");
          td1.style.width = "35%";
          var b1 = document.createElement("b");
          b1.innerText = "Zone name";
          td1.append(b1);
  
          var td2 = document.createElement("td");
          var b2 = document.createElement("b");
          td2.style.width = "35%";
          b2.innerText = "Capacity";
          td2.append(b2);
  
          var td3 = document.createElement("td");
          td3.style.width = "15%";
          var b3 = document.createElement("b");
          b3.innerText = "Price";
          td3.append(b3);
  
          var td4 = document.createElement("td");
          td4.style.width = "15%";
  
          th.append(td1);
          th.append(td2);
          th.append(td3);
          th.append(td4);
          table.append(th);
        }
        m.locationZones.forEach(function(lz){
          let lzo = lz;
          let upo = true;
          self.leasedZonesUpdatable.forEach(function(upd){
            if(upd.id == lzo.id){
              upo = upd.updatable;
            }
          })
          printedZones.push({zone_Id:lz.zoneId, price:lz.price, updatable:upo});
        })
        _location_zones.forEach(function(locationZone){
          let _lz = locationZone;
          let price = 0;
          let zoneId_ = _lz.id+"";
          var table = document.getElementById("zoneTable"+_m.id);
          var tr = document.createElement("tr");

          var td1 = document.createElement("td");
          td1.style.width = "35%";
          td1.innerText = _lz.name;
    
          var td2 = document.createElement("td");
          td2.style.width = "35%";
          td2.innerText = _lz.capacity + "";
          tr.append(td1);
          tr.append(td2);

          var td3 = document.createElement("td");
          td3.style.width = "15%";
          td3.id="td"+zoneId_+_m.id+"";

          let updatable_ = 1;
          printedZones.forEach(function(added){
            if(added.zone_Id == zoneId_){
              price = added.price;
              if(added.updatable == false){
                updatable_ = 0;
              }
            }
          })
          if(price > 0 && updatable_ == 1){
            var input = document.createElement("input");
            input.type = "number";
            input.name = zoneId_;
            input.id = "input"+zoneId_+_m.id+"";
            input.className = "price_input";
            input.onchange = function(e){
              self.initPrice(zoneId_,_m.id+"");
            }
            input.value = price+"";
            input.style.width = '90%';
            td3.append(input);
            var zone : LeasedZoneDto = {id : "", zoneId : locationZone.id, maintenanceId: "", price : price};
            self.leasedZonesDto.push(zone);
            var td4 = document.createElement("td");
            td4.style.width = "15%";
            var checkbox = document.createElement("button");
            checkbox.className = "btn btn-primary";
            checkbox.innerText = "Unselect";
            checkbox.name = zoneId_+_m.id+"";
            checkbox.id = "checkbox"+zoneId_+_m.id+"";
            checkbox.onclick = function(e){
              self.selectZoneButton(zoneId_,_m.id);
            }
            checkbox.style.width = '90%';
            td4.append(checkbox);
            tr.append(td3);
            tr.append(td4);
            table.append(tr);
        }
        else if(price > 0 && updatable_ == 0){
            var p8 = document.createElement("p");
            p8.innerText = price+"";
            p8.style.width = '90%';
            var zone : LeasedZoneDto = {id : "", zoneId : zoneId_, maintenanceId: "", price : price};
            self.leasedZonesDto.push(zone);
            var td4 = document.createElement("td");
            td4.style.width = "15%";
            td3.append(p8);
            tr.append(td3);
            tr.append(td4);
            table.append(tr);
          }
          else{
            var td4 = document.createElement("td");
            td4.style.width = "15%";
            var checkbox = document.createElement("button");
            checkbox.className = "btn btn-primary";
            checkbox.innerText = "Select";
            checkbox.name = zoneId_+_m.id+"";
            checkbox.id = "checkbox"+zoneId_+_m.id+"";
            checkbox.onclick = function(e){
              self.selectZoneButton(zoneId_,_m.id);
            }
            checkbox.style.width = '90%';
            td4.append(checkbox);
            tr.append(td3);
            tr.append(td4);
            table.append(tr);
          }
        })
      })
    });
  }

  newLocationZones(locationId, nums_){
    let choosen = locationId;
    let self = this;
    let nums = nums_;
    if(choosen != ""){
        document.querySelector('div#zoneDisplay'+nums).innerHTML = "";

        var div = document.getElementById("zoneDisplay"+nums);
        var table = document.getElementById("zoneTable"+nums);
        table.innerHTML = ""

        var br = document.createElement("br");

        var p = document.createElement("p");
        p.innerText = "Location zones:";
        div.append(br);
        div.append(p);

        var th = document.createElement("tr");
        
        var td1 = document.createElement("td");
        td1.style.width = "35%";
        var b1 = document.createElement("b");
        b1.innerText = "Zone name";
        td1.append(b1);

        var td2 = document.createElement("td");
        var b2 = document.createElement("b");
        td2.style.width = "35%";
        b2.innerText = "Capacity";
        td2.append(b2);

        var td3 = document.createElement("td");
        td3.style.width = "15%";
        var b3 = document.createElement("b");
        b3.innerText = "Price";
        td3.append(b3);

        var td4 = document.createElement("td");
        td4.style.width = "15%";

        th.append(td1);
        th.append(td2);
        th.append(td3);
        th.append(td4);
        table.append(th);

        this.locationZonesService.getLocationZones(choosen).subscribe(locationZones => {
          locationZones.forEach(function(locationZone){
           var table = document.getElementById("zoneTable"+nums);
           var tr = document.createElement("tr");
         
           var td1 = document.createElement("td");
           td1.style.width = "35%";
           td1.innerText = locationZone.name;
    
           var td2 = document.createElement("td");
           td2.style.width = "35%";
           td2.innerText = locationZone.capacity + "";
    
           var td3 = document.createElement("td");
           td3.style.width = "15%";
           td3.id="td"+locationZone.id+nums;
    
           var td4 = document.createElement("td");
           td4.style.width = "15%";
           var checkbox = document.createElement("button");
           checkbox.className = "btn btn-primary";
           checkbox.innerText = "Select";
           checkbox.name = locationZone.id + nums;
           checkbox.id = "checkbox"+locationZone.id + nums;
           let zon = locationZone;
           checkbox.onclick = function(e){
            self.selectZoneButton(zon.id, nums);
           }
           checkbox.style.width = '90%';
           td4.append(checkbox);
    
           tr.append(td1);
           tr.append(td2);
           tr.append(td3);
           tr.append(td4);
           table.append(tr);
         });
       });
    }
  }

  selectZoneButton(zoneId, maintenanceId){
    let self = this;
    let _id = zoneId;
    let __mid = maintenanceId;
    let button = document.getElementById("checkbox"+zoneId +maintenanceId);
    if(button.textContent == "Select"){
      button.innerText = "Unselect";
      let zone : LeasedZoneDto = {id : "", zoneId : _id, maintenanceId: maintenanceId, price : 0};
      this.leasedZonesDto.push(zone)
      var priceInput = document.getElementById("td"+_id+__mid);
      var input = document.createElement("input");
      input.type = "number";
      input.name = _id +__mid+"";
      input.id = "input"+_id +__mid;
      input.className = "price_input";
      input.onchange = function(e){
        self.initPrice(_id, __mid);
      }
      input.style.width = '90%';
      priceInput.append(input);
    }

    else{
      button.innerText = "Select";
      var priceInput = document.getElementById("td"+_id+__mid);
      priceInput.innerHTML = "";
      self.leasedZonesDto.forEach(function(data, index){
        if(data.zoneId == _id && data.maintenanceId == maintenanceId){
          self.leasedZonesDto.splice(index,1);
        }
      })
    }
  }

  initPrice(id, mid){
    let self = this;
    let _mid = mid;
    let _id = id;
    self.leasedZonesDto.forEach(function(data){
      if(data.zoneId == _id && data.maintenanceId == _mid){
        var input = <HTMLInputElement> document.getElementById("input"+_id+_mid);
        data.price = parseInt(input.value);
        //UPDATE
      }
    });
    this.checkPrices();
  }

  checkPrices(){
    let self = this;
    let error_indexes = [];
    let all_indexes = [];
    self.leasedZonesDto.forEach(function(lz){
      if(lz.price < 1 || lz.price > 10000){
        error_indexes.push(lz.maintenanceId);
        all_indexes.push(lz.maintenanceId);
      }
      else{
        all_indexes.push(lz.maintenanceId);
      }
    })
    all_indexes.forEach(function(index){
      var error = document.getElementById("error_zones"+index);
      if(error_indexes.indexOf(index) > -1){
        error.innerText = self.invalidPriceError;
      }
      else{
        error.innerText = "";
      }
    })
    if(error_indexes.length > 0){
      return 1;
    }
    else{
      return 0;
    }
  }

  initMaintenances(){
    let self = this;
    self.maintenancesDto.forEach(function(m){
      let mid = m.id+"";
      var mainDiv = document.getElementById("addedDays");
      var div = document.createElement("div");
      div.className = "day_display";
      div.style.borderStyle = "ridge";
      div.style.padding = "5%";
      div.id = "day" +mid;

      var close = document.createElement("button");
      close.className = "x";
      close.innerText = "X";
      close.style.background = "red";
      close.style.color = "white";
      close.style.marginLeft = "95%";
      close.style.marginTop = "0%";
      close.style.width = "10%";
      close.onclick = function(e){
        self.removeDay(mid);
      }
      div.append(close);

      var p1 = document.createElement("p");
      p1.innerText = "Start date and time:";
      div.append(p1);

      var input1 = document.createElement("input");
      input1.type = "datetime-local";
      input1.id = "start"+mid;
      input1.className = "form-control";
      input1.name = "date_of_event";
      console.log("DDDDDDDDDDDDDDDDDDDDDDDDDD1111");
      console.log(m.startDate);
      console.log(m.endDate);
      input1.value = m.startDate;
      input1.onblur = function(){
        self.checkDate(mid);
      }
      div.append(input1);

      var br = document.createElement("br");
      div.append(br);

      var p2 = document.createElement("p");
      p2.innerText = "End date and time:";
      div.append(p2);

      var input2 = document.createElement("input");
      input2.type = "datetime-local";
      input2.id = "end"+mid;
      input2.className = "form-control";
      input2.name = "date_of_event";
      input2.value = m.endDate;
      input2.onblur = function(){
        self.checkDate(mid);
      }

      div.append(input2);

      var erorDiv = document.createElement("div");
      erorDiv.style.color = "red";
      erorDiv.id = "error_day"+mid;
      div.append(erorDiv);

      var divZone = document.createElement("div");
      divZone.id = "zoneDisplay"+mid;
      div.append(divZone);

      var tableZone = document.createElement("table");
      tableZone.id = "zoneTable"+mid;
      tableZone.style.width = "100%";
      tableZone.style.border = "1px solid";
      div.append(tableZone);

      var erorDivZ = document.createElement("div");
      erorDivZ.style.color = "red";
      erorDivZ.id = "error_zones"+mid;
      div.append(erorDivZ);

      mainDiv.appendChild(div);
      })
  }

  addNewDay(){
    this.daysNum +=1;
    let nums = this.daysNum+0;
    var mainDiv = document.getElementById("addedDays");
    var div = document.createElement("div");
    div.className = "day_display";
    div.style.borderStyle = "ridge";
    div.style.padding = "5%";
    div.id = "day" + this.daysNum;

    var close = document.createElement("button");
    close.className = "x";
    close.innerText = "X";
    close.style.background = "red";
    close.style.color = "white";
    close.style.marginLeft = "95%";
    close.style.marginTop = "0%";
    close.style.width = "10%";
    let self = this;
    close.onclick = function(e){
      self.removeDay(nums);
    }
    div.append(close);

    var p1 = document.createElement("p");
    p1.innerText = "Start date and time:";
    div.append(p1);

    var input1 = document.createElement("input");
    input1.type = "datetime-local";
    input1.id = "start"+nums;
    input1.className = "form-control";
    input1.name = "date_of_event";
    input1.onblur = function(){
      self.checkDate(nums);
    }
    div.append(input1);

    var br = document.createElement("br");
    div.append(br);

    var p2 = document.createElement("p");
    p2.innerText = "End date and time:";
    div.append(p2);

    var input2 = document.createElement("input");
    input2.type = "datetime-local";
    input2.id = "end"+nums;
    input2.className = "form-control";
    input2.name = "date_of_event";
    input2.onblur = function(){
      self.checkDate(nums);
    }

    div.append(input2);

    var erorDiv = document.createElement("div");
    erorDiv.style.color = "red";
    erorDiv.id = "error_day"+nums;
    div.append(erorDiv);

    var divZone = document.createElement("div");
    divZone.id = "zoneDisplay"+nums;
    div.append(divZone);

    var tableZone = document.createElement("table");
    tableZone.id = "zoneTable"+nums;
    tableZone.style.width = "100%";
    tableZone.style.border = "1px solid";
    div.append(tableZone);

    var erorDivZ = document.createElement("div");
    erorDivZ.style.color = "red";
    erorDivZ.id = "error_zones"+nums;
    div.append(erorDivZ);

    mainDiv.appendChild(div);
    self.newLocationZones(self.locationId, nums);
  }

  removeDay(id){
    // DELETE
    this.daysNum -=1;
    var element = document.getElementById("day"+id);
    element.parentNode.removeChild(element);
    let self = this;
    let _id = id;
    self.maintenancesDto.forEach(function(data, index){
      if(data.eventId == _id){
        self.maintenancesDto.splice(index,1);
      }
    });
    self.leasedZonesDto.forEach(function(loc, indx){
      if(loc.maintenanceId == _id){
        self.leasedZonesDto.splice(indx,1);
      }
    })
  }

  checkAllDates(){
    let self = this;
    let retval_ = 0;
    if(self.maintenancesDto.length == 0){
      retval_ = 1;
      var element = document.getElementById("maintenance_error");
      element.innerText = self.maintenanceError;
    }
    else{
      var element = document.getElementById("maintenance_error");
      element.innerText = "";
    }
    self.maintenancesDto.forEach(function(data){
      if(data.id == "-1"){
        retval_ = 1;
      }
    });
    return retval_;
  }

  checkDate(id){
    let self = this;
    this.errors = 0;
    var start: string = (<HTMLInputElement> document.getElementById("start"+id)).value;
    start = start.replace("T", " ");
    var end: string = (<HTMLInputElement> document.getElementById("end"+id)).value;
    end = end.replace("T", " ");
    let _id = id;

    if(start != "" && end != ""){
      let locationId = this.event.locationInfo.id;
      let maintenance: MaintenanceDto = {startDate: start, endDate:end, id:locationId, eventId:_id, locationZones:[]}; 
      this.maintenancesService.checkEventDates(maintenance,self.event.id).subscribe(data => {
        if(data.message != "OK"){
          var error = document.getElementById("error_day"+_id);
          error.innerText = data.header;
          self.errors = 1;
          maintenance.id = "-1";
        }
        else{
          var error = document.getElementById("error_day"+_id);
          error.innerText = "";
          maintenance.id = "1";
        }
    });
    self.maintenancesDto.forEach(function(data,index){
      if(data.eventId == _id){
        self.maintenancesDto.splice(index,1);
      }
    });
    self.maintenancesDto.push(maintenance);
    var element = document.getElementById("maintenance_error");
    element.innerText = "";
  }
  }

  

  formatDate(date : Date): string {
    return date.getFullYear
              + '-' + this.leftpad(date.getMonth)
              + '-' + this.leftpad(date.getDate)
              + ' ' + this.leftpad(date.getHours)
              + ':' + this.leftpad(date.getMinutes)
  }

  leftpad(val, resultLength = 2, leftpadChar = '0'): string {
    return (String(leftpadChar).repeat(resultLength)
          + String(val)).slice(String(val).length);
  }

  upload(){
    let self = this;
    console.log(self.picture);
    var upload : UploadFileDto = {pathToFile: self.picture}; 
    this.eventsService.uploadFile(upload, this.event.id).subscribe(links=> {
      console.log(links);
    });
  }

}
