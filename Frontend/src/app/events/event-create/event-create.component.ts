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
  leasedZones: LeasedZoneDto[]=[];
  public choosenLocationId: string = "";
  startDate: string;
  endDate: string;
  daysNum = 1;
  errors: number = 0;

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
    if(slide_id == "location_info"){
      var error = this.checkEventName();
      errors = errors + error;
      errors = errors + this.checkEventType();
    }
    else if(slide_id == "maintenance"){
        errors = errors + this.checkPrices();
        errors = errors + this.checkLeasedZones();
        errors = errors + this.checkLocation();
    }
    else if(slide_id == "finish"){
      errors = errors + this.checkAllDates();
      if(errors == 0){
        this.createEvent();
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

  initLocationZones(){
    this.leasedZones = [];
    let choosen = {id: this.choosenLocationId};
    let self = this;
    if(choosen.id != ""){
        document.querySelector('div#zoneDisplay').innerHTML = "";

        var div = document.getElementById("zoneDisplay");
        var table = document.getElementById("zoneTable");
        table.innerHTML = ""

        var p = document.createElement("p");
        p.innerText = "Select location zones you want to lease for this event and define the ticket price per zone:";
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
           var table = document.getElementById("zoneTable");
           var tr = document.createElement("tr");
         
           var td1 = document.createElement("td");
           td1.style.width = "35%";
           td1.innerText = locationZone.name;
    
           var td2 = document.createElement("td");
           td2.style.width = "35%";
           td2.innerText = locationZone.capacity + "";
    
           var td3 = document.createElement("td");
           td3.style.width = "15%";
           td3.id="td"+locationZone.id;
    
           var td4 = document.createElement("td");
           td4.style.width = "15%";
           var checkbox = document.createElement("button");
           checkbox.className = "btn btn-primary";
           checkbox.innerText = "Select";
           checkbox.name = locationZone.id;
           checkbox.id = "checkbox"+locationZone.id;
           let zoneId = locationZone.id;
           checkbox.onclick = function(e){
            self.selectZoneButton(zoneId);
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

  selectZoneButton(zoneId){
    let self = this;
    let _id = zoneId;
    let button = document.getElementById("checkbox"+zoneId);
    if(button.textContent == "Select"){
      button.innerText = "Unselect";
      var zone : LeasedZoneDto = {id : "", zoneId : _id, maintenanceId: "", price : 0};
      self.leasedZones.push(zone);
      var priceInput = document.getElementById("td"+_id);
      var input = document.createElement("input");
      input.type = "number";
      input.name = _id;
      input.id = "input"+_id;
      input.className = "price_input";
      input.onchange = function(e){
        self.initPrice(_id);
      }
      
      input.style.width = '90%';
      priceInput.append(input);
    }
    else{
      button.innerText = "Select";
      var priceInput = document.getElementById("td"+_id);
      priceInput.innerHTML = "";
      self.leasedZones.forEach(function(data, index){
        if(data.zoneId == _id){
          self.leasedZones.splice(index,1);
        }
      });
    }
  }

  initPrice(id){
    let self = this;
    self.leasedZones.forEach(function(data){
      if(data.zoneId == id){
        var input = <HTMLInputElement> document.getElementById("input"+id);
        data.price = parseInt(input.value);
      }
    });
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
    let retval = 0;
    if(self.leasedZones.length > 0){
      self.leasedZones.forEach(function(data){
        if(data.price < 1 || data.price > 10000){
          var error = document.getElementById("error_message_location_info");
          error.innerText = self.invalidPriceError;
          retval += 1;
        }
      });
    }
    if(retval > 0){
      return 1;
    }
    else{
      return 0;
    }
  }

  checkLeasedZones(){
    let self = this;
    let retval = 0;
    if(self.leasedZones.length == 0){
      var error = document.getElementById("error_message_location_info");
      error.innerText = self.noLocationZoneError;
      retval = 1;
    }
    if(retval > 0){
      return 1;
    }
    else{
      return 0;
    }
  }

  addNewDay(){
    this.daysNum +=1;
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
      self.removeDay(self.daysNum+"");
    }
    div.append(close);

    var h4 = document.createElement("h4");
    h4.className = "form-signin-heading";
    h4.innerText = "Day "+ this.daysNum;
    div.append(h4);

    var p1 = document.createElement("p");
    p1.innerText = "Start date and time:";
    div.append(p1);

    var input1 = document.createElement("input");
    input1.type = "datetime-local";
    input1.id = "start"+this.daysNum;
    input1.className = "form-control";
    input1.name = "date_of_event";
    input1.onblur = function(){
      self.checkDate(self.daysNum);
    }
    div.append(input1);

    var br = document.createElement("br");
    div.append(br);

    var p2 = document.createElement("p");
    p2.innerText = "End date and time:";
    div.append(p2);

    var input2 = document.createElement("input");
    input2.type = "datetime-local";
    input2.id = "end"+this.daysNum;
    input2.className = "form-control";
    input2.name = "date_of_event";
    input2.onblur = function(){
      self.checkDate(self.daysNum);
    }
    div.append(input2);

    var erorDiv = document.createElement("div");
    erorDiv.style.color = "red";
    erorDiv.id = "error_day"+this.daysNum;
    div.append(erorDiv);

    mainDiv.appendChild(div);
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
    console.log(this.type);
    let self = this;
    self.maintenances.forEach(function(m){
      self.leasedZones.forEach(function(lz){
        m.locationZones.push(lz);
      })
    });
    var event : EventDto = {id:"0", name:self.name, description:"", eventType:self.type,
     maintenance:self.maintenances, locationZones:self.leasedZones, locationId:self.choosenLocationId};
     this.eventsService.create(event).subscribe(data => {
       console.log(data.header);
       console.log(data.message);
       this.sharedService.updateEvents();
     })
  }

  redirectToEvents(){
    this.router.navigate(['dashboard/events']);
  }

}
