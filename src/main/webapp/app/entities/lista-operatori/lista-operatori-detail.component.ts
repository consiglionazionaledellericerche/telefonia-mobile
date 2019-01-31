import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IListaOperatori } from 'app/shared/model/lista-operatori.model';

@Component({
    selector: 'jhi-lista-operatori-detail',
    templateUrl: './lista-operatori-detail.component.html'
})
export class ListaOperatoriDetailComponent implements OnInit {
    listaOperatori: IListaOperatori;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ listaOperatori }) => {
            this.listaOperatori = listaOperatori;
        });
    }

    previousState() {
        window.history.back();
    }
}
