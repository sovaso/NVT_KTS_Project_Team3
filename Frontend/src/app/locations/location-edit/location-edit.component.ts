import { Component, OnInit,Input } from '@angular/core';
import { CurrentUser } from 'src/app/model/currentUser';
import { LocationsService } from 'src/app/services/locations/locations.service';
import { AlertService } from 'src/app/services';
import { NgbModal,NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from 'src/app/alert-box/alert-box.component';
import { Event } from 'src/app/model/event.model';
import {LocationZone} from  'src/app/model/location_zone.model';
import {Location} from  'src/app/model/location.model';
import {LocationDto} from 'src/app/dto/location.dto'
import { LocationZoneDto } from 'src/app/dto/location_zone.dto';
import { LocationZonesService } from 'src/app/services/location-zones/location-zones.service';
import { EventsService } from 'src/app/services/events/events.service';

@Component({
  selector: 'app-location-edit',
  templateUrl: './location-edit.component.html',
  styleUrls: ['./location-edit.component.css']
})
export class LocationEditComponent implements OnInit {
  loggedUser: CurrentUser;
  location: Location;
  events: Event[]=[];
  locationZones: LocationZone[]=[];
  newZone: boolean;
  newLocationZones: LocationZoneDto[]=[];
  numberOfLocationZones: number=0;


  constructor(private locationService: LocationsService,private alertService: AlertService,private modalService:NgbModal,private eventService: EventsService,private locationZonesService:LocationZonesService) { }




  ngOnInit() {
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));
    this.loadLocationZones();
  }



  saveLocationChanges(){
    if(this.location.name=="" || this.location.address=="" || this.location.description==""){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Name, address and description cannot be blank!';
    }else{
      let lz: LocationDto={
        id: this.location.id.toString(),
        name: this.location.name,
        address: this.location.address,
        description: this.location.description,
        locationZone: []
      }
      this.locationService.update(lz).subscribe(data => {
          const modalRef = this.modalService.open(AlertBoxComponent);
          modalRef.componentInstance.message=data.header;
        });

    }
    

  }

  loadLocationZones(){
    this.locationZonesService.getLocationZones(this.location.id).subscribe(data => {
      console.log('uslo u get reservatons');
      console.log(data.length);
      this.locationZones=data;
      console.log("Velicina location zones: ",this.locationZones.length)
    });
  }

  counter(i: number) {
    let listOfNums: number[]=[];
    let j: number=0;
    for(j;j<i;j++){
      listOfNums[j]=j+1;

    }
    return listOfNums;
}

  newLocationZone(){
    this.numberOfLocationZones+=1;
    console.log("Broj zona varijabla",this.numberOfLocationZones);
    let lz: LocationZoneDto={
        id:(this.numberOfLocationZones-1).toString(),
        locationId:this.location.id.toString(),
        name:'',
        row:0,
        col: 0,
        capacity: 0,
        matrix: false

    }
    this.newLocationZones.push(lz);
    this.newZone=true;
    
  }

  addLocationZone(){
    if(isNaN(this.newLocationZones[this.numberOfLocationZones-1].row) || isNaN(this.newLocationZones[this.numberOfLocationZones-1].col)){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and colomn number must be integers greater than 0!';
        
    }else if(this.newLocationZones[this.numberOfLocationZones-1].name==''){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Name of location zone is required!';

    }else if(this.newLocationZones[this.numberOfLocationZones-1].col==0 || this.newLocationZones[this.numberOfLocationZones-1].row==0){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and colomn number must be integers greater than 0!';
    }else{
      this.newLocationZones[this.numberOfLocationZones-1].capacity=this.newLocationZones[this.numberOfLocationZones-1].row*this.newLocationZones[this.numberOfLocationZones-1].col;
      this.newZone=false;
      this.locationZonesService.create(this.newLocationZones[this.numberOfLocationZones-1]).subscribe(data => {
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message=data.header;
        this.numberOfLocationZones-=1;
        this.newLocationZones.pop();
        this.loadLocationZones();
      });
  
    }
  }

  close(){
    this.modalService.dismissAll();
  }

  remove(id: string){
    if(this.locationZones.length==1){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message="This location contains only one location zone, to remove this one firstly add new one!";
    }else{
      this.locationZonesService.delete(id).subscribe(data => {
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message=data.header;
        this.loadLocationZones();
      });
    }
  }

  updateLocationZone(location: LocationZone){

    if(location.name==""){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Name cannot be blank!';
      this.loadLocationZones();
    }else if(isNaN(location.rowNumber) || isNaN(location.colNumber)){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and column numbers must be numbers greater than 0!';
      this.loadLocationZones();
    }
    else if(location.rowNumber<=0 || location.colNumber<=0){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and column numbers must be greater than 0!';
      this.loadLocationZones();
    }
    else{
      let lz: LocationZoneDto={
        id:location.id,
        locationId:this.location.id,
        name:location.name,
        row:location.rowNumber,
        col: location.colNumber,
        capacity: location.rowNumber*location.colNumber,
        matrix: location.matrix
      }
      this.locationZonesService.update(lz).subscribe(data => {
          const modalRef = this.modalService.open(AlertBoxComponent);
          modalRef.componentInstance.message=data.header;
          this.loadLocationZones();
        });

    }

  }

}
