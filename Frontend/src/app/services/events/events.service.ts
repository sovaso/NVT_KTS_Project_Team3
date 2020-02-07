import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventDto } from 'src/app/dto/event.dto';
import { MessageDto } from 'src/app/dto/message.dto';
import { EventReportDto } from 'src/app/dto/event_report.dto';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { Event } from 'src/app/model/event.model';
import { Media } from 'src/app/model/media.model';


@Injectable({
  providedIn: "root"
})
export class EventsService {
  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }

  getAll(): Observable<Event[]> {
    return this.http.get<Event[]>(`/api/getAllEvents`);
  }

  getActive(): Observable<Event[]> {
    return this.http.get<Event[]>(`/api/getActiveEvents`);
  }

  getById(id: String): Observable<Event> {
    return this.http.get<Event>(
      `/api/getEvent/${id}`
    );
  }

  create(event: EventDto): Observable<MessageDto> {
    var retval = this.http.post<MessageDto>(`/api/createEvent`, event);
    return retval;
  }

  update = (event: EventDto): Observable<MessageDto> =>
  this.http.put<MessageDto>(`/api/updateEvent`, event);

  delete = (id: string): Observable<MessageDto> =>{
  
    return this.http.delete<MessageDto>(`/api/deleteEvent/${id}`);
  }

  getEventLocation = (id: string): Observable<Location> =>
  this.http.get<Location>(`/api/getEventLocation/${ id }`);

  getEventIncome = (id: string): Observable<number> =>
  this.http.get<number>(`/api/getEventIncome/${ id }`);

  seeReport = (id: string): Observable<EventReportDto> =>
  this.http.get<EventReportDto>(`/api/getEventReport/${ id }`);

  search = (field: string, startDate: string, endDate: string): Observable<any> =>
    
 this.http.get<any>(`/api/findEvent/${field}/${startDate}/${endDate}`);
  

  sortByName = (): Observable<any> =>
  this.http.get<any>(`/api/sortByName`);

  sortByDateAcs = (): Observable<any> =>
  this.http.get<any>(`/api/sortByDateAcs`);

  sortByDateDesc = (): Observable<any> =>
  this.http.get<any>(`/api/sortByDateDesc`);

  getIncome = (id:string): Observable<any> =>
  this.http.get<any>(`/api/getEventIncome/${id}`);

  uploadFile(file: File, id:string): Observable<String[]> {

    const data: FormData = new FormData();
    data.append('file', file);
    var retval = this.http.post<String[]>(`/api/upload/${id}`,data,{
      reportProgress: true,
      responseType: 'json'
      });
    return retval;
  }

  getAllMedia = (id:string): Observable<Media[]> =>
  this.http.get<Media[]>(`/api/getMedia/${id}`);

}
