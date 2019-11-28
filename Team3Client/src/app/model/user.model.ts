import { Authority } from './authority.model';

export interface UserModel {
    id: string;
    name: string;
    surname: string;
    username: string;
    email: string;
    password: string;
    enabled: boolean;
    authorities: Set<Authority>;
  }