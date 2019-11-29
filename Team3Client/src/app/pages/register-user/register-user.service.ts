import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import { map } from "rxjs/operators";
import { Message } from '@angular/compiler/src/i18n/i18n_ast';
import { UserModel } from 'src/app/model/user.model';
import { Mail } from 'src/app/model/mail.model';

@Injectable({providedIn: 'root'})
export class RegisterUserService {

  apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    
   
  }
  register = (data: UserModel): Observable<boolean> => {

    var user = {};
    var loggedUser = JSON.parse(
      localStorage.getItem('currentUser'));
    if (loggedUser !== null) {
      this.apiUrl = "http://localhost:8080/auth/registerAdmin";
    }else {
      this.apiUrl = "http://localhost:8080/auth/registerUser";
    }
      return this.http.post<Message>(this.apiUrl, data).pipe(
        map( (res: any) => {
            return res;
        })  );
  }

  

  sendMail = (mail) =>
  this.http.post("http://localhost:8080/sendEmail", mail);

 
}


