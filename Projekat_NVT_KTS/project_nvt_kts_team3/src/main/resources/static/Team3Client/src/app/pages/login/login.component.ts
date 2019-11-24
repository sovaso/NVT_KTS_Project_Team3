import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/security/authentication-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public user;

  public wrongUsernameOrPass:boolean;
  public noInput:boolean;

  constructor(private authenticationService:AuthenticationService,
              private router: Router) {
    this.user = {};
    this.wrongUsernameOrPass = false;
    this.noInput=false;
   }

   ngOnInit() {
  }
  
   login():void{
    if (this.user.username !=null && this.user != null){

    
    this.authenticationService.login(this.user.username, this.user.password).subscribe(
      (loggedIn:boolean) => {
        console.log("nestooo");
        if(loggedIn){
          console.log("is loggeeed in");
          location.reload();         
        }
      }
    ,
    (err:Error) => {
      
        this.wrongUsernameOrPass = true;
        console.log(err);
     
    });

  }else {
    this.noInput = true;
  }
  }

}
