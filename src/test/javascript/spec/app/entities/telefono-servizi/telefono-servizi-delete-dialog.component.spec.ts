/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TelefoniaTestModule } from '../../../test.module';
import { TelefonoServiziDeleteDialogComponent } from 'app/entities/telefono-servizi/telefono-servizi-delete-dialog.component';
import { TelefonoServiziService } from 'app/entities/telefono-servizi/telefono-servizi.service';

describe('Component Tests', () => {
    describe('TelefonoServizi Management Delete Component', () => {
        let comp: TelefonoServiziDeleteDialogComponent;
        let fixture: ComponentFixture<TelefonoServiziDeleteDialogComponent>;
        let service: TelefonoServiziService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [TelefonoServiziDeleteDialogComponent]
            })
                .overrideTemplate(TelefonoServiziDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TelefonoServiziDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelefonoServiziService);
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
