import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { EventsService } from '../events/events.service';
import { LocationsService } from '../locations/locations.service';


import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  public status : boolean = false;
  private eventsSource = new BehaviorSubject<Event[]>([]);
  events = this.eventsSource.asObservable();

  private locationSource = new BehaviorSubject<Location[]>([]);
  locations = this.locationSource.asObservable();
  
  constructor(private eventService: EventsService, private locationService: LocationsService) { }

  updateEvents() {
    this.eventService.getAll().subscribe(data => {
      this.eventsSource.next(data);
    });
  }

  updateLocation() {
    this.locationService.getAll().subscribe(data => {
      this.locationSource.next(data);
    });
  }

  updateAll() {
    this.updateEvents();
    this.updateLocation();
  }

  sortEventsByDateAcs(){
    this.eventService.sortByDateAcs().subscribe(data => {
      this.eventsSource.next(data);
    });
  }
  sortEventsByDateDesc(){
    this.eventService.sortByDateDesc().subscribe(data => {
      this.eventsSource.next(data);
    });
  }

  sortEventsByName(){
    this.eventService.sortByName().subscribe(data => {
      this.eventsSource.next(data);
    });
  }

  searchEvents(field : string, startDate : string, endDate : string) {
    this.eventService.search(field, startDate,endDate).subscribe(data => {
      this.eventsSource.next(data);
    }, (err : Error)=>{
     console.log('error');
     this.status=false;
    }
      
    );
  }

  

}
