export interface LeasedZoneUpdatableDto{
    id: string;
    zoneId: string;
    maintenanceId: string;
    price: number;
    updatable : boolean;
}