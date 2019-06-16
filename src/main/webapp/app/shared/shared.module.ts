import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BillingappSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [BillingappSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [BillingappSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BillingappSharedModule {
  static forRoot() {
    return {
      ngModule: BillingappSharedModule
    };
  }
}
