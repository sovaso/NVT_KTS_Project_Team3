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
  message: String = '';
  type = '';
  public repeatedPassword : String = '';
/*   public success:boolean;
  public noInput:boolean; */
  constructor(private registerAdminService: RegisterAdminService, private router: Router) { 
    this.user = {};
  
  }

  ngOnInit() {
  }

  register():void{
    if (this.user.name != undefined && this.user.surname != undefined && this.user.email != undefined &&
      this.user.username != undefined && this.user.password!=undefined){
      if (this.user.password==this.repeatedPassword){
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
          this.user.username='';
          this.message = 'Username already exist.';
          this.type = 'danger';
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
