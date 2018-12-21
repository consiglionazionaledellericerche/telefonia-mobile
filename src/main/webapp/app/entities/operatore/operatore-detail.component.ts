import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOperatore } from 'app/shared/model/operatore.model';

@Component({
    selector: 'jhi-operatore-detail',
    templateUrl: './operatore-detail.component.html'
})
export class OperatoreDetailComponent implements OnInit {
    operatore: IOperatore;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ operatore }) => {
            this.operatore = operatore;
        });
    }

    previousState() {
        window.history.back();
    }
}
