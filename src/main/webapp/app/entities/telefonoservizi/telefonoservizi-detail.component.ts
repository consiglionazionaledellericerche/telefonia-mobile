import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITelefonoservizi } from 'app/shared/model/telefonoservizi.model';

@Component({
    selector: 'jhi-telefonoservizi-detail',
    templateUrl: './telefonoservizi-detail.component.html'
})
export class TelefonoserviziDetailComponent implements OnInit {
    telefonoservizi: ITelefonoservizi;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ telefonoservizi }) => {
            this.telefonoservizi = telefonoservizi;
        });
    }

    previousState() {
        window.history.back();
    }
}
