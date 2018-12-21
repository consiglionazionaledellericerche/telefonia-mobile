import { IServizi } from 'app/shared/model//servizi.model';
import { ITelefono } from 'app/shared/model//telefono.model';

export interface ITelefonoServizi {
    id?: number;
    altro?: string;
    servizi?: IServizi;
    telefono?: ITelefono;
}

export class TelefonoServizi implements ITelefonoServizi {
    constructor(public id?: number, public altro?: string, public servizi?: IServizi, public telefono?: ITelefono) {}
}
