import { Moment } from 'moment';
import { ITelefono } from 'app/shared/model//telefono.model';

export interface IStoricoTelefono {
    id?: number;
    dataModifica?: Moment;
    dataAttivazione?: Moment;
    dataCessazione?: Moment;
    intestatarioContratto?: string;
    numeroContratto?: string;
    utilizzatoreUtenza?: string;
    cdsuo?: string;
    operatore?: string;
    servizi?: string;
    userModifica?: string;
    versione?: string;
    storicotelefonoTelefono?: ITelefono;
}

export class StoricoTelefono implements IStoricoTelefono {
    constructor(
        public id?: number,
        public dataModifica?: Moment,
        public dataAttivazione?: Moment,
        public dataCessazione?: Moment,
        public intestatarioContratto?: string,
        public numeroContratto?: string,
        public utilizzatoreUtenza?: string,
        public cdsuo?: string,
        public operatore?: string,
        public servizi?: string,
        public userModifica?: string,
        public versione?: string,
        public storicotelefonoTelefono?: ITelefono
    ) {}
}
