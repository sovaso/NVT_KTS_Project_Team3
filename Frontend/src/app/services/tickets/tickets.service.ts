import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventDto } from 'src/app/dto/event.dto';
import { MessageDto } from 'src/app/dto/message.dto';
import { EventReportDto } from 'src/app/dto/event_report.dto';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { Event } from 'src/app/model/event.model';
import { Media } from 'src/app/model/media.model';
import { map } from 'rxjs/operators';
import { Ticket } from 'src/app/model/ticket.model';
import { QRCodeDTO } from 'src/app/dto/QRCode.dto';


@Injectable({
  providedIn: "root"
})

export class TicketsService {
    private headers = new HttpHeaders({ "Content-Type": "application/json" });
  
    constructor(private http: HttpClient) { }
  
    getTickets(id: String): Observable<Ticket[]> {
      return this.http.get<Ticket[]>(`/api/getLeasedZoneTickets/${id}`);
    }

    getReservationTickets(id:String): Observable<Ticket[]>{
      return this.http.get<Ticket[]>(`/api/getReservationTickets/${id}`);
    }


    getQRCode(id: String): Observable<QRCodeDTO>{
      return this.http.get<QRCodeDTO>(`/api/getQRCodeImage/${id}`);
    }

}
  