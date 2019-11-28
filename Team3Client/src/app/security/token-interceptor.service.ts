import { Injectable } from '@angular/core';
import { HttpInterceptor } from '@angular/common/http';
//import { AuthenticationService } from './authentication-service.service';
import { HttpRequest } from '@angular/common/http';
import { HttpHandler } from '@angular/common/http';

import { HttpEvent } from '@angular/common/http';
//import { HttpHeaders } from '@angular/common/http/src/headers';
import { Injector } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthenticationService } from './authentication-service.service';


@Injectable()
export class TokenInterceptorService implements HttpInterceptor {

  constructor(private inj: Injector) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log("interceptor called");
    let authenticationService:AuthenticationService = this.inj.get(AuthenticationService); 
    console.log("from intercept");
    console.log(authenticationService.getToken());
    request = request.clone({
      setHeaders: {
        "Authorization": `Bearer ${authenticationService.getToken()}`
      }
    });
    console.log(request);
    console.log("****");
    console.log(request.headers);

    return next.handle(request);
  }

}