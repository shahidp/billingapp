import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html'
})
export class CustomerUpdateComponent implements OnInit {
  isSaving: boolean;
  dobDp: any;

  editForm = this.fb.group({
    id: [],
    fname: [null, [Validators.required]],
    lname: [null, [Validators.required]],
    mobileNo: [null, [Validators.minLength(10)]],
    dob: [],
    addressLine1: [],
    addressLine2: [],
    city: [],
    state: [],
    pincode: [],
    otherDetail: [],
    createdBy: [],
    createdDate: [],
    lastModifiedBy: [],
    lastModifiedDate: []
  });

  constructor(protected customerService: CustomerService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);
    });
  }

  updateForm(customer: ICustomer) {
    this.editForm.patchValue({
      id: customer.id,
      fname: customer.fname,
      lname: customer.lname,
      mobileNo: customer.mobileNo,
      dob: customer.dob,
      addressLine1: customer.addressLine1,
      addressLine2: customer.addressLine2,
      city: customer.city,
      state: customer.state,
      pincode: customer.pincode,
      otherDetail: customer.otherDetail,
      createdBy: customer.createdBy,
      createdDate: customer.createdDate != null ? customer.createdDate.format(DATE_TIME_FORMAT) : null,
      lastModifiedBy: customer.lastModifiedBy,
      lastModifiedDate: customer.lastModifiedDate != null ? customer.lastModifiedDate.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  private createFromForm(): ICustomer {
    const entity = {
      ...new Customer(),
      id: this.editForm.get(['id']).value,
      fname: this.editForm.get(['fname']).value,
      lname: this.editForm.get(['lname']).value,
      mobileNo: this.editForm.get(['mobileNo']).value,
      dob: this.editForm.get(['dob']).value,
      addressLine1: this.editForm.get(['addressLine1']).value,
      addressLine2: this.editForm.get(['addressLine2']).value,
      city: this.editForm.get(['city']).value,
      state: this.editForm.get(['state']).value,
      pincode: this.editForm.get(['pincode']).value,
      otherDetail: this.editForm.get(['otherDetail']).value,
      createdBy: this.editForm.get(['createdBy']).value,
      createdDate:
        this.editForm.get(['createdDate']).value != null ? moment(this.editForm.get(['createdDate']).value, DATE_TIME_FORMAT) : undefined,
      lastModifiedBy: this.editForm.get(['lastModifiedBy']).value,
      lastModifiedDate:
        this.editForm.get(['lastModifiedDate']).value != null
          ? moment(this.editForm.get(['lastModifiedDate']).value, DATE_TIME_FORMAT)
          : undefined
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
    result.subscribe((res: HttpResponse<ICustomer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
