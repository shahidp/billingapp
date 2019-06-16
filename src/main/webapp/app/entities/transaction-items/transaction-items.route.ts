import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TransactionItems } from 'app/shared/model/transaction-items.model';
import { TransactionItemsService } from './transaction-items.service';
import { TransactionItemsComponent } from './transaction-items.component';
import { TransactionItemsDetailComponent } from './transaction-items-detail.component';
import { TransactionItemsUpdateComponent } from './transaction-items-update.component';
import { TransactionItemsDeletePopupComponent } from './transaction-items-delete-dialog.component';
import { ITransactionItems } from 'app/shared/model/transaction-items.model';

@Injectable({ providedIn: 'root' })
export class TransactionItemsResolve implements Resolve<ITransactionItems> {
  constructor(private service: TransactionItemsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITransactionItems> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TransactionItems>) => response.ok),
        map((transactionItems: HttpResponse<TransactionItems>) => transactionItems.body)
      );
    }
    return of(new TransactionItems());
  }
}

export const transactionItemsRoute: Routes = [
  {
    path: '',
    component: TransactionItemsComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'billingappApp.transactionItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TransactionItemsDetailComponent,
    resolve: {
      transactionItems: TransactionItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'billingappApp.transactionItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TransactionItemsUpdateComponent,
    resolve: {
      transactionItems: TransactionItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'billingappApp.transactionItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TransactionItemsUpdateComponent,
    resolve: {
      transactionItems: TransactionItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'billingappApp.transactionItems.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const transactionItemsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TransactionItemsDeletePopupComponent,
    resolve: {
      transactionItems: TransactionItemsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'billingappApp.transactionItems.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
