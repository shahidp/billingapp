package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.BillingappApp;
import com.wyob.billingapp.domain.TransactionItems;
import com.wyob.billingapp.domain.Transaction;
import com.wyob.billingapp.repository.TransactionItemsRepository;
import com.wyob.billingapp.service.TransactionItemsService;
import com.wyob.billingapp.service.dto.TransactionItemsDTO;
import com.wyob.billingapp.service.mapper.TransactionItemsMapper;
import com.wyob.billingapp.web.rest.errors.ExceptionTranslator;
import com.wyob.billingapp.service.dto.TransactionItemsCriteria;
import com.wyob.billingapp.service.TransactionItemsQueryService;

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
import java.util.List;

import static com.wyob.billingapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link TransactionItemsResource} REST controller.
 */
@SpringBootTest(classes = BillingappApp.class)
public class TransactionItemsResourceIT {

    private static final String DEFAULT_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_QTY = 1D;
    private static final Double UPDATED_QTY = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private TransactionItemsRepository transactionItemsRepository;

    @Autowired
    private TransactionItemsMapper transactionItemsMapper;

    @Autowired
    private TransactionItemsService transactionItemsService;

    @Autowired
    private TransactionItemsQueryService transactionItemsQueryService;

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

    private MockMvc restTransactionItemsMockMvc;

