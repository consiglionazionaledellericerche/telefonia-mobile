import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITelefonoServizi } from 'app/shared/model/telefono-servizi.model';

@Component({
    selector: 'jhi-telefono-servizi-detail',
    templateUrl: './telefono-servizi-detail.component.html'
})
export class TelefonoServiziDetailComponent implements OnInit {
    telefonoServizi: ITelefonoServizi;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ telefonoServizi }) => {
            this.telefonoServizi = telefonoServizi;
        });
    }

    previousState() {
        window.history.back();
    }
}
