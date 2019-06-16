/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ICustomer, Customer } from 'app/shared/model/customer.model';

describe('Service Tests', () => {
  describe('Customer Service', () => {
    let injector: TestBed;
    let service: CustomerService;
    let httpMock: HttpTestingController;
    let elemDefault: ICustomer;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(CustomerService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Customer(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
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
            dob: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Customer', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dob: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dob: currentDate,
            createdDate: currentDate,
            lastModifiedDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Customer(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Customer', async () => {
        const returnedFromService = Object.assign(
          {
            fname: 'BBBBBB',
            lname: 'BBBBBB',
            mobileNo: 'BBBBBB',
            dob: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            pincode: 'BBBBBB',
            otherDetail: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate,
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

      it('should return a list of Customer', async () => {
        const returnedFromService = Object.assign(
          {
            fname: 'BBBBBB',
            lname: 'BBBBBB',
            mobileNo: 'BBBBBB',
            dob: currentDate.format(DATE_FORMAT),
            addressLine1: 'BBBBBB',
            addressLine2: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            pincode: 'BBBBBB',
            otherDetail: 'BBBBBB',
            createdBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT),
            lastModifiedBy: 'BBBBBB',
            lastModifiedDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dob: currentDate,
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

      it('should delete a Customer', async () => {
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
