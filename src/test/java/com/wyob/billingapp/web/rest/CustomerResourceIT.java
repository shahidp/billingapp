package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.BillingappApp;
import com.wyob.billingapp.domain.Customer;
import com.wyob.billingapp.repository.CustomerRepository;
import com.wyob.billingapp.service.CustomerService;
import com.wyob.billingapp.service.dto.CustomerDTO;
import com.wyob.billingapp.service.mapper.CustomerMapper;
import com.wyob.billingapp.web.rest.errors.ExceptionTranslator;
import com.wyob.billingapp.service.dto.CustomerCriteria;
import com.wyob.billingapp.service.CustomerQueryService;

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
 * Integration tests for the {@Link CustomerResource} REST controller.
 */
@SpringBootTest(classes = BillingappApp.class)
public class CustomerResourceIT {

    private static final String DEFAULT_FNAME = "AAAAAAAAAA";
    private static final String UPDATED_FNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LNAME = "AAAAAAAAAA";
    private static final String UPDATED_LNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_NO = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_NO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_DETAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerQueryService customerQueryService;

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

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerResource customerResource = new CustomerResource(customerService, customerQueryService);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
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
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .fname(DEFAULT_FNAME)
            .lname(DEFAULT_LNAME)
            .mobileNo(DEFAULT_MOBILE_NO)
            .dob(DEFAULT_DOB)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .pincode(DEFAULT_PINCODE)
            .otherDetail(DEFAULT_OTHER_DETAIL)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return customer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME)
            .mobileNo(UPDATED_MOBILE_NO)
            .dob(UPDATED_DOB)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .otherDetail(UPDATED_OTHER_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testCustomer.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testCustomer.getMobileNo()).isEqualTo(DEFAULT_MOBILE_NO);
        assertThat(testCustomer.getDob()).isEqualTo(DEFAULT_DOB);
        assertThat(testCustomer.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testCustomer.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testCustomer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomer.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testCustomer.getPincode()).isEqualTo(DEFAULT_PINCODE);
        assertThat(testCustomer.getOtherDetail()).isEqualTo(DEFAULT_OTHER_DETAIL);
        assertThat(testCustomer.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCustomer.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testCustomer.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer with an existing ID
        customer.setId(1L);
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setFname(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setLname(null);

        // Create the Customer, which fails.
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        restCustomerMockMvc.perform(post("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME.toString())))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME.toString())))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO.toString())))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1.toString())))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE.toString())))
            .andExpect(jsonPath("$.[*].otherDetail").value(hasItem(DEFAULT_OTHER_DETAIL.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME.toString()))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME.toString()))
            .andExpect(jsonPath("$.mobileNo").value(DEFAULT_MOBILE_NO.toString()))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1.toString()))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE.toString()))
            .andExpect(jsonPath("$.otherDetail").value(DEFAULT_OTHER_DETAIL.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllCustomersByFnameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fname equals to DEFAULT_FNAME
        defaultCustomerShouldBeFound("fname.equals=" + DEFAULT_FNAME);

        // Get all the customerList where fname equals to UPDATED_FNAME
        defaultCustomerShouldNotBeFound("fname.equals=" + UPDATED_FNAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByFnameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fname in DEFAULT_FNAME or UPDATED_FNAME
        defaultCustomerShouldBeFound("fname.in=" + DEFAULT_FNAME + "," + UPDATED_FNAME);

        // Get all the customerList where fname equals to UPDATED_FNAME
        defaultCustomerShouldNotBeFound("fname.in=" + UPDATED_FNAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByFnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where fname is not null
        defaultCustomerShouldBeFound("fname.specified=true");

        // Get all the customerList where fname is null
        defaultCustomerShouldNotBeFound("fname.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLnameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lname equals to DEFAULT_LNAME
        defaultCustomerShouldBeFound("lname.equals=" + DEFAULT_LNAME);

        // Get all the customerList where lname equals to UPDATED_LNAME
        defaultCustomerShouldNotBeFound("lname.equals=" + UPDATED_LNAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLnameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lname in DEFAULT_LNAME or UPDATED_LNAME
        defaultCustomerShouldBeFound("lname.in=" + DEFAULT_LNAME + "," + UPDATED_LNAME);

        // Get all the customerList where lname equals to UPDATED_LNAME
        defaultCustomerShouldNotBeFound("lname.in=" + UPDATED_LNAME);
    }

    @Test
    @Transactional
    public void getAllCustomersByLnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lname is not null
        defaultCustomerShouldBeFound("lname.specified=true");

        // Get all the customerList where lname is null
        defaultCustomerShouldNotBeFound("lname.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileNoIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNo equals to DEFAULT_MOBILE_NO
        defaultCustomerShouldBeFound("mobileNo.equals=" + DEFAULT_MOBILE_NO);

        // Get all the customerList where mobileNo equals to UPDATED_MOBILE_NO
        defaultCustomerShouldNotBeFound("mobileNo.equals=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileNoIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNo in DEFAULT_MOBILE_NO or UPDATED_MOBILE_NO
        defaultCustomerShouldBeFound("mobileNo.in=" + DEFAULT_MOBILE_NO + "," + UPDATED_MOBILE_NO);

        // Get all the customerList where mobileNo equals to UPDATED_MOBILE_NO
        defaultCustomerShouldNotBeFound("mobileNo.in=" + UPDATED_MOBILE_NO);
    }

    @Test
    @Transactional
    public void getAllCustomersByMobileNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobileNo is not null
        defaultCustomerShouldBeFound("mobileNo.specified=true");

        // Get all the customerList where mobileNo is null
        defaultCustomerShouldNotBeFound("mobileNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByDobIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dob equals to DEFAULT_DOB
        defaultCustomerShouldBeFound("dob.equals=" + DEFAULT_DOB);

        // Get all the customerList where dob equals to UPDATED_DOB
        defaultCustomerShouldNotBeFound("dob.equals=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllCustomersByDobIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dob in DEFAULT_DOB or UPDATED_DOB
        defaultCustomerShouldBeFound("dob.in=" + DEFAULT_DOB + "," + UPDATED_DOB);

        // Get all the customerList where dob equals to UPDATED_DOB
        defaultCustomerShouldNotBeFound("dob.in=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllCustomersByDobIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dob is not null
        defaultCustomerShouldBeFound("dob.specified=true");

        // Get all the customerList where dob is null
        defaultCustomerShouldNotBeFound("dob.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByDobIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dob greater than or equals to DEFAULT_DOB
        defaultCustomerShouldBeFound("dob.greaterOrEqualThan=" + DEFAULT_DOB);

        // Get all the customerList where dob greater than or equals to UPDATED_DOB
        defaultCustomerShouldNotBeFound("dob.greaterOrEqualThan=" + UPDATED_DOB);
    }

    @Test
    @Transactional
    public void getAllCustomersByDobIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dob less than or equals to DEFAULT_DOB
        defaultCustomerShouldNotBeFound("dob.lessThan=" + DEFAULT_DOB);

        // Get all the customerList where dob less than or equals to UPDATED_DOB
        defaultCustomerShouldBeFound("dob.lessThan=" + UPDATED_DOB);
    }


    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the customerList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultCustomerShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine1 is not null
        defaultCustomerShouldBeFound("addressLine1.specified=true");

        // Get all the customerList where addressLine1 is null
        defaultCustomerShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the customerList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultCustomerShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    public void getAllCustomersByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where addressLine2 is not null
        defaultCustomerShouldBeFound("addressLine2.specified=true");

        // Get all the customerList where addressLine2 is null
        defaultCustomerShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city equals to DEFAULT_CITY
        defaultCustomerShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCustomerShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the customerList where city equals to UPDATED_CITY
        defaultCustomerShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where city is not null
        defaultCustomerShouldBeFound("city.specified=true");

        // Get all the customerList where city is null
        defaultCustomerShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where state equals to DEFAULT_STATE
        defaultCustomerShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the customerList where state equals to UPDATED_STATE
        defaultCustomerShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByStateIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where state in DEFAULT_STATE or UPDATED_STATE
        defaultCustomerShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the customerList where state equals to UPDATED_STATE
        defaultCustomerShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where state is not null
        defaultCustomerShouldBeFound("state.specified=true");

        // Get all the customerList where state is null
        defaultCustomerShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where pincode equals to DEFAULT_PINCODE
        defaultCustomerShouldBeFound("pincode.equals=" + DEFAULT_PINCODE);

        // Get all the customerList where pincode equals to UPDATED_PINCODE
        defaultCustomerShouldNotBeFound("pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where pincode in DEFAULT_PINCODE or UPDATED_PINCODE
        defaultCustomerShouldBeFound("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE);

        // Get all the customerList where pincode equals to UPDATED_PINCODE
        defaultCustomerShouldNotBeFound("pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    public void getAllCustomersByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where pincode is not null
        defaultCustomerShouldBeFound("pincode.specified=true");

        // Get all the customerList where pincode is null
        defaultCustomerShouldNotBeFound("pincode.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByOtherDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where otherDetail equals to DEFAULT_OTHER_DETAIL
        defaultCustomerShouldBeFound("otherDetail.equals=" + DEFAULT_OTHER_DETAIL);

        // Get all the customerList where otherDetail equals to UPDATED_OTHER_DETAIL
        defaultCustomerShouldNotBeFound("otherDetail.equals=" + UPDATED_OTHER_DETAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByOtherDetailIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where otherDetail in DEFAULT_OTHER_DETAIL or UPDATED_OTHER_DETAIL
        defaultCustomerShouldBeFound("otherDetail.in=" + DEFAULT_OTHER_DETAIL + "," + UPDATED_OTHER_DETAIL);

        // Get all the customerList where otherDetail equals to UPDATED_OTHER_DETAIL
        defaultCustomerShouldNotBeFound("otherDetail.in=" + UPDATED_OTHER_DETAIL);
    }

    @Test
    @Transactional
    public void getAllCustomersByOtherDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where otherDetail is not null
        defaultCustomerShouldBeFound("otherDetail.specified=true");

        // Get all the customerList where otherDetail is null
        defaultCustomerShouldNotBeFound("otherDetail.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdBy equals to DEFAULT_CREATED_BY
        defaultCustomerShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the customerList where createdBy equals to UPDATED_CREATED_BY
        defaultCustomerShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultCustomerShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the customerList where createdBy equals to UPDATED_CREATED_BY
        defaultCustomerShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdBy is not null
        defaultCustomerShouldBeFound("createdBy.specified=true");

        // Get all the customerList where createdBy is null
        defaultCustomerShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdDate equals to DEFAULT_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the customerList where createdDate equals to UPDATED_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultCustomerShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the customerList where createdDate equals to UPDATED_CREATED_DATE
        defaultCustomerShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where createdDate is not null
        defaultCustomerShouldBeFound("createdDate.specified=true");

        // Get all the customerList where createdDate is null
        defaultCustomerShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultCustomerShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the customerList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCustomerShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultCustomerShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the customerList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultCustomerShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedBy is not null
        defaultCustomerShouldBeFound("lastModifiedBy.specified=true");

        // Get all the customerList where lastModifiedBy is null
        defaultCustomerShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultCustomerShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the customerList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCustomerShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultCustomerShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the customerList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultCustomerShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void getAllCustomersByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastModifiedDate is not null
        defaultCustomerShouldBeFound("lastModifiedDate.specified=true");

        // Get all the customerList where lastModifiedDate is null
        defaultCustomerShouldNotBeFound("lastModifiedDate.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME)))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME)))
            .andExpect(jsonPath("$.[*].mobileNo").value(hasItem(DEFAULT_MOBILE_NO)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].otherDetail").value(hasItem(DEFAULT_OTHER_DETAIL)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc.perform(get("/api/customers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME)
            .mobileNo(UPDATED_MOBILE_NO)
            .dob(UPDATED_DOB)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .otherDetail(UPDATED_OTHER_DETAIL)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        CustomerDTO customerDTO = customerMapper.toDto(updatedCustomer);

        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testCustomer.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testCustomer.getMobileNo()).isEqualTo(UPDATED_MOBILE_NO);
        assertThat(testCustomer.getDob()).isEqualTo(UPDATED_DOB);
        assertThat(testCustomer.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCustomer.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCustomer.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomer.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testCustomer.getPincode()).isEqualTo(UPDATED_PINCODE);
        assertThat(testCustomer.getOtherDetail()).isEqualTo(UPDATED_OTHER_DETAIL);
        assertThat(testCustomer.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCustomer.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCustomer.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testCustomer.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.toDto(customer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc.perform(put("/api/customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = new Customer();
        customer1.setId(1L);
        Customer customer2 = new Customer();
        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);
        customer2.setId(2L);
        assertThat(customer1).isNotEqualTo(customer2);
        customer1.setId(null);
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerDTO.class);
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(1L);
        CustomerDTO customerDTO2 = new CustomerDTO();
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO2.setId(customerDTO1.getId());
        assertThat(customerDTO1).isEqualTo(customerDTO2);
        customerDTO2.setId(2L);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO1.setId(null);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(customerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(customerMapper.fromId(null)).isNull();
    }
}
