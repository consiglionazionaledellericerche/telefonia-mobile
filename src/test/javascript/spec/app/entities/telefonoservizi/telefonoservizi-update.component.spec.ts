/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniTestModule } from '../../../test.module';
import { TelefonoserviziUpdateComponent } from 'app/entities/telefonoservizi/telefonoservizi-update.component';
import { TelefonoserviziService } from 'app/entities/telefonoservizi/telefonoservizi.service';
import { Telefonoservizi } from 'app/shared/model/telefonoservizi.model';

describe('Component Tests', () => {
    describe('Telefonoservizi Management Update Component', () => {
        let comp: TelefonoserviziUpdateComponent;
        let fixture: ComponentFixture<TelefonoserviziUpdateComponent>;
        let service: TelefonoserviziService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniTestModule],
                declarations: [TelefonoserviziUpdateComponent]
            })
                .overrideTemplate(TelefonoserviziUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TelefonoserviziUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelefonoserviziService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Telefonoservizi(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefonoservizi = entity;
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
                    const entity = new Telefonoservizi();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefonoservizi = entity;
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
