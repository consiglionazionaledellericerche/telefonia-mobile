import { Moment } from 'moment';
import { ITelefono } from 'app/shared/model//telefono.model';
import { IListaOperatori } from 'app/shared/model//lista-operatori.model';

export interface IOperatore {
    id?: number;
    data?: Moment;
    telefonoOperatore?: ITelefono;
    listaOperatori?: IListaOperatori;
}

export class Operatore implements IOperatore {
    constructor(public id?: number, public data?: Moment, public telefonoOperatore?: ITelefono, public listaOperatori?: IListaOperatori) {}
}
