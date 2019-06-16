/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { BillingappTestModule } from '../../../test.module';
import { TransactionItemsUpdateComponent } from 'app/entities/transaction-items/transaction-items-update.component';
import { TransactionItemsService } from 'app/entities/transaction-items/transaction-items.service';
import { TransactionItems } from 'app/shared/model/transaction-items.model';

describe('Component Tests', () => {
  describe('TransactionItems Management Update Component', () => {
    let comp: TransactionItemsUpdateComponent;
    let fixture: ComponentFixture<TransactionItemsUpdateComponent>;
    let service: TransactionItemsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillingappTestModule],
        declarations: [TransactionItemsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TransactionItemsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TransactionItemsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionItemsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TransactionItems(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TransactionItems();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
