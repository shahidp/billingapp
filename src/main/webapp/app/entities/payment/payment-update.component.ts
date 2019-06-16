import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IPayment, Payment } from 'app/shared/model/payment.model';
import { PaymentService } from './payment.service';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from 'app/entities/transaction';

@Component({
  selector: 'jhi-payment-update',
  templateUrl: './payment-update.component.html'
})
export class PaymentUpdateComponent implements OnInit {
  isSaving: boolean;

  transactions: ITransaction[];
  billDateDp: any;
  createdDateDp: any;
  lastModifiedDateDp: any;

  editForm = this.fb.group({
    id: [],
    billNumber: [null, [Validators.required]],
    billDate: [],
    paidAmount: [],
    modeOfPayment: [],
    modeOfDescription: [],
    payid: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    transactionId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected paymentService: PaymentService,
    protected transactionService: TransactionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ payment }) => {
      this.updateForm(payment);
    });
    this.transactionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITransaction[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITransaction[]>) => response.body)
      )
      .subscribe((res: ITransaction[]) => (this.transactions = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(payment: IPayment) {
    this.editForm.patchValue({
      id: payment.id,
      billNumber: payment.billNumber,
      billDate: payment.billDate,
      paidAmount: payment.paidAmount,
      modeOfPayment: payment.modeOfPayment,
      modeOfDescription: payment.modeOfDescription,
      payid: payment.payid,
      createdBy: payment.createdBy,
      createdDate: payment.createdDate,
      lastModifiedBy: payment.lastModifiedBy,
      lastModifiedDate: payment.lastModifiedDate,
      transactionId: payment.transactionId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const payment = this.createFromForm();
    if (payment.id !== undefined) {
      this.subscribeToSaveResponse(this.paymentService.update(payment));
    } else {
      this.subscribeToSaveResponse(this.paymentService.create(payment));
    }
  }

  private createFromForm(): IPayment {
    const entity = {
      ...new Payment(),
      id: this.editForm.get(['id']).value,
      billNumber: this.editForm.get(['billNumber']).value,
      billDate: this.editForm.get(['billDate']).value,
      paidAmount: this.editForm.get(['paidAmount']).value,
      modeOfPayment: this.editForm.get(['modeOfPayment']).value,
      modeOfDescription: this.editForm.get(['modeOfDescription']).value,
      payid: this.editForm.get(['payid']).value,
      createdBy: this.editForm.get(['createdBy']).value,
      createdDate: this.editForm.get(['createdDate']).value,
      lastModifiedBy: this.editForm.get(['lastModifiedBy']).value,
      lastModifiedDate: this.editForm.get(['lastModifiedDate']).value,
      transactionId: this.editForm.get(['transactionId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayment>>) {
    result.subscribe((res: HttpResponse<IPayment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackTransactionById(index: number, item: ITransaction) {
    return item.id;
  }
}
