package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.FilterDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

    /**
     * Get certificate by identifier
     *
     * @param id requested certificate identifier
     * @return requested certificate
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public CertificateDto getOne(@PathVariable Long id) {
        CertificateDto certificate = certificateService.getOne(id);
        List<TagDto> tags = certificate.getTags();
        if (tags.size() != 0) {
            tags.forEach(t -> t.add(linkTo(methodOn(TagController.class).getTag(t.getId())).withRel("tag")));
        }
        certificate.add(linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel());
        return certificate;
    }

    /**
     * Show list of certificates
     *
     * @param page current page
     * @param size number records per page
     * @return list of certificates
     */
    @RequestMapping(method = GET)
    @ResponseBody
    public CollectionModel<CertificateDto> getAll(@RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size) {
        Paginator paginator = new Paginator(size, page);
        List<CertificateDto> certificates = certificateService.getAll(paginator);
        addLink(certificates);
        return CollectionModel.of(certificates).add(linkTo(CertificateController.class).withSelfRel());
    }

    /**
     * Create certificate. If new tags are passed
     * they created in DB.
     *
     * @param certificate certificate to create
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public CertificateDto createCertificate(@RequestBody @Valid CertificateDto certificate) {
        CertificateDto certificateDto = certificateService.create(certificate);
        certificateDto.add(linkTo(CertificateController.class).slash(certificateDto.getId()).withSelfRel());
        List<TagDto> tags = certificateDto.getTags();
        if (tags.size() != 0) {
            tags.forEach(tag -> tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel()));
        }
        return certificateDto;
    }

    /**
     * Delete certificate.
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
    public CertificateDto updateCertificate(@PathVariable("id") Long id,
                                            @RequestBody @Valid CertificateDto certificateDto) {
        certificateDto.setId(id);
        CertificateDto certificate = certificateService.update(certificateDto);
        addLink(certificate);
        certificate.add(linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel());
        return certificate;
    }

    /**
     * Get certificates with tags
     *
     * @param filter introduce params to filter certificates
     *               all params are optional and can be used
     *               in conjunction see {@link FilterDto}
     */
    @RequestMapping(value = "/filter", method = GET)
    public CollectionModel<CertificateDto> filterCertificates(@RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer size,
                                                              FilterDto filter) {
        Paginator paginator = new Paginator(size, page);
        List<CertificateDto> certificates = certificateService.filter(paginator, filter);
        addLink(certificates);
        return CollectionModel.of(certificates)
                .add(linkTo(methodOn(CertificateController.class).filterCertificates(page, size, filter)).withSelfRel());
    }

    private void addLink(List<CertificateDto> certificates) {
        certificates.forEach(certificate -> {
            certificate.add(linkTo(CertificateController.class).slash(certificate.getId()).withSelfRel());
            List<TagDto> tags = certificate.getTags();
            if (tags.size() != 0) {
                tags.forEach(t -> t.add(linkTo(methodOn(TagController.class).getTag(t.getId())).withRel("tag")));
            }
        });
    }

    private void addLink(CertificateDto certificate) {
        List<TagDto> tags = certificate.getTags();
        if (tags.size() != 0) {
            tags.forEach(t -> t.add(linkTo(methodOn(TagController.class).getTag(t.getId())).withRel("tag")));
        }
    }
}
