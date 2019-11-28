import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatemaintenacesComponent } from './createmaintenaces.component';

describe('CreatemaintenacesComponent', () => {
  let component: CreatemaintenacesComponent;
  let fixture: ComponentFixture<CreatemaintenacesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreatemaintenacesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatemaintenacesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
