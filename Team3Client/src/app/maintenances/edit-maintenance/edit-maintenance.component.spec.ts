import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditMaintenanceComponent } from './edit-maintenance.component';

describe('EditMaintenanceComponent', () => {
  let component: EditMaintenanceComponent;
  let fixture: ComponentFixture<EditMaintenanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditMaintenanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
