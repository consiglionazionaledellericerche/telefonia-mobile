export interface IListaOperatori {
    id?: number;
    nome?: string;
}

export class ListaOperatori implements IListaOperatori {
    constructor(public id?: number, public nome?: string) {}
}
