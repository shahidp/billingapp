import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { BillingappSharedModule } from 'app/shared';
import {
  PaymentComponent,
  PaymentDetailComponent,
  PaymentUpdateComponent,
  PaymentDeletePopupComponent,
  PaymentDeleteDialogComponent,
  paymentRoute,
  paymentPopupRoute
} from './';

const ENTITY_STATES = [...paymentRoute, ...paymentPopupRoute];

@NgModule({
  imports: [BillingappSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    PaymentComponent,
    PaymentDetailComponent,
    PaymentUpdateComponent,
    PaymentDeleteDialogComponent,
    PaymentDeletePopupComponent
  ],
  entryComponents: [PaymentComponent, PaymentUpdateComponent, PaymentDeleteDialogComponent, PaymentDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillingappPaymentModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
