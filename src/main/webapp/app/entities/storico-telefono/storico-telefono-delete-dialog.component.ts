import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStoricoTelefono } from 'app/shared/model/storico-telefono.model';
import { StoricoTelefonoService } from './storico-telefono.service';

@Component({
    selector: 'jhi-storico-telefono-delete-dialog',
    templateUrl: './storico-telefono-delete-dialog.component.html'
})
export class StoricoTelefonoDeleteDialogComponent {
    storicoTelefono: IStoricoTelefono;

    constructor(
        private storicoTelefonoService: StoricoTelefonoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.storicoTelefonoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'storicoTelefonoListModification',
                content: 'Deleted an storicoTelefono'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-storico-telefono-delete-popup',
    template: ''
})
export class StoricoTelefonoDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storicoTelefono }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StoricoTelefonoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.storicoTelefono = storicoTelefono;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
