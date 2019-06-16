package com.wyob.billingapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.wyob.billingapp.domain.Customer} entity. This class is used
 * in {@link com.wyob.billingapp.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fname;

    private StringFilter lname;

    private StringFilter mobileNo;

    private LocalDateFilter dob;

    private StringFilter addressLine1;

    private StringFilter addressLine2;

    private StringFilter city;

    private StringFilter state;

    private StringFilter pincode;

    private StringFilter otherDetail;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    public CustomerCriteria(){
    }

    public CustomerCriteria(CustomerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.fname = other.fname == null ? null : other.fname.copy();
        this.lname = other.lname == null ? null : other.lname.copy();
        this.mobileNo = other.mobileNo == null ? null : other.mobileNo.copy();
        this.dob = other.dob == null ? null : other.dob.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.addressLine2 = other.addressLine2 == null ? null : other.addressLine2.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.pincode = other.pincode == null ? null : other.pincode.copy();
        this.otherDetail = other.otherDetail == null ? null : other.otherDetail.copy();
        this.createdBy = other.createdBy == null ? null : other.createdBy.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFname() {
        return fname;
    }

    public void setFname(StringFilter fname) {
        this.fname = fname;
    }

    public StringFilter getLname() {
        return lname;
    }

    public void setLname(StringFilter lname) {
        this.lname = lname;
    }

    public StringFilter getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(StringFilter mobileNo) {
        this.mobileNo = mobileNo;
    }

    public LocalDateFilter getDob() {
        return dob;
    }

    public void setDob(LocalDateFilter dob) {
        this.dob = dob;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public StringFilter getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(StringFilter addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getPincode() {
        return pincode;
    }

    public void setPincode(StringFilter pincode) {
        this.pincode = pincode;
    }

    public StringFilter getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(StringFilter otherDetail) {
        this.otherDetail = otherDetail;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fname, that.fname) &&
            Objects.equals(lname, that.lname) &&
            Objects.equals(mobileNo, that.mobileNo) &&
            Objects.equals(dob, that.dob) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(addressLine2, that.addressLine2) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(pincode, that.pincode) &&
            Objects.equals(otherDetail, that.otherDetail) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fname,
        lname,
        mobileNo,
        dob,
        addressLine1,
        addressLine2,
        city,
        state,
        pincode,
        otherDetail,
        createdBy,
        createdDate,
        lastModifiedBy,
        lastModifiedDate
        );
    }

    @Override
    public String toString() {
        return "CustomerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fname != null ? "fname=" + fname + ", " : "") +
                (lname != null ? "lname=" + lname + ", " : "") +
                (mobileNo != null ? "mobileNo=" + mobileNo + ", " : "") +
                (dob != null ? "dob=" + dob + ", " : "") +
                (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
                (addressLine2 != null ? "addressLine2=" + addressLine2 + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (pincode != null ? "pincode=" + pincode + ", " : "") +
                (otherDetail != null ? "otherDetail=" + otherDetail + ", " : "") +
                (createdBy != null ? "createdBy=" + createdBy + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
                (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            "}";
    }

}
