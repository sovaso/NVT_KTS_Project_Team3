import { Component, OnInit, Input } from '@angular/core';


import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { EditProfileComponent } from '../user/edit-profile/edit-profile.component';
import { UserModel } from '../model/user.model';
import { AuthenticationService } from '../security/authentication-service.service';
import { LoginComponent } from '../pages/login/login.component';
import { RegisterUserComponent } from '../pages/register-user/register-user.component';
import { CurrentUser } from '../model/currentUser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css', './general.scss']
})
export class HeaderComponent implements OnInit {

  loggedUser: CurrentUser;
  username: string;


  //moze da se user povuce iz storage-a; uloga i username, ne ceo user, jer se cuva token 
  constructor(private modalService: NgbModal, private AuthenticationService: AuthenticationService) { }

  ngOnInit() {
    this.loggedUser = JSON.parse(
      localStorage.getItem('currentUser'));

    var login: HTMLElement = document.getElementById('loginButton');
    var logout: HTMLElement = document.getElementById('logoutItem');
    var register: HTMLElement = document.getElementById('registerItem');
    var edit: HTMLElement = document.getElementById('editItem');
    var notRegistrated: HTMLElement = document.getElementById('notRegistrated');
    var registrated: HTMLElement = document.getElementById('registrated');


    if (this.loggedUser === null) {
      //alert('niko nije ulogovan');
      //login.hidden = false;
      //register.hidden = false;
      //logout.hidden = true;
      //edit.hidden = true;
      notRegistrated.hidden = false;
      registrated.hidden = true;

    } else {
      //logout.hidden = false;
      //edit.hidden = false;
      //login.hidden = true;

      notRegistrated.hidden = true;
      registrated.hidden = false;
      if (this.loggedUser.userRoleName === "ROLE_ADMIN") {
        notRegistrated.hidden = true;
        login.hidden = true;
        register.hidden = false;
      } else {
        register.hidden = true;
        login.hidden = true;
      }

    }

  }

  open() {
    console.log('login called');
    const modalRef = this.modalService.open(LoginComponent);
    
  }
  openReg() {

    const modalRef = this.modalService.open(RegisterUserComponent);
    //modalRef.componentInstance.name = 'Login';
  }

  edit() {
    const modalRef = this.modalService.open(EditProfileComponent);
  }

  logout() {
    this.AuthenticationService.logout();
    location.reload();
  }

}




