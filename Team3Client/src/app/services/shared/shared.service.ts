import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { EventsService } from '../events/events.service';
import { LocationsService } from '../locations/locations.service';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

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

}
