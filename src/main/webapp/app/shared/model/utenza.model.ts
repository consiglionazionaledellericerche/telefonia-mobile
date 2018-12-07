export interface IUtenza {
    id?: number;
    matricola?: string;
    uid?: string;
}

export class Utenza implements IUtenza {
    constructor(public id?: number, public matricola?: string, public uid?: string) {}
}
