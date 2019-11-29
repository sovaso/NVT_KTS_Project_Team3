import { Reservation } from './reservation.model';
import { LeasedZone } from './leased_zone.model';

export interface Ticket{
    row: number;
    col: number;
    price: number;
    reserved: boolean;
    reservation: Reservation;
    zone: LeasedZone;
}