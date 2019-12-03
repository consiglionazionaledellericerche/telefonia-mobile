/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TelefoniaTestModule } from '../../../test.module';
import { StoricoTelefonoDeleteDialogComponent } from 'app/entities/storico-telefono/storico-telefono-delete-dialog.component';
import { StoricoTelefonoService } from 'app/entities/storico-telefono/storico-telefono.service';

describe('Component Tests', () => {
    describe('StoricoTelefono Management Delete Component', () => {
        let comp: StoricoTelefonoDeleteDialogComponent;
        let fixture: ComponentFixture<StoricoTelefonoDeleteDialogComponent>;
        let service: StoricoTelefonoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [StoricoTelefonoDeleteDialogComponent]
            })
                .overrideTemplate(StoricoTelefonoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoricoTelefonoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoricoTelefonoService);
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
