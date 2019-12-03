/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { TelefoniaTestModule } from '../../../test.module';
import { StoricoTelefonoUpdateComponent } from 'app/entities/storico-telefono/storico-telefono-update.component';
import { StoricoTelefonoService } from 'app/entities/storico-telefono/storico-telefono.service';
import { StoricoTelefono } from 'app/shared/model/storico-telefono.model';

describe('Component Tests', () => {
    describe('StoricoTelefono Management Update Component', () => {
        let comp: StoricoTelefonoUpdateComponent;
        let fixture: ComponentFixture<StoricoTelefonoUpdateComponent>;
        let service: StoricoTelefonoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [TelefoniaTestModule],
                declarations: [StoricoTelefonoUpdateComponent]
            })
                .overrideTemplate(StoricoTelefonoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StoricoTelefonoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoricoTelefonoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new StoricoTelefono(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.storicoTelefono = entity;
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
                    const entity = new StoricoTelefono();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.storicoTelefono = entity;
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
