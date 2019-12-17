import { logging } from 'protractor';
import { UserModel } from './user.model';
import { Ticket } from './ticket.model';
import { Event } from './event.model';
export interface Reservation{
    id: number;
    dateOfReservation: number;
    paid: boolean;
    totalPrice: number;
    version: number;
    user: UserModel;
    reservedTickets: Set<Ticket>;
    event: Event;
}