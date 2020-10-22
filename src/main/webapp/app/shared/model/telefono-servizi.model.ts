import { Moment } from 'moment';
import { IServizi } from 'app/shared/model//servizi.model';
import { ITelefono } from 'app/shared/model//telefono.model';

export interface ITelefonoServizi {
    id?: number;
    altro?: string;
    dataInizio?: Moment;
    dataFine?: Moment;
    servizi?: IServizi;
    telefono?: ITelefono;
}

export class TelefonoServizi implements ITelefonoServizi {
    constructor(
        public id?: number,
        public altro?: string,
        public dataInizio?: Moment,
        public dataFine?: Moment,
        public servizi?: IServizi,
        public telefono?: ITelefono
    ) {}
}
