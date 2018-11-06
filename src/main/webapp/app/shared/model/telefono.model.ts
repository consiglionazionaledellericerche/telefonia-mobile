import { Moment } from 'moment';
import { IUtenza } from 'app/shared/model//utenza.model';
import { IIstituto } from 'app/shared/model//istituto.model';

export interface ITelefono {
    id?: number;
    numero?: string;
    dataattivazione?: Moment;
    datacessazione?: Moment;
    intestatariocontratto?: string;
    numerocontratto?: string;
    utenza_telefono?: IUtenza;
    istituto_telefono?: IIstituto;
}

export class Telefono implements ITelefono {
    constructor(
        public id?: number,
        public numero?: string,
        public dataattivazione?: Moment,
        public datacessazione?: Moment,
        public intestatariocontratto?: string,
        public numerocontratto?: string,
        public utenza_telefono?: IUtenza,
        public istituto_telefono?: IIstituto
    ) {}
}
