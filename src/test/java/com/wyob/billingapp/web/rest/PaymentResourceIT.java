package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.BillingappApp;
import com.wyob.billingapp.domain.Payment;
import com.wyob.billingapp.domain.Transaction;
import com.wyob.billingapp.repository.PaymentRepository;
import com.wyob.billingapp.service.PaymentService;
import com.wyob.billingapp.service.dto.PaymentDTO;
import com.wyob.billingapp.service.mapper.PaymentMapper;
import com.wyob.billingapp.web.rest.errors.ExceptionTranslator;
import com.wyob.billingapp.service.dto.PaymentCriteria;
import com.wyob.billingapp.service.PaymentQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.wyob.billingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PaymentResource} REST controller.
 */
@SpringBootTest(classes = BillingappApp.class)
public class PaymentResourceIT {

    private static final String DEFAULT_BILL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BILL_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BILL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BILL_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_PAID_AMOUNT = 1D;
    private static final Double UPDATED_PAID_AMOUNT = 2D;

    private static final String DEFAULT_MODE_OF_PAYMENT = "AAAAAAAAAA";
    private static final String UPDATED_MODE_OF_PAYMENT = "BBBBBBBBBB";

    private static final String DEFAULT_MODE_OF_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MODE_OF_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PAYID = "AAAAAAAAAA";
    private static final String UPDATED_PAYID = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LAST_MODIFIED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_MODIFIED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentQueryService paymentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPaymentMockMvc;

