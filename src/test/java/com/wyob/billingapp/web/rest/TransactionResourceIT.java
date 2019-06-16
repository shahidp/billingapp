package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.BillingappApp;
import com.wyob.billingapp.domain.Transaction;
import com.wyob.billingapp.domain.Customer;
import com.wyob.billingapp.repository.TransactionRepository;
import com.wyob.billingapp.service.TransactionService;
import com.wyob.billingapp.service.dto.TransactionDTO;
import com.wyob.billingapp.service.mapper.TransactionMapper;
import com.wyob.billingapp.web.rest.errors.ExceptionTranslator;
import com.wyob.billingapp.service.dto.TransactionCriteria;
import com.wyob.billingapp.service.TransactionQueryService;

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
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.wyob.billingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TransactionResource} REST controller.
 */
@SpringBootTest(classes = BillingappApp.class)
public class TransactionResourceIT {

    private static final String DEFAULT_TRANSACTION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_TRN_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRN_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_TOTAL_AMOUNT = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT = 2D;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ITEMS = "AAAAAAAAAA";
    private static final String UPDATED_ITEMS = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionQueryService transactionQueryService;

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

    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionResource transactionResource = new TransactionResource(transactionService, transactionQueryService);
        this.restTransactionMockMvc = MockMvcBuilders.standaloneSetup(transactionResource)
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
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .transactionNumber(DEFAULT_TRANSACTION_NUMBER)
            .trnDate(DEFAULT_TRN_DATE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .description(DEFAULT_DESCRIPTION)
            .items(DEFAULT_ITEMS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        transaction.setCustomer(customer);
        return transaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .trnDate(UPDATED_TRN_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .items(UPDATED_ITEMS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        transaction.setCustomer(customer);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionNumber()).isEqualTo(DEFAULT_TRANSACTION_NUMBER);
        assertThat(testTransaction.getTrnDate()).isEqualTo(DEFAULT_TRN_DATE);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTransaction.getItems()).isEqualTo(DEFAULT_ITEMS);
        assertThat(testTransaction.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTransaction.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTransaction.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTransaction.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // Create the Transaction with an existing ID
        transaction.setId(1L);
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTransactionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTransactionNumber(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTrnDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTrnDate(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionRepository.findAll().size();
        // set the field null
        transaction.setTotalAmount(null);

        // Create the Transaction, which fails.
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        restTransactionMockMvc.perform(post("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].trnDate").value(hasItem(DEFAULT_TRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].items").value(hasItem(DEFAULT_ITEMS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionNumber").value(DEFAULT_TRANSACTION_NUMBER.toString()))
            .andExpect(jsonPath("$.trnDate").value(DEFAULT_TRN_DATE.toString()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.items").value(DEFAULT_ITEMS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionNumber equals to DEFAULT_TRANSACTION_NUMBER
        defaultTransactionShouldBeFound("transactionNumber.equals=" + DEFAULT_TRANSACTION_NUMBER);

        // Get all the transactionList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultTransactionShouldNotBeFound("transactionNumber.equals=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionNumber in DEFAULT_TRANSACTION_NUMBER or UPDATED_TRANSACTION_NUMBER
        defaultTransactionShouldBeFound("transactionNumber.in=" + DEFAULT_TRANSACTION_NUMBER + "," + UPDATED_TRANSACTION_NUMBER);

        // Get all the transactionList where transactionNumber equals to UPDATED_TRANSACTION_NUMBER
        defaultTransactionShouldNotBeFound("transactionNumber.in=" + UPDATED_TRANSACTION_NUMBER);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTransactionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where transactionNumber is not null
        defaultTransactionShouldBeFound("transactionNumber.specified=true");

        // Get all the transactionList where transactionNumber is null
        defaultTransactionShouldNotBeFound("transactionNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByTrnDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where trnDate equals to DEFAULT_TRN_DATE
        defaultTransactionShouldBeFound("trnDate.equals=" + DEFAULT_TRN_DATE);

        // Get all the transactionList where trnDate equals to UPDATED_TRN_DATE
        defaultTransactionShouldNotBeFound("trnDate.equals=" + UPDATED_TRN_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTrnDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where trnDate in DEFAULT_TRN_DATE or UPDATED_TRN_DATE
        defaultTransactionShouldBeFound("trnDate.in=" + DEFAULT_TRN_DATE + "," + UPDATED_TRN_DATE);

        // Get all the transactionList where trnDate equals to UPDATED_TRN_DATE
        defaultTransactionShouldNotBeFound("trnDate.in=" + UPDATED_TRN_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTrnDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where trnDate is not null
        defaultTransactionShouldBeFound("trnDate.specified=true");

        // Get all the transactionList where trnDate is null
        defaultTransactionShouldNotBeFound("trnDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByTrnDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where trnDate greater than or equals to DEFAULT_TRN_DATE
        defaultTransactionShouldBeFound("trnDate.greaterOrEqualThan=" + DEFAULT_TRN_DATE);

        // Get all the transactionList where trnDate greater than or equals to UPDATED_TRN_DATE
        defaultTransactionShouldNotBeFound("trnDate.greaterOrEqualThan=" + UPDATED_TRN_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTrnDateIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where trnDate less than or equals to DEFAULT_TRN_DATE
        defaultTransactionShouldNotBeFound("trnDate.lessThan=" + DEFAULT_TRN_DATE);

        // Get all the transactionList where trnDate less than or equals to UPDATED_TRN_DATE
        defaultTransactionShouldBeFound("trnDate.lessThan=" + UPDATED_TRN_DATE);
    }


    @Test
    @Transactional
    public void getAllTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is not null
        defaultTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the transactionList where totalAmount is null
        defaultTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description equals to DEFAULT_DESCRIPTION
        defaultTransactionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the transactionList where description equals to UPDATED_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTransactionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTransactionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the transactionList where description equals to UPDATED_DESCRIPTION
        defaultTransactionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTransactionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where description is not null
        defaultTransactionShouldBeFound("description.specified=true");

        // Get all the transactionList where description is null
        defaultTransactionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByItemsIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where items equals to DEFAULT_ITEMS
        defaultTransactionShouldBeFound("items.equals=" + DEFAULT_ITEMS);

        // Get all the transactionList where items equals to UPDATED_ITEMS
        defaultTransactionShouldNotBeFound("items.equals=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    public void getAllTransactionsByItemsIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where items in DEFAULT_ITEMS or UPDATED_ITEMS
        defaultTransactionShouldBeFound("items.in=" + DEFAULT_ITEMS + "," + UPDATED_ITEMS);

        // Get all the transactionList where items equals to UPDATED_ITEMS
        defaultTransactionShouldNotBeFound("items.in=" + UPDATED_ITEMS);
    }

    @Test
    @Transactional
    public void getAllTransactionsByItemsIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where items is not null
        defaultTransactionShouldBeFound("items.specified=true");

        // Get all the transactionList where items is null
        defaultTransactionShouldNotBeFound("items.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy equals to DEFAULT_CREATED_BY
        defaultTransactionShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the transactionList where createdBy equals to UPDATED_CREATED_BY
        defaultTransactionShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTransactionShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the transactionList where createdBy equals to UPDATED_CREATED_BY
        defaultTransactionShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdBy is not null
        defaultTransactionShouldBeFound("createdBy.specified=true");

        // Get all the transactionList where createdBy is null
        defaultTransactionShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTransactionShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the transactionList where createdDate equals to UPDATED_CREATED_DATE
        defaultTransactionShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTransactionShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the transactionList where createdDate equals to UPDATED_CREATED_DATE
        defaultTransactionShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where createdDate is not null
        defaultTransactionShouldBeFound("createdDate.specified=true");

        // Get all the transactionList where createdDate is null
        defaultTransactionShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultTransactionShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the transactionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTransactionShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultTransactionShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the transactionList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTransactionShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedBy is not null
        defaultTransactionShouldBeFound("lastModifiedBy.specified=true");

        // Get all the transactionList where lastModifiedBy is null
        defaultTransactionShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultTransactionShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the transactionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTransactionShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultTransactionShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the transactionList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTransactionShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllTransactionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where lastModifiedDate is not null
        defaultTransactionShouldBeFound("lastModifiedDate.specified=true");

        // Get all the transactionList where lastModifiedDate is null
        defaultTransactionShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionsByCustomerIsEqualToSomething() throws Exception {
        // Get already existing entity
        Customer customer = transaction.getCustomer();
        transactionRepository.saveAndFlush(transaction);
        Long customerId = customer.getId();

        // Get all the transactionList where customer equals to customerId
        defaultTransactionShouldBeFound("customerId.equals=" + customerId);

        // Get all the transactionList where customer equals to customerId + 1
        defaultTransactionShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionNumber").value(hasItem(DEFAULT_TRANSACTION_NUMBER)))
            .andExpect(jsonPath("$.[*].trnDate").value(hasItem(DEFAULT_TRN_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].items").value(hasItem(DEFAULT_ITEMS)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransactionMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc.perform(get("/api/transactions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionMockMvc.perform(get("/api/transactions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get("/api/transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .transactionNumber(UPDATED_TRANSACTION_NUMBER)
            .trnDate(UPDATED_TRN_DATE)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .description(UPDATED_DESCRIPTION)
            .items(UPDATED_ITEMS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        TransactionDTO transactionDTO = transactionMapper.toDto(updatedTransaction);

        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTransactionNumber()).isEqualTo(UPDATED_TRANSACTION_NUMBER);
        assertThat(testTransaction.getTrnDate()).isEqualTo(UPDATED_TRN_DATE);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTransaction.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTransaction.getItems()).isEqualTo(UPDATED_ITEMS);
        assertThat(testTransaction.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTransaction.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTransaction.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTransaction.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Create the Transaction
        TransactionDTO transactionDTO = transactionMapper.toDto(transaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc.perform(put("/api/transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc.perform(delete("/api/transactions/{id}", transaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        Transaction transaction2 = new Transaction();
        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);
        transaction2.setId(2L);
        assertThat(transaction1).isNotEqualTo(transaction2);
        transaction1.setId(null);
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionDTO.class);
        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(1L);
        TransactionDTO transactionDTO2 = new TransactionDTO();
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO2.setId(transactionDTO1.getId());
        assertThat(transactionDTO1).isEqualTo(transactionDTO2);
        transactionDTO2.setId(2L);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
        transactionDTO1.setId(null);
        assertThat(transactionDTO1).isNotEqualTo(transactionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionMapper.fromId(null)).isNull();
    }
}
