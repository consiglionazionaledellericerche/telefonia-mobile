import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelefonoservizi } from 'app/shared/model/telefonoservizi.model';
import { TelefonoserviziService } from './telefonoservizi.service';

@Component({
    selector: 'jhi-telefonoservizi-delete-dialog',
    templateUrl: './telefonoservizi-delete-dialog.component.html'
})
export class TelefonoserviziDeleteDialogComponent {
    telefonoservizi: ITelefonoservizi;

    constructor(
        private telefonoserviziService: TelefonoserviziService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.telefonoserviziService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'telefonoserviziListModification',
                content: 'Deleted an telefonoservizi'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-telefonoservizi-delete-popup',
    template: ''
})
export class TelefonoserviziDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ telefonoservizi }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TelefonoserviziDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.telefonoservizi = telefonoservizi;
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
