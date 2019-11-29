import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UsersService } from 'src/app/services/users/users.service';
import { repeat } from 'rxjs/operators';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  public u;
  public user;
  public success:boolean;
  public noInput:boolean;
  public passwordsDoNotMatch:boolean;
  message: String = '';
  type = '';
  public repeatedPassword : String = '';
  constructor(public activeModal: NgbActiveModal, private usersService : UsersService) { 
    this.user = {};
    this.success=false;
    this.noInput=false;
    this.passwordsDoNotMatch=false;
  }

  ngOnInit() {
    this.u={};
    this.user = {
      id: '',
      name: '',
      surname: '',
      username: '',
      email: '',
      password: '',
      enabled: true,
      authorities: null,
     }
     
    this.usersService.getUser().subscribe(data=>{
      this.user=data;
      this.user.password='';
      this.u.repeatPassword='';
    
    },error=>{
      this.type="danger";
      this.message="Something went wrong.Please try again.";
    });
  }

  saveChanges():void{
   
    console.log("****");
    console.log(this.repeatedPassword);
    console.log("****");
    if (this.user.name != "" && this.user.surname != "" && this.user.email != "" &&
      this.user.username != "" && this.user.password != "" && this.repeatedPassword!=""){
        if (this.user.password== this.repeatedPassword){
          this.usersService.edit(this.user).subscribe(
            (updated:boolean) => {
              console.log("izmenjen user");
              if(updated){
                console.log("user uspesno izmenjen");
                location.reload();       
              }
            }
          ,
          (err:Error) => {
            
              this.success = false;
              console.log(err);
           
          });
        }else {
          console.log("passwords do not match");
          console.log(this.user.password + "---"+this.repeatedPassword);
          this.passwordsDoNotMatch = true;
        }
    
  }else {
    this.noInput = true;
  }
  }

}
