import { TestBed, getTestBed } from "@angular/core/testing";
import { LocationsService } from './locations.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Reservation } from 'src/app/model/reservation.model';
import { Maintenance } from 'src/app/model/maintenance.model';
import { MaintenanceDto } from 'src/app/dto/maintenance.dto';
import { LeasedZoneDto } from 'src/app/dto/leased_zone.dto';
import { LocationZone } from 'src/app/model/location_zone.model';
import { Location } from 'src/app/model/location.model';
import { LocationZoneDto } from 'src/app/dto/location_zone.dto';
import { LocationReportDto } from 'src/app/dto/location_report.dto';

describe('LocationsService', () => {
    let injector: TestBed;
    let service: LocationsService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
          imports: [HttpClientTestingModule],
          providers: [LocationsService]
        });
    
        injector = getTestBed();
        service = injector.get(LocationsService);
        httpMock = injector.get(HttpTestingController);
      });
    
    it('should be created', () => {
        const service: LocationsService = TestBed.get(LocationsService);
        expect(service).toBeTruthy();
      });

      describe('#getAllLocations', () => {
        it('should be return an Observable<Location[]>', () => {
    
            const locationsMock = [
                { id: "1",name:"Name 1", address: 'Address 1', description: 'Description 1', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                { id: "2",name:"Name 2", address: 'Address 2', description: 'Description 2', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                { id: "3",name:"Name 3", address: 'Address 3', description: 'Description 3', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                ];
    
            service.getAll().subscribe(
            locations => {
                expect(locations.length).toBe(3);
                expect(locations).toEqual(locationsMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/getAllLocations`);
            expect(req.request.method).toBe('GET');
            req.flush(locationsMock);
    
        })
        });

        describe('#getActiveLocations', () => {
            it('should be return an Observable<Location[]>', () => {
        
                const locationsMock = [
                { id: "1",name:"Name 1", address: 'Address 1', description: 'Description 1', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                { id: "2",name:"Name 2", address: 'Address 2', description: 'Description 2', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                { id: "3",name:"Name 3", address: 'Address 3', description: 'Description 3', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()},
                ];
        
                service.getActive().subscribe(
                locations => {
                    expect(locations.length).toBe(3);
                    expect(locations).toEqual(locationsMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/getActiveLocations`);
                expect(req.request.method).toBe('GET');
                req.flush(locationsMock);
        
            })
        });
        
        describe('#getById', () => {
            it('should be return an Observable<Event>', () => {
            
                const locationMock = 
                { id: "1",name:"Name 1", address: 'Address 1', description: 'Description 1', status: true, events: new Set<Event>(), locationZones: new Set<LocationZone>()};
                    service.getById("1").subscribe(
                    location => {
                        expect(location).toEqual(locationMock);
                    }
                    );
                    const req = httpMock.expectOne(`/api/getLocation/1`);
                    expect(req.request.method).toBe('GET');
                    req.flush(locationMock);
            
                })
        });

        describe('#create', () => {
            it('should be return an Observable<MessageDto>', () => {
        
            const locationDto = 
               {id: "1", name: "Location 1", address: "Address 1", description: "Description 1", locationZone: new Array<LocationZoneDto>()
                } 
    
                const messageDtoMock = 
                {
                    message: "message created",
                    header: "header created"
                } 
        
                service.create(locationDto).subscribe(
                messageDto => {
                    expect(messageDto).toEqual(messageDtoMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/createLocation`);
                expect(req.request.method).toBe('POST');
                req.flush(messageDtoMock);
        
            })
        });


        describe('#update', () => {
            it('should be return an Observable<MessageDto>', () => {
        
                const locationDto = 
               {id: "1", name: "Location 1", address: "Address 1", description: "Description 1", locationZone: new Array<LocationZoneDto>()
                } 
    
                const messageDtoMock = 
                {
                    message: "message updated",
                    header: "header updated"
                } 
        
                service.update(locationDto).subscribe(
                messageDto => {
                    expect(messageDto).toEqual(messageDtoMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/updateLocation`);
                expect(req.request.method).toBe('POST');
                req.flush(messageDtoMock);
        
            })
        });


        describe('#delete', () => {
            it('should be return an Observable<MessageDto>', () => {
    
                const messageDtoMock = 
                {
                    message: "message deleted",
                    header: "header deleted"
                } 
        
                service.delete("1").subscribe(
                messageDto => {
                    expect(messageDto).toEqual(messageDtoMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/deleteLocation/1`);
                expect(req.request.method).toBe('DELETE');
                req.flush(messageDtoMock);
            })
        });


        describe('#seeReport', () => {
            it('should be return an Observable<LocationReportDto>', () => {
    
                const locationReportDtoMock = new LocationReportDto();
    
                locationReportDtoMock.dailyLabels = new Array<string>();
                locationReportDtoMock.dailyValues = new Array<number>();
                locationReportDtoMock.weeklyLabels = new Array<string>();
                locationReportDtoMock.weeklyValues = new Array<number>();
                locationReportDtoMock.monthlyLabels = new Array<string>();
                locationReportDtoMock.monthlyValues = new Array<number>();
        
                service.seeReport("1").subscribe(
                reportDto => {
                    expect(reportDto).toEqual(locationReportDtoMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/getLocationReport/1`);
                expect(req.request.method).toBe('GET');
                req.flush(locationReportDtoMock);
        
            })
        });

        describe('#checkNameAndAddressLocation', () => {
            it('should be return an Observable<MessageDto>', () => {
    
                const messageDtoMock = 
                {
                message: "available",
                header: "header available"
                } 
    
            service.checkNameAndAddress("name1","address1").subscribe(
            messageDto => {
                expect(messageDto).toEqual(messageDtoMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/checkIfNameAndAddressAvailable/name1/address1`);
            expect(req.request.method).toBe('GET');
            req.flush(messageDtoMock);
        
            })
        });




    
});