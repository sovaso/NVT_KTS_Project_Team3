import { LocationZone } from './location_zone.model';
import { Maintenance } from './maintenance.model';
import { Ticket } from './ticket.model';

export interface LeasedZone{
    id: string;
    seatPrice: number;
    zone: LocationZone;
    maintenance: Maintenance;
    tickets: Set<Ticket>;
}