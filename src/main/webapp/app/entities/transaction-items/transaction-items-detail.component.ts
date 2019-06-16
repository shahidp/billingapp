import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITransactionItems } from 'app/shared/model/transaction-items.model';

@Component({
  selector: 'jhi-transaction-items-detail',
  templateUrl: './transaction-items-detail.component.html'
})
export class TransactionItemsDetailComponent implements OnInit {
  transactionItems: ITransactionItems;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactionItems }) => {
      this.transactionItems = transactionItems;
    });
  }

  previousState() {
    window.history.back();
  }
}
