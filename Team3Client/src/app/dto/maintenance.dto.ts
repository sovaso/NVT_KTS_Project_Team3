import { LeasedZone } from '../model/leased_zone.model';
import { LeasedZoneDto } from './leased_zone.dto';

export interface MaintenanceDto{
    startDate: string;
    endDate: string;
    id: string;
    eventId: string;
    locationZones : Array<LeasedZoneDto>
}