package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.service.TransactionService;
import com.wyob.billingapp.web.rest.errors.BadRequestAlertException;
import com.wyob.billingapp.service.dto.TransactionDTO;
import com.wyob.billingapp.service.dto.TransactionCriteria;
import com.wyob.billingapp.service.TransactionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wyob.billingapp.domain.Transaction}.
 */
@RestController
@RequestMapping("/api")
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;

    private final TransactionQueryService transactionQueryService;

    public TransactionResource(TransactionService transactionService, TransactionQueryService transactionQueryService) {
        this.transactionService = transactionService;
        this.transactionQueryService = transactionQueryService;
    }

    /**
     * {@code POST  /transactions} : Create a new transaction.
     *
     * @param transactionDTO the transactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionDTO, or with status {@code 400 (Bad Request)} if the transaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transactionDTO);
        if (transactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new transaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.created(new URI("/api/transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transactions} : Updates an existing transaction.
     *
     * @param transactionDTO the transactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionDTO,
     * or with status {@code 400 (Bad Request)} if the transactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transactions")
    public ResponseEntity<TransactionDTO> updateTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws URISyntaxException {
        log.debug("REST request to update Transaction : {}", transactionDTO);
        if (transactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionDTO result = transactionService.save(transactionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transactions} : get all the transactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactions in body.
     */
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(TransactionCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Transactions by criteria: {}", criteria);
        Page<TransactionDTO> page = transactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /transactions/count} : count all the transactions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/transactions/count")
    public ResponseEntity<Long> countTransactions(TransactionCriteria criteria) {
        log.debug("REST request to count Transactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transactions/:id} : get the "id" transaction.
     *
     * @param id the id of the transactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id) {
        log.debug("REST request to get Transaction : {}", id);
        Optional<TransactionDTO> transactionDTO = transactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionDTO);
    }

    /**
     * {@code DELETE  /transactions/:id} : delete the "id" transaction.
     *
     * @param id the id of the transactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        log.debug("REST request to delete Transaction : {}", id);
        transactionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
