export interface ITransactionItems {
  id?: number;
  itemName?: string;
  qty?: number;
  amount?: number;
  transactionId?: number;
}

export class TransactionItems implements ITransactionItems {
  constructor(public id?: number, public itemName?: string, public qty?: number, public amount?: number, public transactionId?: number) {}
}
