import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { MessageDto } from 'src/app/dto/message.dto';
import { LocationZone } from 'src/app/model/location_zone.model';
import { LocationZoneDto } from 'src/app/dto/location_zone.dto';

@Injectable({
  providedIn: "root"
})
export class LocationZonesService {
  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }

  getLocationZones(id: String): Observable<LocationZone[]> {
    return this.http.get<LocationZone[]>(`/api/getLocationZones/${id}`);
  }

  create(location: LocationZoneDto): Observable<MessageDto> {
    return this.http.post<MessageDto>(`/api/createLocationZone`, location);
  }


  delete = (id: string): Observable<MessageDto> =>{
    return this.http.delete<MessageDto>(`/api/deleteLocationZone/${id}`)
  }

  update = (location: LocationZoneDto): Observable<MessageDto> =>
  this.http.post<MessageDto>(`/api/updateLocationZone`, location);
}
