import { Reservation } from './reservation.model';
import { Maintenance } from './maintenance.model';

export class Event {
    id: string;
    name: string;
    status: boolean;
    type: string;
    locationInfo: Location;
    reservations:  Set<Reservation>;
    maintenances: Set<Maintenance>;

   

    constructor(id: string){
        id = id;
    }
 }