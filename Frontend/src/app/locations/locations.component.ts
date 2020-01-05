import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../model/currentUser';
import { SharedService } from '../services/shared/shared.service';
import { LocationsService } from '../services/locations/locations.service';
import { AlertService } from '../services';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertBoxComponent } from '../alert-box/alert-box.component';
import { LocationDetailsComponent } from './location-details/location-details.component';
import { Event } from '../model/event.model';
import { LocationReportComponent } from './location-report/location-report.component';

@Component({
  selector: 'app-locations',
  templateUrl: './locations.component.html',
  styleUrls: ['./locations.component.css']
})
export class LocationsComponent implements OnInit {

  locations: Location[];

  activeTab: String;

  field: string='';

  loggedUser: CurrentUser;

  startDate: string='';

  endDate: string='';

  message: String = '';

  type = '';

  constructor(private modalService: NgbModal, private alertService: AlertService, private sharedService: SharedService, private locationsService: LocationsService) {}

  ngOnInit() {
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));

    this.sharedService.locations.subscribe(
      locations => (this.locations = locations)
    );

   
    if (this.locations.length===0) {
      this.sharedService.updateAll();
    }
  }
  onTabChange($event: any) {
    this.activeTab = $event.nextId;
  }

  deleteLocation(location){
    console.log('delete location called');
    console.log(location.id);
    this.locationsService.delete(location.id).subscribe(data => {
        console.log(data);
        this.sharedService.updateAll();
        const modalRef = this.modalService.open(AlertBoxComponent);
        modalRef.componentInstance.message=data.header;
    }
    );
  }

  seeReport(event){
    this.locationsService.seeReport(event.id).subscribe(data => {
        const modalRef = this.modalService.open(LocationReportComponent);
        modalRef.componentInstance.data = data;
  });
  }

  // showDetails(e){
  //   console.log('show details called');
  //   console.log(e.id);
  //   this.locationsService.getById(e.id).subscribe(data => {
  //     const modalRef = this.modalService.open(LocationDetailsComponent);
  //     modalRef.componentInstance.location = data;
  //   });
  // }


}
