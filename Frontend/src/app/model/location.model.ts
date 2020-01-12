import { EventsComponent } from '../events/events.component';
import { LocationsComponent } from '../locations/locations.component';
import { LocationZone } from './location_zone.model';


export interface Location {
    id: string;
    name: string;
    address: string;
    description: string;
    status: boolean;
    events: Set<Event>;
    locationZones: Set<LocationZone>;
 }