/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { TelefonoServiziUpdateComponent } from 'app/entities/telefono-servizi/telefono-servizi-update.component';
import { TelefonoServiziService } from 'app/entities/telefono-servizi/telefono-servizi.service';
import { TelefonoServizi } from 'app/shared/model/telefono-servizi.model';

describe('Component Tests', () => {
    describe('TelefonoServizi Management Update Component', () => {
        let comp: TelefonoServiziUpdateComponent;
        let fixture: ComponentFixture<TelefonoServiziUpdateComponent>;
        let service: TelefonoServiziService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [TelefonoServiziUpdateComponent]
            })
                .overrideTemplate(TelefonoServiziUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TelefonoServiziUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelefonoServiziService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TelefonoServizi(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefonoServizi = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new TelefonoServizi();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefonoServizi = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
