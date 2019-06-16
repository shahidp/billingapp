import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITransactionItems, TransactionItems } from 'app/shared/model/transaction-items.model';
import { TransactionItemsService } from './transaction-items.service';
import { ITransaction } from 'app/shared/model/transaction.model';
import { TransactionService } from 'app/entities/transaction';

@Component({
  selector: 'jhi-transaction-items-update',
  templateUrl: './transaction-items-update.component.html'
})
export class TransactionItemsUpdateComponent implements OnInit {
  isSaving: boolean;

  transactions: ITransaction[];

  editForm = this.fb.group({
    id: [],
    itemName: [],
    qty: [],
    amount: [],
    transactionId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected transactionItemsService: TransactionItemsService,
    protected transactionService: TransactionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ transactionItems }) => {
      this.updateForm(transactionItems);
    });
    this.transactionService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITransaction[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITransaction[]>) => response.body)
      )
      .subscribe((res: ITransaction[]) => (this.transactions = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(transactionItems: ITransactionItems) {
    this.editForm.patchValue({
      id: transactionItems.id,
      itemName: transactionItems.itemName,
      qty: transactionItems.qty,
      amount: transactionItems.amount,
      transactionId: transactionItems.transactionId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const transactionItems = this.createFromForm();
    if (transactionItems.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionItemsService.update(transactionItems));
    } else {
      this.subscribeToSaveResponse(this.transactionItemsService.create(transactionItems));
    }
  }

  private createFromForm(): ITransactionItems {
    const entity = {
      ...new TransactionItems(),
      id: this.editForm.get(['id']).value,
      itemName: this.editForm.get(['itemName']).value,
      qty: this.editForm.get(['qty']).value,
      amount: this.editForm.get(['amount']).value,
      transactionId: this.editForm.get(['transactionId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransactionItems>>) {
    result.subscribe((res: HttpResponse<ITransactionItems>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
