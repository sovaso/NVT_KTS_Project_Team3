import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LocationReportComponent } from './location-report.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { LocationReportDto } from 'src/app/dto/location_report.dto';

describe('LocationReportComponent', () => {
  let component: LocationReportComponent;
  let fixture: ComponentFixture<LocationReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LocationReportComponent ],
      providers: [
        NgbModal
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LocationReportComponent);
    component = fixture.componentInstance;
    component.data = new LocationReportDto();
    component.data.dailyLabels = new Array<string>();
    component.data.dailyValues = new Array<number>();
    component.data.weeklyLabels = new Array<string>();
    component.data.weeklyValues = new Array<number>();
    component.data.monthlyLabels = new Array<string>();
    component.data.monthlyValues = new Array<number>();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
