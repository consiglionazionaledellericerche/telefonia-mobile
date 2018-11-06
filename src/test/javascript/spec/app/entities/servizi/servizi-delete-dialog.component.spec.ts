/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { TelefoniTestModule } from '../../../test.module';
import { ServiziDeleteDialogComponent } from 'app/entities/servizi/servizi-delete-dialog.component';
import { ServiziService } from 'app/entities/servizi/servizi.service';

describe('Component Tests', () => {
    describe('Servizi Management Delete Component', () => {
        let comp: ServiziDeleteDialogComponent;
        let fixture: ComponentFixture<ServiziDeleteDialogComponent>;
        let service: ServiziService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniTestModule],
                declarations: [ServiziDeleteDialogComponent]
            })
                .overrideTemplate(ServiziDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ServiziDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ServiziService);
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
