/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PaymentService } from 'app/entities/payment/payment.service';
import { IPayment, Payment } from 'app/shared/model/payment.model';

describe('Service Tests', () => {
  describe('Payment Service', () => {
    let injector: TestBed;
    let service: PaymentService;
    let httpMock: HttpTestingController;
    let elemDefault: IPayment;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(PaymentService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Payment(
        0,
        'AAAAAAA',
        currentDate,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            billDate: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Payment', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            billDate: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            billDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Payment(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Payment', async () => {
        const returnedFromService = Object.assign(
          {
            billNumber: 'BBBBBB',
            billDate: currentDate.format(DATE_FORMAT),
            paidAmount: 1,
            modeOfPayment: 'BBBBBB',
            modeOfDescription: 'BBBBBB',
            payid: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            billDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Payment', async () => {
        const returnedFromService = Object.assign(
          {
            billNumber: 'BBBBBB',
            billDate: currentDate.format(DATE_FORMAT),
            paidAmount: 1,
            modeOfPayment: 'BBBBBB',
            modeOfDescription: 'BBBBBB',
            payid: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            billDate: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Payment', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
