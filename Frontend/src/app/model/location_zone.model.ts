import { LeasedZone } from './leased_zone.model';

export interface LocationZone{
    id: string;
    rowNumber: number;
    name: string;
    capacity: number;
    matrix: boolean;
    colNumber: number;
    leasedZone: Set<LeasedZone>;
    location: Location;
}