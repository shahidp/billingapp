import { Moment } from 'moment';

export interface ICustomer {
  id?: number;
  fname?: string;
  lname?: string;
  mobileNo?: string;
  dob?: Moment;
  addressLine1?: string;
  addressLine2?: string;
  city?: string;
  state?: string;
  pincode?: string;
  otherDetail?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public fname?: string,
    public lname?: string,
    public mobileNo?: string,
    public dob?: Moment,
    public addressLine1?: string,
    public addressLine2?: string,
    public city?: string,
    public state?: string,
    public pincode?: string,
    public otherDetail?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment
  ) {}
}
