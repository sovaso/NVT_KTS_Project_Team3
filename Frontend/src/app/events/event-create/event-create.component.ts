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
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.css']
})
export class EventCreateComponent implements OnInit {
  name: string;
  type: string = "";
  loggedUser: CurrentUser;
  locations: Location[];
  events: Event[];
  maintenances : MaintenanceDto[] = [];
  leasedZones : LeasedZoneDto[] = [];
  public choosenLocationId: string = "";
  startDate: string;
  endDate: string;
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
    private router: Router) { }

  ngOnInit() {
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    this.sharedService.locations.subscribe(locations => (this.locations = locations));
    this.sharedService.events.subscribe(events => (this.events = events));

    var selector = document.getElementById("location_selector");

    this.locations.forEach(function (location) {
      if(location.status == true){
        var option = document.createElement("option");
        option.value = location.id;
        option.innerText = location.name + ", " + location.address;
        selector.append(option);
      }
  });
  this.initLocationZones();

  var slides = document.getElementsByClassName("adding_window");
    for (let i = 0; i < slides.length; i++) {
      const slide = slides[i] as HTMLElement;
      if(slide.id != "basics"){
        slide.style.display = "none";
      }
      else{
        slide.style.display = "block";
      }
    }
  }

  switch_next_slide(slide_id){
    if(this.checkRequirements(slide_id) == true){
      var slides = document.getElementsByClassName("adding_window");
      for (let i = 0; i < slides.length; i++) {
        const slide = slides[i] as HTMLElement;
        if(slide.id != slide_id){
          slide.style.display = "none";
        }
        else{
          slide.style.display = "block";
        }
      }
    }
  }

  switch_prev_slide(slide_id){
    var slides = document.getElementsByClassName("adding_window");
    for (let i = 0; i < slides.length; i++) {
      const slide = slides[i] as HTMLElement;
      if(slide.id != slide_id){
        slide.style.display = "none";
      }
      else{
        slide.style.display = "block";
      }
    }
  }

  checkRequirements(slide_id){
    var errors = 0;
    if(slide_id == "maintenance"){
      var error = this.checkEventName();
      errors = errors + error;
      errors = errors + this.checkEventType();
      errors = errors + this.checkLocation();
    }
    else if(slide_id == "finish"){
        errors = errors + this.checkPrices();
        errors = errors + this.checkLeasedZones();
        errors = errors + this.checkAllDates();
        if(errors == 0){
          this.createEvent();
          return false;
        }
    }
    if(errors > 0){
      return false;
    }
    var message = document.getElementById("error_message_location_info");
    message.innerText = "";
    return true;
  }

  checkEventName(){
    let nameExists = false;
    if(typeof this.name === 'undefined' || this.name == null || this.name == ""){
      var error = document.getElementById("error_message_basics");
      error.innerText = this.noNameError;
      return 1;
    }
    else{
      let _name = this.name;
      let _error = this.nameExistsError;
      this.events.forEach(function (data) {
        if(data.name == _name){
          nameExists = true;
          var error = document.getElementById("error_message_basics");
          error.innerText = _error;
          return 1;
        }
      });

      if(nameExists == false){
        var error = document.getElementById("error_message_basics");
        error.innerText = "";
        return 0;
      }
    }
  }

  checkEventType(){
    if(this.type == ""){
      var error = document.getElementById("error_message_basics");
      error.innerText = this.noNameError;
      return 1;
    }
    else{
      var error = document.getElementById("error_message_basics");
      error.innerText = "";
      return 0;
    }
  }

  resetLocationZones(){
    this.leasedZones = [];
    this.initLocationZones();
  }

  initLocationZones(){
    let choosen = {id: this.choosenLocationId};
    let self = this;
    let nums = self.daysNum + 0;
    if(choosen.id != ""){
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

        this.locationZonesService.getLocationZones(choosen.id).subscribe(locationZones => {
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
      this.leasedZones.push(zone)
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
      self.leasedZones.forEach(function(data, index){
        if(data.zoneId == _id && data.maintenanceId == maintenanceId){
          self.leasedZones.splice(index,1);
        }
      })
    }
  }

  initPrice(id, mid){
    let self = this;
    let _mid = mid;
    let _id = id;
    self.leasedZones.forEach(function(data){
      if(data.zoneId == _id && data.maintenanceId == _mid){
        var input = <HTMLInputElement> document.getElementById("input"+_id+_mid);
        data.price = parseInt(input.value);
      }
    });
    this.checkPrices();
  }

  checkLocation(){
    let self = this;
    if(this.choosenLocationId == ""){
      var error = document.getElementById("error_message_location_info");
      error.innerText = self.noLocationError;
      return 1;
    }
    else{
      return 0;
    }
  }

  checkPrices(){
    let self = this;
    let error_indexes = [];
    let all_indexes = [];
    self.leasedZones.forEach(function(lz){
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

  checkLeasedZones(){
    let self = this;
    let ret = 0;
    self.maintenances.forEach(function(m){
      let maintenNum = 0;
      let _m = m;
      self.leasedZones.forEach(function(data){
        if(data.maintenanceId == _m.eventId){
          maintenNum = maintenNum + 1;
        }
      })
      if(maintenNum ==0){
        var error = document.getElementById("error_zones"+m.eventId);
        error.innerText = self.noLocationZoneError;
        ret = 1;
      }
      else{
        var error = document.getElementById("error_zones"+m.eventId);
        error.innerText = "";
      }
    })
    if(ret > 0){
      return 1;
    }
    else{
      return 0;
    }
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
    close.style.marginLeft = "100%";
    close.style.marginTop = "0%";
    close.style.width = "5%";
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
    this.initLocationZones();
  }

  removeDay(id){
    this.daysNum -=1;
    var element = document.getElementById("day"+id);
    element.parentNode.removeChild(element);
    let self = this;
    let _id = id;
    self.maintenances.forEach(function(data, index){
      if(data.eventId == _id){
        self.maintenances.splice(index,1);
      }
    });
    self.leasedZones.forEach(function(loc, indx){
      if(loc.maintenanceId == _id){
        self.leasedZones.splice(indx,1);
      }
    })
  }

  checkAllDates(){
    let self = this;
    let retval_ = 0;
    if(self.maintenances.length == 0){
      retval_ = 1;
      var element = document.getElementById("maintenance_error");
      element.innerText = self.maintenanceError;
    }
    else{
      var element = document.getElementById("maintenance_error");
      element.innerText = "";
    }
    self.maintenances.forEach(function(data){
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
      let locationId = this.choosenLocationId;
      let maintenance: MaintenanceDto = {startDate: start, endDate:end, id:locationId, eventId:_id, locationZones:[]}; 
      this.maintenancesService.checkMaintenance(maintenance).subscribe(data => {
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
    self.maintenances.forEach(function(data,index){
      if(data.eventId == _id){
        self.maintenances.splice(index,1);
      }
    });
    self.maintenances.push(maintenance);
    var element = document.getElementById("maintenance_error");
    element.innerText = "";
  }
  }

  createEvent(){
    this.generateLoader();
    let self = this;
    self.maintenances.forEach(function(m){
      let _m_ = m;
      self.leasedZones.forEach(function(lz){
        if(lz.maintenanceId == _m_.eventId){
          _m_.locationZones.push(lz);
        }
      })
    })
    var event : EventDto = {id:"0", name:self.name, description:"", eventType:self.type,
     maintenance:self.maintenances, locationZones:[], locationId:self.choosenLocationId};
     this.eventsService.create(event).subscribe(data => {
       console.log(data.header);
       console.log(data.message);
       this.sharedService.updateEvents();
       self.removeLoader();
       self.switch_prev_slide("finish");
     })
  }

  generateLoader(){
    var loader = document.getElementById("loader");
    loader.style.display = "block";
  }

  removeLoader(){
    var loader = document.getElementById("loader");
    loader.style.display = "none";
  }

  redirectToEvents(){
    this.router.navigate(['dashboard/events']);
  }

  upload(){
    console.log(this.picture);
  }
}
