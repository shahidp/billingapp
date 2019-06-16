import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITransactionItems } from 'app/shared/model/transaction-items.model';
import { TransactionItemsService } from './transaction-items.service';

@Component({
  selector: 'jhi-transaction-items-delete-dialog',
  templateUrl: './transaction-items-delete-dialog.component.html'
})
export class TransactionItemsDeleteDialogComponent {
  transactionItems: ITransactionItems;

  constructor(
    protected transactionItemsService: TransactionItemsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.transactionItemsService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'transactionItemsListModification',
        content: 'Deleted an transactionItems'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-transaction-items-delete-popup',
  template: ''
})
export class TransactionItemsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ transactionItems }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TransactionItemsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.transactionItems = transactionItems;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/transaction-items', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/transaction-items', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
