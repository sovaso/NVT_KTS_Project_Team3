import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventReportComponent } from './event-report.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventReportDto } from 'src/app/dto/event_report.dto';

describe('EventReportComponent', () => {
  let component: EventReportComponent;
  let fixture: ComponentFixture<EventReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventReportComponent ],
      providers: [
        NgbModal
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventReportComponent);
    component = fixture.componentInstance;
    component.data = new EventReportDto();
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
