import { Authority } from './authority.model';

export class UserModel {
    id: string;
    name: string;
    surname: string;
    username: string;
    email: string;
    password: string;
    enabled: boolean;
    authorities: Set<Authority>;

    constructor(){
      
    }
  }