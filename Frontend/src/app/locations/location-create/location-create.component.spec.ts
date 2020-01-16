import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationCreateComponent } from './location-create.component';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { LocationsService } from 'src/app/services/locations/locations.service';
import { AlertService } from 'src/app/services';
import { SharedService } from 'src/app/services/shared/shared.service';

describe('LocationCreateComponent', () => {
  let component: LocationCreateComponent;
  let fixture: ComponentFixture<LocationCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationCreateComponent ],
      imports: [NgbModule, FormsModule ],
      providers: [
        {provide: LocationsService, useClass: LocationServiceMock},
        {provide: AlertService, useClass: AlertServiceMock},
        {provide: SharedService, useClass: SharedServiceMock},
        {provide: LocationsService, useClass: LocationServiceMock},
        NgbActiveModal
        
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class LocationServiceMock{

}

class AlertServiceMock{

}

class SharedServiceMock{
  
}