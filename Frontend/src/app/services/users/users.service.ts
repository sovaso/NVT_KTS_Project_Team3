import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import { map } from "rxjs/operators";
import { Message } from '@angular/compiler/src/i18n/i18n_ast';
import { UserModel } from 'src/app/model/user.model';

@Injectable({providedIn: 'root'})
export class UsersService {

  user: UserModel;
  public u;
  message: String = '';
  type = '';
  constructor(private http: HttpClient) {
    
  }
  edit = (data: UserModel): Observable<boolean> => {
    
      return this.http.put<boolean>("/api/editUser", data).pipe(
        map( (res: any) => {
            return res;
        })  );
  }

  getUser():Observable<UserModel>{
    return this.http.get<UserModel>("/api/getLogged");
  }

  


}