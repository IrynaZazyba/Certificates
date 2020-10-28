package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    /**
     * Get certificate by identifier
     *
     * @param id requested certificate identifier
     * @return requested certificate
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CertificateDto getOne(@PathVariable Long id) {
        return certificateService.getOne(id);
    }

    /**
     * Show list of certificates
     *
     * @return list of certificates
     */
    @GetMapping
    @ResponseBody
    public List<CertificateDto> getAll() {
        return certificateService.getAll();
    }

    /**
     * Create certificate. If new tags are passed
     * they created in DB.
     *
     * @param certificate certificate to create
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCertificate(@RequestBody CertificateDto certificate) {
        certificateService.createCertificate(certificate);
    }

    /**
     * Delete certificate. If tag linked with certificate -
     * link will be deleted
     *
     * @param id certificate identifier to delete
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteCertificate(@PathVariable("id") Long id) {
        certificateService.deleteCertificate(id);
    }

    /**
     * Update certificate. If new tags are passed –
     * they will be created in DB.
     *
     * @param id             certificate identifier to update
     * @param certificateDto certificate info to update
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateCertificate(@PathVariable("id") Long id,
                                  @RequestBody CertificateDto certificateDto) {
        certificateDto.setId(id);
        certificateService.updateCertificate(certificateDto);
    }

    /**
     * Get certificates with tags
     *
     * @param filter introduce params to filter certificates
     *               all params are optional and can be used
     *               in conjunction see {@link FilterDto}
     */
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public List<CertificateDto> filterCertificates(FilterDto filter) {
        return certificateService.filterCertificate(filter);
    }

    /**
     * Allow to link tag with certificate
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     */
    @RequestMapping(value = "/{certificateId}/tags/{tagId}", method = RequestMethod.PUT)
    public void linkTagToCertificate(@PathVariable("certificateId") Long certificateId, @PathVariable("tagId") Long tagId) {
        certificateService.linkTagToCertificate(certificateId, tagId);
    }


}
