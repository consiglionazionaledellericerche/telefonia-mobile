/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TelefoniaTestModule } from '../../../test.module';
import { TelefonoDeleteDialogComponent } from 'app/entities/telefono/telefono-delete-dialog.component';
import { TelefonoService } from 'app/entities/telefono/telefono.service';

describe('Component Tests', () => {
    describe('Telefono Management Delete Component', () => {
        let comp: TelefonoDeleteDialogComponent;
        let fixture: ComponentFixture<TelefonoDeleteDialogComponent>;
        let service: TelefonoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [TelefonoDeleteDialogComponent]
            })
                .overrideTemplate(TelefonoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelefonoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelefonoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
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
            ));
        });
    });
});
