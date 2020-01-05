import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventDto } from 'src/app/dto/event.dto';
import { MessageDto } from 'src/app/dto/message.dto';
import { EventReportDto } from 'src/app/dto/event_report.dto';
import { UploadFileDto } from 'src/app/dto/upload_file.dto';
import { LocationReportDto } from 'src/app/dto/location_report.dto';
import { LocationDto } from 'src/app/dto/location.dto';


@Injectable({
  providedIn: "root"
})
export class LocationsService {
  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }

  getAll(): Observable<Location[]> {
    return this.http.get<Location[]>(`http://localhost:8080/api/getAllLocations`);
  }

  getActive(): Observable<Location[]> {
    return this.http.get<Location[]>(`http://localhost:8080/api/getActiveLocations`);
  }

  getById(id: String): Observable<Location> {
    return this.http.get<Location>(
      `http://localhost:8080/api/getLocation/${id}`
    );
  }

  create(location: LocationDto): Observable<MessageDto> {
    return this.http.post<MessageDto>(`http://localhost:8080/api/createLocation`, location);
  }

  update = (location: LocationDto): Observable<MessageDto> =>
  this.http.put<MessageDto>(`http://localhost:8080/api/updateLocation`, location);

  delete = (id: string): Observable<MessageDto> =>{
    console.log('delete from service called');
    console.log(id);
    return this.http.delete<MessageDto>(`http://localhost:8080/api/deleteLocation/${id}`)
  }

  seeReport = (id: string): Observable<LocationReportDto> =>
  this.http.get<LocationReportDto>(`http://localhost:8080/api/getLocationReport/${ id }`);

  
  

  

  

  
}
