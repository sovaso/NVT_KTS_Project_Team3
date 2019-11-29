import { LocationZoneDto } from './location_zone.dto';

export interface LocationDto{
    id: string;
    name: string;
    address: string;
    description: string;
    locationZone: Array<LocationZoneDto>
}