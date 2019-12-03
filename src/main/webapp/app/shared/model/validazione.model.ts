import { Moment } from 'moment';
import { ITelefono } from 'app/shared/model//telefono.model';

export interface IValidazione {
    id?: number;
    dataModifica?: Moment;
    descrizione?: string;
    documentoFirmatoContentType?: string;
    documentoFirmato?: any;
    dataValidazione?: Moment;
    userDirettore?: string;
    ipValidazione?: string;
    idFlusso?: string;
    validazioneTelefono?: ITelefono;
}

export class Validazione implements IValidazione {
    constructor(
        public id?: number,
        public dataModifica?: Moment,
        public descrizione?: string,
        public documentoFirmatoContentType?: string,
        public documentoFirmato?: any,
        public dataValidazione?: Moment,
        public userDirettore?: string,
        public ipValidazione?: string,
        public idFlusso?: string,
        public validazioneTelefono?: ITelefono
    ) {}
}
