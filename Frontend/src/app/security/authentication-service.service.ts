import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs';

import { map } from "rxjs/operators";
import { catchError } from "rxjs/operators";
import { JwtUtilsService } from './jwt-utils.service';
import { MessageDto } from '../dto/message.dto';
import { analyzeAndValidateNgModules } from '@angular/compiler';

@Injectable()
export class AuthenticationService {

  private readonly loginPath = "/auth/login";
  private readonly getUser = "/auth/login";

  constructor(private http: HttpClient, private jwtUtilsService: JwtUtilsService) { }

  

  login(username: string, password: string) {

    console.log('login called');
    var headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post(this.loginPath, JSON.stringify({ username, password }), { headers }).pipe(
      map( ((res: any) => {
         
          console.log(res['accessToken']);
          console.log(res['userRoleName']);
          console.log("1");
          let token = res && res['accessToken'];
          console.log("2");
          if (token) {
            console.log("3");
            localStorage.setItem('currentUser', JSON.stringify({
              userRoleName: res['userRoleName'],
              token: res['accessToken']
            }));
            console.log("4");
          }
          
      }) 
      
      
      
      ) 
      
      
      );
      /*
      .pipe(catchError((error: any) => {
        if (error.status === 400) {
          return Observable.throw('Ilegal login');
        }
        else {
          return Observable.throw(error.json().error || 'Server error');
        }
      }));
      */

  }

  getToken(): String {
    var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    
    if (currentUser!==null){
      return currentUser['token'];
    }
   
    return "";
  }

  logout(): void {
    localStorage.removeItem('currentUser');
  }

  isLoggedIn(): boolean {
    if (this.getToken() != '') return true;
    else return false;
  }

  getCurrentUser() {
    if (localStorage.currentUser) {
      return JSON.parse(localStorage.currentUser);
    }
    else {
      return undefined;
    }
  }

}
