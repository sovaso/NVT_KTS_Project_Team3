import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/security/authentication-service.service';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public user;

  public wrongUsernameOrPass:boolean;
  public noInput:boolean;
  public needToValidate:boolean;
  

  constructor(private authenticationService:AuthenticationService,
              private router: Router) {
    this.user = {};
    this.wrongUsernameOrPass = false;
    this.noInput=false;
    this.needToValidate = false;
   }

   ngOnInit() {
  }
  
   login():void{
    if (this.user.username !=null && this.user != null){

    
    this.authenticationService.login(this.user.username, this.user.password).subscribe(
      (loggedIn:string) => {
        console.log("nestooo");
        if(loggedIn=="success"){
          console.log("is loggeeed in");
          location.reload();         
        }else if (loggedIn=="doNotExist"){
          console.log('do not exist');
          this.wrongUsernameOrPass = true;
        }else if (loggedIn=="needToValidate"){
          console.log('need to validate');
          this.needToValidate = true;
        }
      }
    ,
    (err:Error) => {
        console.log('errrroooor');
        console.log(err);
        console.log('erroooor');
        var errResponse = err as HttpErrorResponse;
        if (errResponse.status == 403){
          console.log('need to verify your account');
          this.needToValidate = true;
        }else {
          this.wrongUsernameOrPass = true;
        }
       
       
        console.log(err);
     
    });

  }else {
    this.noInput = true;
  }
  }

}
