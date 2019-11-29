import { Component, OnInit } from '@angular/core';
import { RegisterUserService } from './register-user.service';
import { Router } from '@angular/router';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Mail } from 'src/app/model/mail.model';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {

  public user;
  public success:boolean;
  public noInput:boolean;
  message: String = '';
  type = '';
  public repeatedPassword : String = '';
  constructor(public activeModal: NgbActiveModal, private registerUserService: RegisterUserService, private router: Router) { 
    this.user = {};
    this.success=false;
    this.noInput=false;
  }

  ngOnInit() {
  }

  register():void{
    console.log('-----');
    console.log(this.user.name);
    console.log('-----');
    this.user.enabled = false;
    if (this.user.name != undefined && this.user.surname != undefined && this.user.email != undefined &&
      this.user.username != undefined && this.user.password!=undefined){

      if (this.user.password==this.repeatedPassword){
        this.registerUserService.register(this.user).subscribe(
          (registered:boolean) => {
            console.log("nestooo");
            if(registered){
              console.log("is registered in");
              this.message = "Successful registration, congratulations! Please go to email to verify your registration!",
              "success";
              this.type = 'success';
              var username = this.user.username;
              let mail = new Mail();
              mail.emailAddress = this.user.email;
              mail.subject = "Register verification";
              mail.body = `Please click on the following link in order to activate your account:\nhttp://localhost:8080/api/confirmRegistration/${username}`;
    
              this.registerUserService.sendMail(mail).subscribe(
                () =>{

                }
              );
        
            }
          }
        ,
          (err:Error) => {
              this.user.username='';
                this.message = 'Username already exist.';
                this.type = 'danger';
              console.log(err);
          
          });
      }else {
        
        this.message = 'Passwords must match.';
        this.type = 'danger';
      }
      
  }else {
   
    this.message = 'Please fill all fields.';
    this.type = 'danger';
  }
  }

}
