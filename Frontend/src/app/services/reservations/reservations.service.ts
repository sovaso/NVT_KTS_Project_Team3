import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventDto } from 'src/app/dto/event.dto';
import { MessageDto } from 'src/app/dto/message.dto';
import { EventReportDto } from 'src/app/dto/event_report.dto';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { LocationReportDto } from 'src/app/dto/location_report.dto';
import { LocationDto } from 'src/app/dto/location.dto';
import { Reservation } from 'src/app/model/reservation.model';
import { ReservationDTO } from 'src/app/dto/reservation.dto';


@Injectable({
  providedIn: "root"
})
export class ReservationsService {
  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }


  getByEventId(eventId: String): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(
      `/api/getEventReservations/${eventId}`
    );
  }

  createReservation(reservationDto: ReservationDTO):Observable<MessageDto>{
    return this.http.post<MessageDto>(
      '/api/createReservation',reservationDto);

  }

  getAllReservations(): Observable<Reservation[]>{
    return this.http.get<Reservation[]>(
      `/api/getLoggedUserReservations`
    );
  }

  cancelReservation(id: string): Observable<MessageDto>{
    return this.http.delete<MessageDto>(`/api/deleteReservation/${id}`);
  }


  

  

  
}
