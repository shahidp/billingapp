/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BillingappTestModule } from '../../../test.module';
import { TransactionItemsDetailComponent } from 'app/entities/transaction-items/transaction-items-detail.component';
import { TransactionItems } from 'app/shared/model/transaction-items.model';

describe('Component Tests', () => {
  describe('TransactionItems Management Detail Component', () => {
    let comp: TransactionItemsDetailComponent;
    let fixture: ComponentFixture<TransactionItemsDetailComponent>;
    const route = ({ data: of({ transactionItems: new TransactionItems(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BillingappTestModule],
        declarations: [TransactionItemsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TransactionItemsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionItemsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.transactionItems).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
