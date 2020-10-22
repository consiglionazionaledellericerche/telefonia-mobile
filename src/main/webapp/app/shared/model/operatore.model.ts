import { Moment } from 'moment';
import { ITelefono } from 'app/shared/model//telefono.model';
import { IListaOperatori } from 'app/shared/model//lista-operatori.model';

export interface IOperatore {
    id?: number;
    data?: Moment;
    dataFine?: Moment;
    numeroContratto?: string;
    contrattoContentType?: string;
    contratto?: any;
    telefonoOperatore?: ITelefono;
    listaOperatori?: IListaOperatori;
}

export class Operatore implements IOperatore {
    constructor(
        public id?: number,
        public data?: Moment,
        public dataFine?: Moment,
        public numeroContratto?: string,
        public contrattoContentType?: string,
        public contratto?: any,
        public telefonoOperatore?: ITelefono,
        public listaOperatori?: IListaOperatori
    ) {}
}
