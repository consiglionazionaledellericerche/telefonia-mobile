import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOperatore } from 'app/shared/model/operatore.model';

@Component({
    selector: 'jhi-operatore-detail',
    templateUrl: './operatore-detail.component.html'
})
export class OperatoreDetailComponent implements OnInit {
    operatore: IOperatore;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ operatore }) => {
            this.operatore = operatore;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
