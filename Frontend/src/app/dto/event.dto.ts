import { Maintenance } from '../model/maintenance.model';
import { LeasedZoneDto } from './leased_zone.dto';
import { MaintenanceDto } from './maintenance.dto';
    
export interface EventDto{
    id: string;
    name : string;
    description: string;
    eventType: string;
    maintenance: Array<MaintenanceDto>;
    locationZones: Array<LeasedZoneDto>;
    locationId: string;
}