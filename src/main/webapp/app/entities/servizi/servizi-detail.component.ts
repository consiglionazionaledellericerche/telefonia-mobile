import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServizi } from 'app/shared/model/servizi.model';

@Component({
    selector: 'jhi-servizi-detail',
    templateUrl: './servizi-detail.component.html'
})
export class ServiziDetailComponent implements OnInit {
    servizi: IServizi;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ servizi }) => {
            this.servizi = servizi;
        });
    }

    previousState() {
        window.history.back();
    }
}
