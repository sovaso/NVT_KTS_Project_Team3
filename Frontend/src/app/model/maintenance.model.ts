import { LeasedZone } from './leased_zone.model';

export interface Maintenance{
    id: string;
    maintenanceDate: Date;
    maintenanceEndTime: Date;
    reservationExpiry: Date;
    leasedZones: Set<LeasedZone>;
    event: Event;
}