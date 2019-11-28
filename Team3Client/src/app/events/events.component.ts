import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../model/currentUser';
import { SharedService } from '../services/shared/shared.service';
import { EventsService } from '../services/events/events.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  events: Event[];
  locations: Location[];

  activeTab: String;

  loggedUser: CurrentUser;

  constructor(private sharedService: SharedService, private eventsService: EventsService) {}

  ngOnInit() {
    this.loggedUser = JSON.parse(localStorage.getItem("currentUser"));

    this.sharedService.events.subscribe(
      events => (this.events = events)
    );

    this.sharedService.locations.subscribe(locations => (this.locations = locations));
   
    if (this.events.length === 0 || this.locations.length===0) {
      this.sharedService.updateAll();
    }
  }
  onTabChange($event: any) {
    this.activeTab = $event.nextId;
  }

  deleteEvent(event){
    console.log('delete event called');
    console.log(event.id);
    this.eventsService.delete(event.id).subscribe(data => {
        console.log(data);
        this.sharedService.updateAll();
    }
    );
    
  }



  
}

