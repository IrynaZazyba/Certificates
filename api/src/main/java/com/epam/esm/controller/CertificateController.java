package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.validation.CertificateValidatorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateValidatorImpl certificateValidator;

    /**
     * Get certificate by identifier
     *
     * @param id requested certificate identifier
     * @return requested certificate
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public CertificateDto getOne(@PathVariable Long id) {
        return certificateService.getOne(id);
    }

    /**
     * Show list of certificates
     *
     * @return list of certificates
     */
    @RequestMapping(method = GET)
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
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public CertificateDto createCertificate(@RequestBody CertificateDto certificate, BindingResult bindingResult) {
        certificateValidator.validate(certificate, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDataException("Validation exception: ", bindingResult);
        }
        return certificateService.create(certificate);
    }

    /**
     * Delete certificate. If tag linked with certificate -
     * link will be deleted
     *
     * @param id certificate identifier to delete
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    public void deleteCertificate(@PathVariable("id") Long id) {
        certificateService.delete(id);
    }

    /**
     * Update certificate. If new tags are passed –
     * they will be created in DB.
     *
     * @param id             certificate identifier to update
     * @param certificateDto certificate info to update
     */
    @RequestMapping(value = "/{id}", method = PUT)
    public void updateCertificate(@PathVariable("id") Long id,
                                  @RequestBody CertificateDto certificateDto,
                                  BindingResult bindingResult) {
        certificateDto.setId(id);
        certificateValidator.validate(certificateDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDataException("Validation exception: ", bindingResult);
        }
        certificateService.update(certificateDto);
    }

    /**
     * Get certificates with tags
     *
     * @param filter introduce params to filter certificates
     *               all params are optional and can be used
     *               in conjunction see {@link FilterDto}
     */
    @RequestMapping(value = "/filter", method = GET)
    public List<CertificateDto> filterCertificates(FilterDto filter) {
        return certificateService.filter(filter);
    }

    /**
     * Allow to link tag with certificate
     *
     * @param certificateId certificate id
     * @param tagId         tag id
     */
    @RequestMapping(value = "/{certificateId}/tags/{tagId}", method = PUT)
    public void linkTagToCertificate(@PathVariable("certificateId") Long certificateId, @PathVariable("tagId") Long tagId) {
        certificateService.linkTag(certificateId, tagId);
    }


}
