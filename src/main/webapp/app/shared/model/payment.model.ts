import { Moment } from 'moment';

export interface IPayment {
  id?: number;
  billNumber?: string;
  billDate?: Moment;
  paidAmount?: number;
  modeOfPayment?: string;
  modeOfDescription?: string;
  payid?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  transactionId?: number;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public billNumber?: string,
    public billDate?: Moment,
    public paidAmount?: number,
    public modeOfPayment?: string,
    public modeOfDescription?: string,
    public payid?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public transactionId?: number
  ) {}
}
