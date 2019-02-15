import { Moment } from 'moment';

export interface ITelefono {
    id?: number;
    numero?: string;
    dataAttivazione?: Moment;
    dataCessazione?: Moment;
    intestatarioContratto?: string;
    numeroContratto?: string;
    utenzaTelefono?: string;
    istitutoTelefono?: string;
    cdsuo?: string;
}

export class Telefono implements ITelefono {
    constructor(
        public id?: number,
        public numero?: string,
        public dataAttivazione?: Moment,
        public dataCessazione?: Moment,
        public intestatarioContratto?: string,
        public numeroContratto?: string,
        public utenzaTelefono?: string,
        public istitutoTelefono?: string,
        public cdsuo?: string
    ) {}
}
