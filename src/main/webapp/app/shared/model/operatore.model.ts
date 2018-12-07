import { Moment } from 'moment';
import { ITelefono } from 'app/shared/model//telefono.model';

export interface IOperatore {
    id?: number;
    nome?: string;
    data?: Moment;
    telefono_operatore?: ITelefono;
}

export class Operatore implements IOperatore {
    constructor(public id?: number, public nome?: string, public data?: Moment, public telefono_operatore?: ITelefono) {}
}
