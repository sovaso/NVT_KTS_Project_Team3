import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import { map } from "rxjs/operators";
import { Message } from '@angular/compiler/src/i18n/i18n_ast';
import { UserModel } from 'src/app/model/user.model';

@Injectable({providedIn: 'root'})
export class RegisterAdminService {

  apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = "http://localhost:8080/auth/registerAdmin";
   
  }
  register = (data: UserModel): Observable<boolean> => {
    
      return this.http.post<Message>(this.apiUrl, data).pipe(
        map( (res: any) => {
            return res;
        })  );
  }

}