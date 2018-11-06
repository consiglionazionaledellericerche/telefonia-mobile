export interface IServizi {
    id?: number;
    nome?: string;
}

export class Servizi implements IServizi {
    constructor(public id?: number, public nome?: string) {}
}
