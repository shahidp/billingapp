package com.wyob.billingapp.web.rest;

import com.wyob.billingapp.service.TransactionItemsService;
import com.wyob.billingapp.web.rest.errors.BadRequestAlertException;
import com.wyob.billingapp.service.dto.TransactionItemsDTO;
import com.wyob.billingapp.service.dto.TransactionItemsCriteria;
import com.wyob.billingapp.service.TransactionItemsQueryService;

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
 * REST controller for managing {@link com.wyob.billingapp.domain.TransactionItems}.
 */
@RestController
@RequestMapping("/api")
public class TransactionItemsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionItemsResource.class);

    private static final String ENTITY_NAME = "transactionItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionItemsService transactionItemsService;

    private final TransactionItemsQueryService transactionItemsQueryService;

    public TransactionItemsResource(TransactionItemsService transactionItemsService, TransactionItemsQueryService transactionItemsQueryService) {
        this.transactionItemsService = transactionItemsService;
        this.transactionItemsQueryService = transactionItemsQueryService;
    }

    /**
     * {@code POST  /transaction-items} : Create a new transactionItems.
     *
     * @param transactionItemsDTO the transactionItemsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionItemsDTO, or with status {@code 400 (Bad Request)} if the transactionItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-items")
    public ResponseEntity<TransactionItemsDTO> createTransactionItems(@Valid @RequestBody TransactionItemsDTO transactionItemsDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionItems : {}", transactionItemsDTO);
        if (transactionItemsDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionItemsDTO result = transactionItemsService.save(transactionItemsDTO);
        return ResponseEntity.created(new URI("/api/transaction-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-items} : Updates an existing transactionItems.
     *
     * @param transactionItemsDTO the transactionItemsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionItemsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionItemsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionItemsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-items")
    public ResponseEntity<TransactionItemsDTO> updateTransactionItems(@Valid @RequestBody TransactionItemsDTO transactionItemsDTO) throws URISyntaxException {
        log.debug("REST request to update TransactionItems : {}", transactionItemsDTO);
        if (transactionItemsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TransactionItemsDTO result = transactionItemsService.save(transactionItemsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionItemsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /transaction-items} : get all the transactionItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionItems in body.
     */
    @GetMapping("/transaction-items")
    public ResponseEntity<List<TransactionItemsDTO>> getAllTransactionItems(TransactionItemsCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get TransactionItems by criteria: {}", criteria);
        Page<TransactionItemsDTO> page = transactionItemsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /transaction-items/count} : count all the transactionItems.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/transaction-items/count")
    public ResponseEntity<Long> countTransactionItems(TransactionItemsCriteria criteria) {
        log.debug("REST request to count TransactionItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionItemsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-items/:id} : get the "id" transactionItems.
     *
     * @param id the id of the transactionItemsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionItemsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-items/{id}")
    public ResponseEntity<TransactionItemsDTO> getTransactionItems(@PathVariable Long id) {
        log.debug("REST request to get TransactionItems : {}", id);
        Optional<TransactionItemsDTO> transactionItemsDTO = transactionItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionItemsDTO);
    }

    /**
     * {@code DELETE  /transaction-items/:id} : delete the "id" transactionItems.
     *
     * @param id the id of the transactionItemsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-items/{id}")
    public ResponseEntity<Void> deleteTransactionItems(@PathVariable Long id) {
        log.debug("REST request to delete TransactionItems : {}", id);
        transactionItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
