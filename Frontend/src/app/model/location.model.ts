import { EventsComponent } from '../events/events.component';
import { LocationsComponent } from '../locations/locations.component';


export interface Location {
    id: string;
    name: string;
    address: string;
    description: string;
    status: boolean;
    events: Set<EventsComponent>;
    locationZones: Set<LocationsComponent>;
 }