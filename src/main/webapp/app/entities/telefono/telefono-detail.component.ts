import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ITelefono } from 'app/shared/model/telefono.model';

@Component({
    selector: 'jhi-telefono-detail',
    templateUrl: './telefono-detail.component.html'
})
export class TelefonoDetailComponent implements OnInit {
    telefono: ITelefono;

    constructor(private activatedRoute: ActivatedRoute, private dataUtils: JhiDataUtils) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ telefono }) => {
            this.telefono = telefono;
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
