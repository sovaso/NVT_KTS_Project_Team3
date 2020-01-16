import { TestBed, async, inject } from '@angular/core/testing';

import { CanActivateAuthGuard } from './can-activate-auth.guard';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthenticationService } from './authentication-service.service';

describe('CanActivateAuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [CanActivateAuthGuard,
        {provide: AuthenticationService, useClass: AuthenticationServiceMock},
      ]
    });
  });

  it('should ...', inject([CanActivateAuthGuard], (guard: CanActivateAuthGuard) => {
    expect(guard).toBeTruthy();
  }));
});

class AuthenticationServiceMock {
  
}