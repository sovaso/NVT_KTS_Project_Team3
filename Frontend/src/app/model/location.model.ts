
import { LocationsComponent } from '../locations/locations.component';
import { LocationZone } from './location_zone.model';
import { Event } from './event.model';


export class Location {
    id: string;
    name: string;
    address: string;
    description: string;
    status: boolean;
    events: Set<Event>;
    locationZones: Set<LocationZone>;

    constructor(id: String){
        id = id;
    }
 }