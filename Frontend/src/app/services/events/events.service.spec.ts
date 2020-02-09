import { TestBed, getTestBed } from "@angular/core/testing";
import { EventsService } from './events.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Reservation } from 'src/app/model/reservation.model';
import { Maintenance } from 'src/app/model/maintenance.model';
import { MaintenanceDto } from 'src/app/dto/maintenance.dto';
import { LeasedZoneDto } from 'src/app/dto/leased_zone.dto';
import { LocationZone } from 'src/app/model/location_zone.model';
import { Location } from 'src/app/model/location.model';
import { EventReportDto } from 'src/app/dto/event_report.dto';


describe('EventsService', () => {
    let injector: TestBed;
    let service: EventsService;
    let httpMock: HttpTestingController;
  
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [EventsService]
      });
  
      injector = getTestBed();
      service = injector.get(EventsService);
      httpMock = injector.get(HttpTestingController);
    });
  
    it('should be created', () => {
        const service: EventsService = TestBed.get(EventsService);
        expect(service).toBeTruthy();
      });
      
    describe('#getAllEvents', () => {
    it('should be return an Observable<Event[]>', () => {

        const eventsMock = [
        { id: "1", name: 'Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
        { id: "2", name: 'Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
        { id: "3", name: 'Event 3', status: false, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
        ];

        service.getAll().subscribe(
        events => {
            expect(events.length).toBe(3);
            expect(events).toEqual(eventsMock);
        }
        );

        const req = httpMock.expectOne(`/api/getAllEvents`);
        expect(req.request.method).toBe('GET');
        req.flush(eventsMock);

    })
    });


    describe('#getActiveEvents', () => {
        it('should be return an Observable<Event[]>', () => {
    
            const eventsMock = [
            { id: "1", name: 'Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
            { id: "2", name: 'Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
            { id: "3", name: 'Event 3', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
            ];
    
            service.getActive().subscribe(
            events => {
                expect(events.length).toBe(3);
                expect(events).toEqual(eventsMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/getActiveEvents`);
            expect(req.request.method).toBe('GET');
            req.flush(eventsMock);
    
        })
        });

    describe('#getById', () => {
        it('should be return an Observable<Event>', () => {
    
            const eventMock = 
            { id: "1", name: 'Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() };
            
    
            service.getById("1").subscribe(
            event => {
                expect(event).toEqual(eventMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/getEvent/1`);
            expect(req.request.method).toBe('GET');
            req.flush(eventMock);
    
        })
        });

    describe('#create', () => {
        it('should be return an Observable<MessageDto>', () => {
    
            const eventDto = 
           {id: "1", name: "Event 1", description: "Description 1", eventType: "Sports", maintenance: new Array<MaintenanceDto>(),
            locationZones: new Array<LeasedZoneDto>(), locationId: "1"
            } 

            const messageDtoMock = 
            {
                message: "message created",
                header: "header created"
            } 
    
            service.create(eventDto).subscribe(
            messageDto => {
                expect(messageDto).toEqual(messageDtoMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/createEvent`);
            expect(req.request.method).toBe('POST');
            req.flush(messageDtoMock);
    
        })
        });


    describe('#update', () => {
        it('should be return an Observable<MessageDto>', () => {
    
            const eventDto = 
            {id: "1", name: "Event 1", description: "Description 1", eventType: "Sports", maintenance: new Array<MaintenanceDto>(),
            locationZones: new Array<LeasedZoneDto>(), locationId: "1"
            } 

            const messageDtoMock = 
            {
                message: "message updated",
                header: "header updated"
            } 
    
            service.update(eventDto).subscribe(
            messageDto => {
                expect(messageDto).toEqual(messageDtoMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/updateEvent`);
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
    
            const req = httpMock.expectOne(`/api/deleteEvent/1`);
            expect(req.request.method).toBe('DELETE');
            req.flush(messageDtoMock);
    
        })
        });

    describe('#getEventIncome', () => {
        it('should be return an Observable<number>', () => {

            const incomeMock = 1000;
    
            service.getEventIncome("1").subscribe(
            income => {
                expect(income).toEqual(incomeMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/getEventIncome/1`);
            expect(req.request.method).toBe('GET');
            req.flush(incomeMock);
    
        })
        });

    describe('#seeReport', () => {
        it('should be return an Observable<number>', () => {

            const eventReportDtoMock = new EventReportDto();

            eventReportDtoMock.dailyLabels = new Array<string>();
            eventReportDtoMock.dailyValues = new Array<number>();
            eventReportDtoMock.weeklyLabels = new Array<string>();
            eventReportDtoMock.weeklyValues = new Array<number>();
            eventReportDtoMock.monthlyLabels = new Array<string>();
            eventReportDtoMock.monthlyValues = new Array<number>();
    
            service.seeReport("1").subscribe(
            reportDto => {
                expect(reportDto).toEqual(eventReportDtoMock);
            }
            );
    
            const req = httpMock.expectOne(`/api/getEventReport/1`);
            expect(req.request.method).toBe('GET');
            req.flush(eventReportDtoMock);
    
        })
        });
/*
    describe('#uploadFile', () => {
        it('should be return an Observable<number>', () => {

            const uploadFileDto = {
                name: "name",
                pathToFile: "path"
            }

            let response = "successfull upload";

            service.uploadFile(uploadFileDto).subscribe(
            result => {
                expect(result).toEqual(response);
            }
            );
    
            const req = httpMock.expectOne(`/api/upload`);
            expect(req.request.method).toBe('POST');
            req.flush(response);
    
        })
        });
*/
        describe('#search', () => {
            it('should be return an Observable<any>', () => {
        
                const eventsMock = [
                { id: "1", name: 'Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "2", name: 'Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "3", name: 'Event 3', status: false, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                ];
        
                service.search("sports", "2020-01-01", "2020-12-12").subscribe(
                events => {
                    expect(events.length).toBe(3);
                    expect(events).toEqual(eventsMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/findEvent/sports/2020-01-01/2020-12-12`);
                expect(req.request.method).toBe('GET');
                req.flush(eventsMock);
        
            })
            });

        describe('#sortByName', () => {
            it('should be return an Observable<any>', () => {
        
                const eventsMock = [
                { id: "1", name: 'A Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "2", name: 'B Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "3", name: 'C Event 3', status: false, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                ];
        
                service.sortByName().subscribe(
                events => {
                    expect(events.length).toBe(3);
                    expect(events).toEqual(eventsMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/sortByName`);
                expect(req.request.method).toBe('GET');
                req.flush(eventsMock);
        
            })
            });

        describe('#sortByDateAcs', () => {
            it('should be return an Observable<any>', () => {
        
                const eventsMock = [
                { id: "1", name: 'A Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "2", name: 'B Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "3", name: 'C Event 3', status: false, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                ];
        
                service.sortByDateAcs().subscribe(
                events => {
                    expect(events.length).toBe(3);
                    expect(events).toEqual(eventsMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/sortByDateAcs`);
                expect(req.request.method).toBe('GET');
                req.flush(eventsMock);
        
            })
            });

        describe('#sortByDateDesc', () => {
            it('should be return an Observable<any>', () => {
        
                const eventsMock = [
                { id: "1", name: 'A Event 1', status: true, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "2", name: 'B Event 2', status: true, type: 'Cultural', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                { id: "3", name: 'C Event 3', status: false, type: 'Sports', locationInfo: null, reservations: new Set<Reservation>(), maintenances: new Set<Maintenance>() },
                ];
        
                service.sortByDateDesc().subscribe(
                events => {
                    expect(events.length).toBe(3);
                    expect(events).toEqual(eventsMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/sortByDateDesc`);
                expect(req.request.method).toBe('GET');
                req.flush(eventsMock);
        
            })
            });

        describe('#getIncome', () => {
            it('should be return an Observable<any>', () => {
    
                const incomeMock = 1000;
        
                service.getIncome("1").subscribe(
                income => {
                    expect(income).toEqual(incomeMock);
                }
                );
        
                const req = httpMock.expectOne(`/api/getEventIncome/1`);
                expect(req.request.method).toBe('GET');
                req.flush(incomeMock);
        
            })
            });
/*
    describe('#getEventLocation', () => {
        it('should be return an Observable<Location>', () => {

            let locationMock = new Location("1");
            locationMock.name = "Location1",
            locationMock.address = "Address1",
            locationMock.description = "Description",
            locationMock.status=true;
            locationMock.events = new Set<Event>();
            locationMock.locationZones = new Set<LocationZone>();

            
            {
                id: "1",
                name: "Location 1",
                address: "Address 1",
                description: "Description 1",
                status: true,
                events: new Set<Event>(),
                locationZones: new Set<LocationZone>()
            } 
            
    
            service.getEventLocation("1").subscribe(
                loc => {
                    expect(loc).toEqual(locationMock);
                }
            );
        
    
            const req = httpMock.expectOne(`/api/getEventLocation/1`);
            expect(req.request.method).toBe('GET');
            req.flush(locationMock);
    
        })
        });

        */
        
  });
  