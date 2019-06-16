import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'customer',
        loadChildren: './customer/customer.module#BillingappCustomerModule'
      },
      {
        path: 'transaction',
        loadChildren: './transaction/transaction.module#BillingappTransactionModule'
      },
      {
        path: 'payment',
        loadChildren: './payment/payment.module#BillingappPaymentModule'
      },
      {
        path: 'transaction-items',
        loadChildren: './transaction-items/transaction-items.module#BillingappTransactionItemsModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillingappEntityModule {}
