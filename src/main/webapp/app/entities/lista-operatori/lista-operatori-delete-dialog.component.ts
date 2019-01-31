import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IListaOperatori } from 'app/shared/model/lista-operatori.model';
import { ListaOperatoriService } from './lista-operatori.service';

@Component({
    selector: 'jhi-lista-operatori-delete-dialog',
    templateUrl: './lista-operatori-delete-dialog.component.html'
})
export class ListaOperatoriDeleteDialogComponent {
    listaOperatori: IListaOperatori;

    constructor(
        private listaOperatoriService: ListaOperatoriService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.listaOperatoriService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'listaOperatoriListModification',
                content: 'Deleted an listaOperatori'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-lista-operatori-delete-popup',
    template: ''
})
export class ListaOperatoriDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ listaOperatori }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ListaOperatoriDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.listaOperatori = listaOperatori;
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
