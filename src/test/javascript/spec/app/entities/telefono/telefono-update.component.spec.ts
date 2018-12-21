/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { TelefonoUpdateComponent } from 'app/entities/telefono/telefono-update.component';
import { TelefonoService } from 'app/entities/telefono/telefono.service';
import { Telefono } from 'app/shared/model/telefono.model';

describe('Component Tests', () => {
    describe('Telefono Management Update Component', () => {
        let comp: TelefonoUpdateComponent;
        let fixture: ComponentFixture<TelefonoUpdateComponent>;
        let service: TelefonoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [TelefonoUpdateComponent]
            })
                .overrideTemplate(TelefonoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(TelefonoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TelefonoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Telefono(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefono = entity;
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
                    const entity = new Telefono();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.telefono = entity;
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
