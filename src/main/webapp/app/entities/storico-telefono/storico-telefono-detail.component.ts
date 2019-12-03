import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';

@Component({
    selector: 'jhi-storico-telefono-detail',
    templateUrl: './storico-telefono-detail.component.html'
})
export class StoricoTelefonoDetailComponent implements OnInit {
    storicoTelefono: IStoricoTelefono;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storicoTelefono }) => {
            this.storicoTelefono = storicoTelefono;
        });
    }

    previousState() {
        window.history.back();
    }
}