    private TransactionItems transactionItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransactionItemsResource transactionItemsResource = new TransactionItemsResource(transactionItemsService, transactionItemsQueryService);
        this.restTransactionItemsMockMvc = MockMvcBuilders.standaloneSetup(transactionItemsResource)
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
    public static TransactionItems createEntity(EntityManager em) {
        TransactionItems transactionItems = new TransactionItems()
            .itemName(DEFAULT_ITEM_NAME)
            .qty(DEFAULT_QTY)
            .amount(DEFAULT_AMOUNT);
        // Add required entity
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            transaction = TransactionResourceIT.createEntity(em);
            em.persist(transaction);
            em.flush();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        transactionItems.setTransaction(transaction);
        return transactionItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionItems createUpdatedEntity(EntityManager em) {
        TransactionItems transactionItems = new TransactionItems()
            .itemName(UPDATED_ITEM_NAME)
            .qty(UPDATED_QTY)
            .amount(UPDATED_AMOUNT);
        // Add required entity
        Transaction transaction;
        if (TestUtil.findAll(em, Transaction.class).isEmpty()) {
            transaction = TransactionResourceIT.createUpdatedEntity(em);
            em.persist(transaction);
            em.flush();
        } else {
            transaction = TestUtil.findAll(em, Transaction.class).get(0);
        }
        transactionItems.setTransaction(transaction);
        return transactionItems;
    }

    @BeforeEach
    public void initTest() {
        transactionItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionItems() throws Exception {
        int databaseSizeBeforeCreate = transactionItemsRepository.findAll().size();

        // Create the TransactionItems
        TransactionItemsDTO transactionItemsDTO = transactionItemsMapper.toDto(transactionItems);
        restTransactionItemsMockMvc.perform(post("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemsDTO)))
            .andExpect(status().isCreated());

        // Validate the TransactionItems in the database
        List<TransactionItems> transactionItemsList = transactionItemsRepository.findAll();
        assertThat(transactionItemsList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionItems testTransactionItems = transactionItemsList.get(transactionItemsList.size() - 1);
        assertThat(testTransactionItems.getItemName()).isEqualTo(DEFAULT_ITEM_NAME);
        assertThat(testTransactionItems.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testTransactionItems.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createTransactionItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionItemsRepository.findAll().size();

        // Create the TransactionItems with an existing ID
        transactionItems.setId(1L);
        TransactionItemsDTO transactionItemsDTO = transactionItemsMapper.toDto(transactionItems);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionItemsMockMvc.perform(post("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionItems in the database
        List<TransactionItems> transactionItemsList = transactionItemsRepository.findAll();
        assertThat(transactionItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTransactionItems() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList
        restTransactionItemsMockMvc.perform(get("/api/transaction-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME.toString())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTransactionItems() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get the transactionItems
        restTransactionItemsMockMvc.perform(get("/api/transaction-items/{id}", transactionItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transactionItems.getId().intValue()))
            .andExpect(jsonPath("$.itemName").value(DEFAULT_ITEM_NAME.toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where itemName equals to DEFAULT_ITEM_NAME
        defaultTransactionItemsShouldBeFound("itemName.equals=" + DEFAULT_ITEM_NAME);

        // Get all the transactionItemsList where itemName equals to UPDATED_ITEM_NAME
        defaultTransactionItemsShouldNotBeFound("itemName.equals=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where itemName in DEFAULT_ITEM_NAME or UPDATED_ITEM_NAME
        defaultTransactionItemsShouldBeFound("itemName.in=" + DEFAULT_ITEM_NAME + "," + UPDATED_ITEM_NAME);

        // Get all the transactionItemsList where itemName equals to UPDATED_ITEM_NAME
        defaultTransactionItemsShouldNotBeFound("itemName.in=" + UPDATED_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where itemName is not null
        defaultTransactionItemsShouldBeFound("itemName.specified=true");

        // Get all the transactionItemsList where itemName is null
        defaultTransactionItemsShouldNotBeFound("itemName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where qty equals to DEFAULT_QTY
        defaultTransactionItemsShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the transactionItemsList where qty equals to UPDATED_QTY
        defaultTransactionItemsShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultTransactionItemsShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the transactionItemsList where qty equals to UPDATED_QTY
        defaultTransactionItemsShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where qty is not null
        defaultTransactionItemsShouldBeFound("qty.specified=true");

        // Get all the transactionItemsList where qty is null
        defaultTransactionItemsShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where amount equals to DEFAULT_AMOUNT
        defaultTransactionItemsShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the transactionItemsList where amount equals to UPDATED_AMOUNT
        defaultTransactionItemsShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTransactionItemsShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the transactionItemsList where amount equals to UPDATED_AMOUNT
        defaultTransactionItemsShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        // Get all the transactionItemsList where amount is not null
        defaultTransactionItemsShouldBeFound("amount.specified=true");

        // Get all the transactionItemsList where amount is null
        defaultTransactionItemsShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllTransactionItemsByTransactionIsEqualToSomething() throws Exception {
        // Get already existing entity
        Transaction transaction = transactionItems.getTransaction();
        transactionItemsRepository.saveAndFlush(transactionItems);
        Long transactionId = transaction.getId();

        // Get all the transactionItemsList where transaction equals to transactionId
        defaultTransactionItemsShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the transactionItemsList where transaction equals to transactionId + 1
        defaultTransactionItemsShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionItemsShouldBeFound(String filter) throws Exception {
        restTransactionItemsMockMvc.perform(get("/api/transaction-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemName").value(hasItem(DEFAULT_ITEM_NAME)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restTransactionItemsMockMvc.perform(get("/api/transaction-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionItemsShouldNotBeFound(String filter) throws Exception {
        restTransactionItemsMockMvc.perform(get("/api/transaction-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionItemsMockMvc.perform(get("/api/transaction-items/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTransactionItems() throws Exception {
        // Get the transactionItems
        restTransactionItemsMockMvc.perform(get("/api/transaction-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransactionItems() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        int databaseSizeBeforeUpdate = transactionItemsRepository.findAll().size();

        // Update the transactionItems
        TransactionItems updatedTransactionItems = transactionItemsRepository.findById(transactionItems.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionItems are not directly saved in db
        em.detach(updatedTransactionItems);
        updatedTransactionItems
            .itemName(UPDATED_ITEM_NAME)
            .qty(UPDATED_QTY)
            .amount(UPDATED_AMOUNT);
        TransactionItemsDTO transactionItemsDTO = transactionItemsMapper.toDto(updatedTransactionItems);

        restTransactionItemsMockMvc.perform(put("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemsDTO)))
            .andExpect(status().isOk());

        // Validate the TransactionItems in the database
        List<TransactionItems> transactionItemsList = transactionItemsRepository.findAll();
        assertThat(transactionItemsList).hasSize(databaseSizeBeforeUpdate);
        TransactionItems testTransactionItems = transactionItemsList.get(transactionItemsList.size() - 1);
        assertThat(testTransactionItems.getItemName()).isEqualTo(UPDATED_ITEM_NAME);
        assertThat(testTransactionItems.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testTransactionItems.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingTransactionItems() throws Exception {
        int databaseSizeBeforeUpdate = transactionItemsRepository.findAll().size();

        // Create the TransactionItems
        TransactionItemsDTO transactionItemsDTO = transactionItemsMapper.toDto(transactionItems);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionItemsMockMvc.perform(put("/api/transaction-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transactionItemsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionItems in the database
        List<TransactionItems> transactionItemsList = transactionItemsRepository.findAll();
        assertThat(transactionItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTransactionItems() throws Exception {
        // Initialize the database
        transactionItemsRepository.saveAndFlush(transactionItems);

        int databaseSizeBeforeDelete = transactionItemsRepository.findAll().size();

        // Delete the transactionItems
        restTransactionItemsMockMvc.perform(delete("/api/transaction-items/{id}", transactionItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<TransactionItems> transactionItemsList = transactionItemsRepository.findAll();
        assertThat(transactionItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionItems.class);
        TransactionItems transactionItems1 = new TransactionItems();
        transactionItems1.setId(1L);
        TransactionItems transactionItems2 = new TransactionItems();
        transactionItems2.setId(transactionItems1.getId());
        assertThat(transactionItems1).isEqualTo(transactionItems2);
        transactionItems2.setId(2L);
        assertThat(transactionItems1).isNotEqualTo(transactionItems2);
        transactionItems1.setId(null);
        assertThat(transactionItems1).isNotEqualTo(transactionItems2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionItemsDTO.class);
        TransactionItemsDTO transactionItemsDTO1 = new TransactionItemsDTO();
        transactionItemsDTO1.setId(1L);
        TransactionItemsDTO transactionItemsDTO2 = new TransactionItemsDTO();
        assertThat(transactionItemsDTO1).isNotEqualTo(transactionItemsDTO2);
        transactionItemsDTO2.setId(transactionItemsDTO1.getId());
        assertThat(transactionItemsDTO1).isEqualTo(transactionItemsDTO2);
        transactionItemsDTO2.setId(2L);
        assertThat(transactionItemsDTO1).isNotEqualTo(transactionItemsDTO2);
        transactionItemsDTO1.setId(null);
        assertThat(transactionItemsDTO1).isNotEqualTo(transactionItemsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(transactionItemsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(transactionItemsMapper.fromId(null)).isNull();
    }
}
