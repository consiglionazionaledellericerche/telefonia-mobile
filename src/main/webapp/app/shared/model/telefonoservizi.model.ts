import { ITelefono } from 'app/shared/model//telefono.model';
import { IServizi } from 'app/shared/model//servizi.model';

export interface ITelefonoservizi {
    id?: number;
    altro?: string;
    telefono_telser?: ITelefono;
    servizi_telser?: IServizi;
}

export class Telefonoservizi implements ITelefonoservizi {
    constructor(public id?: number, public altro?: string, public telefono_telser?: ITelefono, public servizi_telser?: IServizi) {}
}
