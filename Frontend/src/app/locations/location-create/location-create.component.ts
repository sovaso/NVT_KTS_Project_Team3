import { Component, OnInit,Input } from '@angular/core';
import { CurrentUser } from 'src/app/model/currentUser';
import { SharedService } from 'src/app/services/shared/shared.service';
import { LocationsService } from 'src/app/services/locations/locations.service';
import { AlertService } from 'src/app/services';
import { NgbModal,NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from 'src/app/alert-box/alert-box.component';
import { Event } from 'src/app/model/event.model';
import {LocationZone} from  'src/app/model/location_zone.model'
import {LocationDto} from 'src/app/dto/location.dto'
import { LocationZoneDto } from 'src/app/dto/location_zone.dto';

@Component({
  selector: 'app-location-create',
  templateUrl: './location-create.component.html',
  styleUrls: ['./location-create.component.css']
})
export class LocationCreateComponent implements OnInit {
  name: string='';
  address: string='';
  description: string='';
  status: boolean=false;
  locationZones: LocationZoneDto[]=[];
  newZone: boolean=false;
  lzname: string='';
  rowNumber: number=0;
  colNumber: number=0;
  matrix: boolean=false;
  capacity: number=0;
  location: LocationDto;
  numOfLocationZones:number=0;
  isModelShow: boolean=false;
  done: number=0;

  ngOnInit() {
      this.locationZones=[];
      this.location={
        id:'',
        name:'',
        address:'',
        description:'',
        locationZone:[]


      };
  }

  constructor(private modalService: NgbModal, private alertService: AlertService, private sharedService: SharedService, private locationsService: LocationsService,private activeModal:NgbActiveModal){};
  checkNameAndAdress(){
    this.sharedService.updateAll();
    this.locationsService.checkNameAndAddress(this.location.name,this.location.address).subscribe(data => {
      if(data.message=="Location exists"){
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message="Location with this name and address already exists!";
      }else{
         this.createLocation2();
      }
  });
  }

  createLocation(){
    if(this.location.name=='' || this.location.address=='' || this.location.description==''){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message="Location name,address and description cannot be blank!";

    }
    this.checkNameAndAdress();
  }

  createLocation2(){
      this.newZone=false;
      let k:number=0;
      for(k;k<this.locationZones.length;k++){
        if(this.locationZones[k].name==''|| this.locationZones[k].capacity==0 || this.locationZones[k].row==0 || this.locationZones[k].col==0){
          this.numOfLocationZones-=1;
          this.locationZones.splice(k,1);
        }
        
      }
      if(this.locationZones.length==0){
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message="Must have at least one location zone!";
      }else{
      this.location.locationZone=this.locationZones;
      this.locationsService.create(this.location).subscribe(data => {
        console.log(data);
        this.sharedService.updateAll();
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message=data.header;
        this.activeModal.dismiss();
    });
  }
}



  

  addLocationZone(){
    if(isNaN(this.locationZones[this.numOfLocationZones-1].row) || isNaN(this.locationZones[this.numOfLocationZones-1].col)){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and colomn number must be integers greater than 0!';
      this.locationZones.pop();
      this.numOfLocationZones-=1;
      if(this.numOfLocationZones==0){
          this.newLocationZone(); 
        
      }
    }else if(this.locationZones[this.numOfLocationZones-1].name==''){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Name of location zone is required!';
      this.locationZones.pop();
      this.numOfLocationZones-=1;
      if(this.numOfLocationZones==0){
        this.newLocationZone(); 
      }

    }else if(this.locationZones[this.numOfLocationZones-1].col==0 || this.locationZones[this.numOfLocationZones-1].row==0){
      const modalRef = this.modalService.open(AlertBoxComponent);
      modalRef.componentInstance.message='Row and colomn number must be integers greater than 0!';
      this.locationZones.pop();
      this.numOfLocationZones-=1;
      if(this.numOfLocationZones==0){
        this.newLocationZone(); 
      }
      
    }else{
    this.locationZones[this.numOfLocationZones-1].capacity=this.locationZones[this.numOfLocationZones-1].row*this.locationZones[this.numOfLocationZones-1].col;
    this.newZone=false;
  
    }
  }

  newLocationZone(){
    this.numOfLocationZones+=1;
    console.log("Broj zona varijabla",this.numOfLocationZones);
    let lz: LocationZoneDto={
        id:(this.numOfLocationZones-1).toString(),
        locationId:'',
        name:'',
        row:0,
        col: 0,
        capacity: 0,
        matrix: false

    }
    this.locationZones.push(lz);
    console.log("Broj kategorinja",this.locationZones.length)
    console.log("Broj redova prve: ",this.locationZones[0].row)
    this.newZone=true;
    
  }

  counter(i: number) {
    let listOfNums: number[]=[];
    let j: number=0;
    for(j;j<i;j++){
      listOfNums[j]=j+1;

    }
    return listOfNums;
}

close(){
  this.modalService.dismissAll();
}

remove(i:string){
  let k: number=0;
  let a: number=parseInt(i);
  for(k;k<=this.locationZones.length;k++){
    if(this.locationZones[k].id==i){
      this.locationZones.splice(k,1);
      break;
    }
  }
  this.numOfLocationZones-=1;
  if(this.numOfLocationZones==0){
    this.newLocationZone(); 
  }
}

}
