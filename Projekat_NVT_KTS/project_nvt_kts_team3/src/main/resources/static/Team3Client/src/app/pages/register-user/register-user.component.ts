import { Component, OnInit } from '@angular/core';
import { RegisterUserService } from './register-user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {

  public user;
  public success:boolean;
  public noInput:boolean;
  constructor(private registerUserService: RegisterUserService, private router: Router) { 
    this.user = {};
    this.success=false;
    this.noInput=false;
  }

  ngOnInit() {
  }

  register():void{
    if (this.user.name != null && this.user.surname != null && this.user.email != null &&
      this.user.username != null && this.user.password!=null){

      
    this.registerUserService.register(this.user).subscribe(
      (registered:boolean) => {
        console.log("nestooo");
        if(registered){
          console.log("is registered in");
          this.router.navigate(['/userPage']);   
                
        }
      }
    ,
    (err:Error) => {
      
        this.success = false;
        console.log(err);
     
    });
  }else {
    this.noInput = true;
  }
  }

}
