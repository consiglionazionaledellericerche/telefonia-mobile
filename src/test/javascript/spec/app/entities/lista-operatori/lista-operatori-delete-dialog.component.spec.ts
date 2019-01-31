/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TelefoniaTestModule } from '../../../test.module';
import { ListaOperatoriDeleteDialogComponent } from 'app/entities/lista-operatori/lista-operatori-delete-dialog.component';
import { ListaOperatoriService } from 'app/entities/lista-operatori/lista-operatori.service';

describe('Component Tests', () => {
    describe('ListaOperatori Management Delete Component', () => {
        let comp: ListaOperatoriDeleteDialogComponent;
        let fixture: ComponentFixture<ListaOperatoriDeleteDialogComponent>;
        let service: ListaOperatoriService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [ListaOperatoriDeleteDialogComponent]
            })
                .overrideTemplate(ListaOperatoriDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ListaOperatoriDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ListaOperatoriService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
