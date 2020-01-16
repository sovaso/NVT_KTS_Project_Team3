import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertBoxComponent } from './alert-box.component';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

describe('AlertBoxComponent', () => {
  let component: AlertBoxComponent;
  let fixture: ComponentFixture<AlertBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AlertBoxComponent ],
      imports: [ NgbModule],
      providers: [NgbActiveModal]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
