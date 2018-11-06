export interface IIstituto {
    id?: number;
    cds?: string;
    nome?: string;
    citta?: string;
    indirizzo?: string;
}

export class Istituto implements IIstituto {
    constructor(public id?: number, public cds?: string, public nome?: string, public citta?: string, public indirizzo?: string) {}
}
