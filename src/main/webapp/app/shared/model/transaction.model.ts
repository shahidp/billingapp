import { Moment } from 'moment';

export interface ITransaction {
  id?: number;
  transactionNumber?: string;
  trnDate?: Moment;
  totalAmount?: number;
  description?: string;
  items?: string;
  createdBy?: string;
  createdDate?: Moment;
  lastModifiedBy?: string;
  lastModifiedDate?: Moment;
  customerFname?: string;
  customerId?: number;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public transactionNumber?: string,
    public trnDate?: Moment,
    public totalAmount?: number,
    public description?: string,
    public items?: string,
    public createdBy?: string,
    public createdDate?: Moment,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Moment,
    public customerFname?: string,
    public customerId?: number
  ) {}
}