    private Payment payment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentResource paymentResource = new PaymentResource(paymentService, paymentQueryService);
        this.restPaymentMockMvc = MockMvcBuilders.standaloneSetup(paymentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .billNumber(DEFAULT_BILL_NUMBER)
            .billDate(DEFAULT_BILL_DATE)
            .paidAmount(DEFAULT_PAID_AMOUNT)
            .modeOfPayment(DEFAULT_MODE_OF_PAYMENT)
            .modeOfDescription(DEFAULT_MODE_OF_DESCRIPTION)
            .payid(DEFAULT_PAYID)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            transaction = TransactionResourceIT.createEntity(em);
            em.persist(transaction);
            em.flush();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        payment.setTransaction(transaction);
        return payment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .billNumber(UPDATED_BILL_NUMBER)
            .billDate(UPDATED_BILL_DATE)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .modeOfDescription(UPDATED_MODE_OF_DESCRIPTION)
            .payid(UPDATED_PAYID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            transaction = TransactionResourceIT.createUpdatedEntity(em);
            em.persist(transaction);
            em.flush();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        payment.setTransaction(transaction);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getBillNumber()).isEqualTo(DEFAULT_BILL_NUMBER);
        assertThat(testPayment.getBillDate()).isEqualTo(DEFAULT_BILL_DATE);
        assertThat(testPayment.getPaidAmount()).isEqualTo(DEFAULT_PAID_AMOUNT);
        assertThat(testPayment.getModeOfPayment()).isEqualTo(DEFAULT_MODE_OF_PAYMENT);
        assertThat(testPayment.getModeOfDescription()).isEqualTo(DEFAULT_MODE_OF_DESCRIPTION);
        assertThat(testPayment.getPayid()).isEqualTo(DEFAULT_PAYID);
        assertThat(testPayment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testPayment.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testPayment.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testPayment.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createPaymentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBillNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setBillNumber(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].billDate").value(hasItem(DEFAULT_BILL_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPayment").value(hasItem(DEFAULT_MODE_OF_PAYMENT.toString())))
            .andExpect(jsonPath("$.[*].modeOfDescription").value(hasItem(DEFAULT_MODE_OF_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].payid").value(hasItem(DEFAULT_PAYID.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.billNumber").value(DEFAULT_BILL_NUMBER.toString()))
            .andExpect(jsonPath("$.billDate").value(DEFAULT_BILL_DATE.toString()))
            .andExpect(jsonPath("$.paidAmount").value(DEFAULT_PAID_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.modeOfPayment").value(DEFAULT_MODE_OF_PAYMENT.toString()))
            .andExpect(jsonPath("$.modeOfDescription").value(DEFAULT_MODE_OF_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.payid").value(DEFAULT_PAYID.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billNumber equals to DEFAULT_BILL_NUMBER
        defaultPaymentShouldBeFound("billNumber.equals=" + DEFAULT_BILL_NUMBER);

        // Get all the paymentList where billNumber equals to UPDATED_BILL_NUMBER
        defaultPaymentShouldNotBeFound("billNumber.equals=" + UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillNumberIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billNumber in DEFAULT_BILL_NUMBER or UPDATED_BILL_NUMBER
        defaultPaymentShouldBeFound("billNumber.in=" + DEFAULT_BILL_NUMBER + "," + UPDATED_BILL_NUMBER);

        // Get all the paymentList where billNumber equals to UPDATED_BILL_NUMBER
        defaultPaymentShouldNotBeFound("billNumber.in=" + UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billNumber is not null
        defaultPaymentShouldBeFound("billNumber.specified=true");

        // Get all the paymentList where billNumber is null
        defaultPaymentShouldNotBeFound("billNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billDate equals to DEFAULT_BILL_DATE
        defaultPaymentShouldBeFound("billDate.equals=" + DEFAULT_BILL_DATE);

        // Get all the paymentList where billDate equals to UPDATED_BILL_DATE
        defaultPaymentShouldNotBeFound("billDate.equals=" + UPDATED_BILL_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billDate in DEFAULT_BILL_DATE or UPDATED_BILL_DATE
        defaultPaymentShouldBeFound("billDate.in=" + DEFAULT_BILL_DATE + "," + UPDATED_BILL_DATE);

        // Get all the paymentList where billDate equals to UPDATED_BILL_DATE
        defaultPaymentShouldNotBeFound("billDate.in=" + UPDATED_BILL_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billDate is not null
        defaultPaymentShouldBeFound("billDate.specified=true");

        // Get all the paymentList where billDate is null
        defaultPaymentShouldNotBeFound("billDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billDate greater than or equals to DEFAULT_BILL_DATE
        defaultPaymentShouldBeFound("billDate.greaterOrEqualThan=" + DEFAULT_BILL_DATE);

        // Get all the paymentList where billDate greater than or equals to UPDATED_BILL_DATE
        defaultPaymentShouldNotBeFound("billDate.greaterOrEqualThan=" + UPDATED_BILL_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByBillDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where billDate less than or equals to DEFAULT_BILL_DATE
        defaultPaymentShouldNotBeFound("billDate.lessThan=" + DEFAULT_BILL_DATE);

        // Get all the paymentList where billDate less than or equals to UPDATED_BILL_DATE
        defaultPaymentShouldBeFound("billDate.lessThan=" + UPDATED_BILL_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByPaidAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount equals to DEFAULT_PAID_AMOUNT
        defaultPaymentShouldBeFound("paidAmount.equals=" + DEFAULT_PAID_AMOUNT);

        // Get all the paymentList where paidAmount equals to UPDATED_PAID_AMOUNT
        defaultPaymentShouldNotBeFound("paidAmount.equals=" + UPDATED_PAID_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount in DEFAULT_PAID_AMOUNT or UPDATED_PAID_AMOUNT
        defaultPaymentShouldBeFound("paidAmount.in=" + DEFAULT_PAID_AMOUNT + "," + UPDATED_PAID_AMOUNT);

        // Get all the paymentList where paidAmount equals to UPDATED_PAID_AMOUNT
        defaultPaymentShouldNotBeFound("paidAmount.in=" + UPDATED_PAID_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPaidAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paidAmount is not null
        defaultPaymentShouldBeFound("paidAmount.specified=true");

        // Get all the paymentList where paidAmount is null
        defaultPaymentShouldNotBeFound("paidAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfPaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfPayment equals to DEFAULT_MODE_OF_PAYMENT
        defaultPaymentShouldBeFound("modeOfPayment.equals=" + DEFAULT_MODE_OF_PAYMENT);

        // Get all the paymentList where modeOfPayment equals to UPDATED_MODE_OF_PAYMENT
        defaultPaymentShouldNotBeFound("modeOfPayment.equals=" + UPDATED_MODE_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfPaymentIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfPayment in DEFAULT_MODE_OF_PAYMENT or UPDATED_MODE_OF_PAYMENT
        defaultPaymentShouldBeFound("modeOfPayment.in=" + DEFAULT_MODE_OF_PAYMENT + "," + UPDATED_MODE_OF_PAYMENT);

        // Get all the paymentList where modeOfPayment equals to UPDATED_MODE_OF_PAYMENT
        defaultPaymentShouldNotBeFound("modeOfPayment.in=" + UPDATED_MODE_OF_PAYMENT);
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfPaymentIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfPayment is not null
        defaultPaymentShouldBeFound("modeOfPayment.specified=true");

        // Get all the paymentList where modeOfPayment is null
        defaultPaymentShouldNotBeFound("modeOfPayment.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfDescription equals to DEFAULT_MODE_OF_DESCRIPTION
        defaultPaymentShouldBeFound("modeOfDescription.equals=" + DEFAULT_MODE_OF_DESCRIPTION);

        // Get all the paymentList where modeOfDescription equals to UPDATED_MODE_OF_DESCRIPTION
        defaultPaymentShouldNotBeFound("modeOfDescription.equals=" + UPDATED_MODE_OF_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfDescription in DEFAULT_MODE_OF_DESCRIPTION or UPDATED_MODE_OF_DESCRIPTION
        defaultPaymentShouldBeFound("modeOfDescription.in=" + DEFAULT_MODE_OF_DESCRIPTION + "," + UPDATED_MODE_OF_DESCRIPTION);

        // Get all the paymentList where modeOfDescription equals to UPDATED_MODE_OF_DESCRIPTION
        defaultPaymentShouldNotBeFound("modeOfDescription.in=" + UPDATED_MODE_OF_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPaymentsByModeOfDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modeOfDescription is not null
        defaultPaymentShouldBeFound("modeOfDescription.specified=true");

        // Get all the paymentList where modeOfDescription is null
        defaultPaymentShouldNotBeFound("modeOfDescription.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByPayidIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payid equals to DEFAULT_PAYID
        defaultPaymentShouldBeFound("payid.equals=" + DEFAULT_PAYID);

        // Get all the paymentList where payid equals to UPDATED_PAYID
        defaultPaymentShouldNotBeFound("payid.equals=" + UPDATED_PAYID);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPayidIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payid in DEFAULT_PAYID or UPDATED_PAYID
        defaultPaymentShouldBeFound("payid.in=" + DEFAULT_PAYID + "," + UPDATED_PAYID);

        // Get all the paymentList where payid equals to UPDATED_PAYID
        defaultPaymentShouldNotBeFound("payid.in=" + UPDATED_PAYID);
    }

    @Test
    @Transactional
    public void getAllPaymentsByPayidIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where payid is not null
        defaultPaymentShouldBeFound("payid.specified=true");

        // Get all the paymentList where payid is null
        defaultPaymentShouldNotBeFound("payid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdBy equals to DEFAULT_CREATED_BY
        defaultPaymentShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the paymentList where createdBy equals to UPDATED_CREATED_BY
        defaultPaymentShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultPaymentShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the paymentList where createdBy equals to UPDATED_CREATED_BY
        defaultPaymentShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdBy is not null
        defaultPaymentShouldBeFound("createdBy.specified=true");

        // Get all the paymentList where createdBy is null
        defaultPaymentShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate equals to DEFAULT_CREATED_DATE
        defaultPaymentShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the paymentList where createdDate equals to UPDATED_CREATED_DATE
        defaultPaymentShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultPaymentShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the paymentList where createdDate equals to UPDATED_CREATED_DATE
        defaultPaymentShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is not null
        defaultPaymentShouldBeFound("createdDate.specified=true");

        // Get all the paymentList where createdDate is null
        defaultPaymentShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate greater than or equals to DEFAULT_CREATED_DATE
        defaultPaymentShouldBeFound("createdDate.greaterOrEqualThan=" + DEFAULT_CREATED_DATE);

        // Get all the paymentList where createdDate greater than or equals to UPDATED_CREATED_DATE
        defaultPaymentShouldNotBeFound("createdDate.greaterOrEqualThan=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate less than or equals to DEFAULT_CREATED_DATE
        defaultPaymentShouldNotBeFound("createdDate.lessThan=" + DEFAULT_CREATED_DATE);

        // Get all the paymentList where createdDate less than or equals to UPDATED_CREATED_DATE
        defaultPaymentShouldBeFound("createdDate.lessThan=" + UPDATED_CREATED_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultPaymentShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the paymentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPaymentShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultPaymentShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the paymentList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultPaymentShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedBy is not null
        defaultPaymentShouldBeFound("lastModifiedBy.specified=true");

        // Get all the paymentList where lastModifiedBy is null
        defaultPaymentShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPaymentShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the paymentList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPaymentShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultPaymentShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the paymentList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultPaymentShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedDate is not null
        defaultPaymentShouldBeFound("lastModifiedDate.specified=true");

        // Get all the paymentList where lastModifiedDate is null
        defaultPaymentShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedDate greater than or equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPaymentShouldBeFound("lastModifiedDate.greaterOrEqualThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the paymentList where lastModifiedDate greater than or equals to UPDATED_LAST_MODIFIED_DATE
        defaultPaymentShouldNotBeFound("lastModifiedDate.greaterOrEqualThan=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllPaymentsByLastModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where lastModifiedDate less than or equals to DEFAULT_LAST_MODIFIED_DATE
        defaultPaymentShouldNotBeFound("lastModifiedDate.lessThan=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the paymentList where lastModifiedDate less than or equals to UPDATED_LAST_MODIFIED_DATE
        defaultPaymentShouldBeFound("lastModifiedDate.lessThan=" + UPDATED_LAST_MODIFIED_DATE);
    }


    @Test
    @Transactional
    public void getAllPaymentsByTransactionIsEqualToSomething() throws Exception {
        // Get already existing entity
        Transaction transaction = payment.getTransaction();
        paymentRepository.saveAndFlush(payment);
        Long transactionId = transaction.getId();

        // Get all the paymentList where transaction equals to transactionId
        defaultPaymentShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the paymentList where transaction equals to transactionId + 1
        defaultPaymentShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER)))
            .andExpect(jsonPath("$.[*].billDate").value(hasItem(DEFAULT_BILL_DATE.toString())))
            .andExpect(jsonPath("$.[*].paidAmount").value(hasItem(DEFAULT_PAID_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].modeOfPayment").value(hasItem(DEFAULT_MODE_OF_PAYMENT)))
            .andExpect(jsonPath("$.[*].modeOfDescription").value(hasItem(DEFAULT_MODE_OF_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].payid").value(hasItem(DEFAULT_PAYID)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc.perform(get("/api/payments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc.perform(get("/api/payments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .billNumber(UPDATED_BILL_NUMBER)
            .billDate(UPDATED_BILL_DATE)
            .paidAmount(UPDATED_PAID_AMOUNT)
            .modeOfPayment(UPDATED_MODE_OF_PAYMENT)
            .modeOfDescription(UPDATED_MODE_OF_DESCRIPTION)
            .payid(UPDATED_PAYID)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getBillNumber()).isEqualTo(UPDATED_BILL_NUMBER);
        assertThat(testPayment.getBillDate()).isEqualTo(UPDATED_BILL_DATE);
        assertThat(testPayment.getPaidAmount()).isEqualTo(UPDATED_PAID_AMOUNT);
        assertThat(testPayment.getModeOfPayment()).isEqualTo(UPDATED_MODE_OF_PAYMENT);
        assertThat(testPayment.getModeOfDescription()).isEqualTo(UPDATED_MODE_OF_DESCRIPTION);
        assertThat(testPayment.getPayid()).isEqualTo(UPDATED_PAYID);
        assertThat(testPayment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testPayment.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testPayment.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testPayment.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc.perform(delete("/api/payments/{id}", payment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = new Payment();
        payment1.setId(1L);
        Payment payment2 = new Payment();
        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);
        payment2.setId(2L);
        assertThat(payment1).isNotEqualTo(payment2);
        payment1.setId(null);
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setId(1L);
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setId(paymentDTO1.getId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setId(2L);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(paymentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(paymentMapper.fromId(null)).isNull();
    }
}
