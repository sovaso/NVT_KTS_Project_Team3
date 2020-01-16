import { TestBed, inject } from '@angular/core/testing';
import { AuthenticationService } from './authentication-service.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { JwtUtilsService } from './jwt-utils.service';



describe('AuthenticationServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:  [HttpClientTestingModule],
      providers: [AuthenticationService,
        {provide: JwtUtilsService, useClass: JwtUtilsServiceMock},
      ]
    });
  });

  it('should be created', inject([AuthenticationService], (service: AuthenticationService) => {
    expect(service).toBeTruthy();
  }));
});

class JwtUtilsServiceMock{
  
}