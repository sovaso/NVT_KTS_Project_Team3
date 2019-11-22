import { Component, OnInit } from '@angular/core';
import { RegisterAdminService } from './register-admin.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-admin',
  templateUrl: './register-admin.component.html',
  styleUrls: ['./register-admin.component.css']
})
export class RegisterAdminComponent implements OnInit {

  public user;
  public success:boolean;
  public noInput:boolean;
  constructor(private registerAdminService: RegisterAdminService, private router: Router) { 
    this.user = {};
    this.success=false;
    this.noInput = false;
  }

  ngOnInit() {
  }

  register():void{
    if (this.user.name != null && this.user.surname != null && this.user.email != null &&
      this.user.username != null && this.user.password!=null){
    this.registerAdminService.register(this.user).subscribe(
      (registered:boolean) => {
        console.log("nestooo");
        if(registered){
          console.log("is registered in");
          this.router.navigate(['/adminPage']);       
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
