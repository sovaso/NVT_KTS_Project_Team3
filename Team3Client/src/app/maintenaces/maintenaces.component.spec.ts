import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MaintenacesComponent } from './maintenaces.component';

describe('MaintenacesComponent', () => {
  let component: MaintenacesComponent;
  let fixture: ComponentFixture<MaintenacesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MaintenacesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MaintenacesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
