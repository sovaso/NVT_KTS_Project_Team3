import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProfileComponent } from './edit-profile.component';
import { UsersService } from 'src/app/services/users/users.service';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { UserModel } from 'src/app/model/user.model';
import { FormsModule } from '@angular/forms';
import { UserComponent } from '../user.component';
import { Authority } from 'src/app/model/authority.model';

describe('EditProfileComponent', () => {
  let component: EditProfileComponent;
  let fixture: ComponentFixture<EditProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditProfileComponent ],
      imports: [NgbModule,FormsModule],
      providers: [
        NgbActiveModal,
        {provide: UsersService, useClass: UsersServiceMock},
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditProfileComponent);
    component = fixture.componentInstance;
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

class UsersServiceMock{
  getUser():Observable<UserModel>{

    var user = new UserModel();
    user.id = "1";
    user.name = "someName";
    user.surname = "someSurname";
    user.email = "email";
    user.password = "password";
    user.enabled = true;
    user.authorities = new Set<Authority>();
    return of(user);
  }
}
