import { IUser } from 'app/core/user/user.model';
import { IIstituto } from 'app/shared/model//istituto.model';

export interface IUtenza {
    id?: number;
    matricola?: string;
    user_utenza?: IUser;
    istituto_user?: IIstituto;
}

export class Utenza implements IUtenza {
    constructor(public id?: number, public matricola?: string, public user_utenza?: IUser, public istituto_user?: IIstituto) {}
}
