import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventDto } from 'src/app/dto/event.dto';
import { MessageDto } from 'src/app/dto/message.dto';
import { EventReportDto } from 'src/app/dto/event_report.dto';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { Event } from 'src/app/model/event.model';


@Injectable({
  providedIn: "root"
})
export class EventsService {
  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }

  getAll(): Observable<Event[]> {
    return this.http.get<Event[]>(`http://localhost:8080/api/getAllEvents`);
  }

  getActive(): Observable<Event[]> {
    return this.http.get<Event[]>(`http://localhost:8080/api/getActiveEvents`);
  }

  getById(id: String): Observable<Event> {
    return this.http.get<Event>(
      `http://localhost:8080/api/getEvent/${id}`
    );
  }

  create(event: EventDto): Observable<MessageDto> {
    return this.http.post<MessageDto>(`http://localhost:8080/api/createEvent`, event);
  }

  update = (event: EventDto): Observable<MessageDto> =>
  this.http.put<MessageDto>(`http://localhost:8080/api/updateEvent`, event);

  delete = (id: string): Observable<MessageDto> =>{
    console.log('delete from service called');
    console.log(id);
  return this.http.delete<MessageDto>(`http://localhost:8080/api/deleteEvent/${id}`);
  }


  getEventLocation = (id: string): Observable<Location> =>
  this.http.get<Location>(`http://localhost:8080/api/getEventLocation/${ id }`);

  getEventIncome = (id: string): Observable<number> =>
  this.http.get<number>(`http://localhost:8080/api/getEventIncome/${ id }`);

  getEventReport = (id: string): Observable<EventReportDto> =>
  this.http.get<EventReportDto>(`http://localhost:8080/api/getEventReport/${ id }`);

  uploadFile = (uploadFile: UploadFileDto): Observable<string> =>
  this.http.post<string>(`http://localhost:8080/api/upload`, uploadFile);

  search = (field: string, startDate: string, endDate: string): Observable<any> =>
    
 this.http.get<any>(`http://localhost:8080/api/findEvent/${field}/${startDate}/${endDate}`);
  

  sortByName = (): Observable<any> =>
  this.http.get<any>(`http://localhost:8080/api/sortByName`);

  sortByDateAcs = (): Observable<any> =>
  this.http.get<any>(`http://localhost:8080/api/sortByDateAcs`);

  sortByDateDesc = (): Observable<any> =>
  this.http.get<any>(`http://localhost:8080/api/sortByDateDesc`);
}
