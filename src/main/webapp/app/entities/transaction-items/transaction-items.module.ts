import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BillingappSharedModule } from 'app/shared';
import {
  TransactionItemsComponent,
  TransactionItemsDetailComponent,
  TransactionItemsUpdateComponent,
  TransactionItemsDeletePopupComponent,
  TransactionItemsDeleteDialogComponent,
  transactionItemsRoute,
  transactionItemsPopupRoute
} from './';

const ENTITY_STATES = [...transactionItemsRoute, ...transactionItemsPopupRoute];

@NgModule({
  imports: [BillingappSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TransactionItemsComponent,
    TransactionItemsDetailComponent,
    TransactionItemsUpdateComponent,
    TransactionItemsDeleteDialogComponent,
    TransactionItemsDeletePopupComponent
  ],
  entryComponents: [
    TransactionItemsComponent,
    TransactionItemsUpdateComponent,
    TransactionItemsDeleteDialogComponent,
    TransactionItemsDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillingappTransactionItemsModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
