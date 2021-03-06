import { Moment } from 'moment';

export interface ITelefono {
    id?: number;
    numero?: string;
    dataAttivazione?: Moment;
    dataCessazione?: Moment;
    intestatarioContratto?: string;
    numeroContratto?: string;
    cdsuo?: string;
    deleted?: boolean;
    deletedNote?: string;
    utilizzatoreUtenza?: string;
    contrattoContentType?: string;
    contratto?: any;
}

export class Telefono implements ITelefono {
    constructor(
        public id?: number,
        public numero?: string,
        public dataAttivazione?: Moment,
        public dataCessazione?: Moment,
        public intestatarioContratto?: string,
        public numeroContratto?: string,
        public cdsuo?: string,
        public deleted?: boolean,
        public deletedNote?: string,
        public utilizzatoreUtenza?: string,
        public contrattoContentType?: string,
        public contratto?: any
    ) {
        this.deleted = this.deleted || false;
    }
}
