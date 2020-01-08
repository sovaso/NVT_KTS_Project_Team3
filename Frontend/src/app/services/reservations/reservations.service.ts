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


  

  

  
}
