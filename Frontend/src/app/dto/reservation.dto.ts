import { TicketDTO } from './ticket.dto';
import {Event} from '../model/event.model';

export interface ReservationDTO{
    event: Event;
    tickets: TicketDTO[];
    qrCode: String;
}