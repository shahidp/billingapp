/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { BillingappTestModule } from '../../../test.module';
import { TransactionItemsDeleteDialogComponent } from 'app/entities/transaction-items/transaction-items-delete-dialog.component';
import { TransactionItemsService } from 'app/entities/transaction-items/transaction-items.service';

describe('Component Tests', () => {
  describe('TransactionItems Management Delete Component', () => {
    let comp: TransactionItemsDeleteDialogComponent;
    let fixture: ComponentFixture<TransactionItemsDeleteDialogComponent>;
    let service: TransactionItemsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillingappTestModule],
        declarations: [TransactionItemsDeleteDialogComponent]
      })
        .overrideTemplate(TransactionItemsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionItemsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TransactionItemsService);
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
