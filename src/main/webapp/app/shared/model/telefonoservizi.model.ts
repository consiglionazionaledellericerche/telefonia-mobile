import { ITelefono } from 'app/shared/model//telefono.model';
import { IServizi } from 'app/shared/model//servizi.model';

export interface ITelefonoservizi {
    id?: number;
    altro?: string;
    telefono?: ITelefono;
    servizi?: IServizi;
}

export class Telefonoservizi implements ITelefonoservizi {
    constructor(public id?: number, public altro?: string, public telefono?: ITelefono, public servizi?: IServizi) {}
}
