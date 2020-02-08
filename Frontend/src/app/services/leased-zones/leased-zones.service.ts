import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { MessageDto } from 'src/app/dto/message.dto';
import { LeasedZone } from 'src/app/model/leased_zone.model';
import { LeasedZoneDto } from 'src/app/dto/leased_zone.dto';
import { LeasedZoneUpdatableDto } from 'src/app/dto/leased_zone_updatable.dto';

@Injectable({
  providedIn: 'root'
})
export class LeasedZonesService {

  private headers = new HttpHeaders({ "Content-Type": "application/json" });

  constructor(private http: HttpClient) { }

  getEventLeasedZones(eventId: String): Observable<LeasedZone[]>{
    return this.http.get<LeasedZone[]>(`/api/getEventLeasedZones/${eventId}`);
  }

  getMaintenanceLeasedZones(maintenanceId: String): Observable<LeasedZone[]>{
    return this.http.get<LeasedZone[]>(`/api/getLeasedZones/${maintenanceId}`);
  }

  getEventLeasedZonesUpdatable(eventId: String): Observable<LeasedZoneUpdatableDto[]>{
    return this.http.get<LeasedZoneUpdatableDto[]>(`/api/getEventLeasedZonesDto/${eventId}`);
  }

  updateLeasedZone(lz: LeasedZoneDto): Observable<any>{
    return this.http.post<any>(`/api/updateLeasedZone`,lz);
  }

  createLeasedZone(lz: LeasedZoneDto): Observable<any>{
    return this.http.post<any>(`/api/createLeasedZone`,lz);
  }

  removeLeasedZone(id: string): Observable<MessageDto>{
    return this.http.delete<MessageDto>(`/api/deleteLeasedZone/${id}`);
  }

}