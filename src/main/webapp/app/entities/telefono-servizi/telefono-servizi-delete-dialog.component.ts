import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITelefonoServizi } from 'app/shared/model/telefono-servizi.model';
import { TelefonoServiziService } from './telefono-servizi.service';

@Component({
    selector: 'jhi-telefono-servizi-delete-dialog',
    templateUrl: './telefono-servizi-delete-dialog.component.html'
})
export class TelefonoServiziDeleteDialogComponent {
    telefonoServizi: ITelefonoServizi;

    constructor(
        private telefonoServiziService: TelefonoServiziService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.telefonoServiziService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'telefonoServiziListModification',
                content: 'Deleted an telefonoServizi'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-telefono-servizi-delete-popup',
    template: ''
})
export class TelefonoServiziDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ telefonoServizi }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(TelefonoServiziDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.telefonoServizi = telefonoServizi;
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
