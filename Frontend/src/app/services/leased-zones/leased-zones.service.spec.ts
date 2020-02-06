import { TestBed } from '@angular/core/testing';

import { LeasedZonesService } from './leased-zones.service';

describe('LeasedZonesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LeasedZonesService = TestBed.get(LeasedZonesService);
    expect(service).toBeTruthy();
  });
});
