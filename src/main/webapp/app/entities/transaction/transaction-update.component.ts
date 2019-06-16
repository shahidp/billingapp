import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html'
})
export class TransactionUpdateComponent implements OnInit {
  isSaving: boolean;

  customers: ICustomer[];
  trnDateDp: any;

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    trnDate: [null, [Validators.required]],
    totalAmount: [null, [Validators.required]],
    description: [],
    items: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: [],
    customerId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected transactionService: TransactionService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);
    });
    this.customerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICustomer[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICustomer[]>) => response.body)
      )
      .subscribe((res: ICustomer[]) => (this.customers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(transaction: ITransaction) {
    this.editForm.patchValue({
      id: transaction.id,
      transactionNumber: transaction.transactionNumber,
      trnDate: transaction.trnDate,
      totalAmount: transaction.totalAmount,
      description: transaction.description,
      items: transaction.items,
      createdBy: transaction.createdBy,
      createdDate: transaction.createdDate != null ? transaction.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: transaction.lastModifiedBy,
      lastModifiedDate: transaction.lastModifiedDate != null ? transaction.lastModifiedDate.format(DATE_TIME_FORMAT) : null,
      customerId: transaction.customerId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  private createFromForm(): ITransaction {
    const entity = {
      ...new Transaction(),
      id: this.editForm.get(['id']).value,
      transactionNumber: this.editForm.get(['transactionNumber']).value,
      trnDate: this.editForm.get(['trnDate']).value,
      totalAmount: this.editForm.get(['totalAmount']).value,
      description: this.editForm.get(['description']).value,
      items: this.editForm.get(['items']).value,
      createdBy: this.editForm.get(['createdBy']).value,
      createdDate:
        this.editForm.get(['createdDate']).value != null ? moment(this.editForm.get(['createdDate']).value, DATE_TIME_FORMAT) : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy']).value,
      lastModifiedDate:
        this.editForm.get(['lastModifiedDate']).value != null
          ? moment(this.editForm.get(['lastModifiedDate']).value, DATE_TIME_FORMAT)
          : undefined,
      customerId: this.editForm.get(['customerId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>) {
    result.subscribe((res: HttpResponse<ITransaction>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackCustomerById(index: number, item: ICustomer) {
    return item.id;
  }
}
