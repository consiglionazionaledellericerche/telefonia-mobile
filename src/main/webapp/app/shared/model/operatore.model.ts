import { ITelefono } from 'app/shared/model//telefono.model';

export interface IOperatore {
    id?: number;
    nome?: string;
    telefono_operatore?: ITelefono;
}

export class Operatore implements IOperatore {
    constructor(public id?: number, public nome?: string, public telefono_operatore?: ITelefono) {}
}
