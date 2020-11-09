package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.InvalidUserDataException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validation.TagValidatorImpl;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;
    private final TagValidatorImpl tagValidator;

    /**
     * Get tag by identifier
     *
     * @param id requested tag identifier
     * @return requested tag
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public TagDto getTag(@PathVariable("id") Long id) {
        return tagService.getOne(id);
    }

    /**
     * Show list of tags
     *
     * @return list of tags
     */
    @RequestMapping(method = GET)
    @ResponseBody
    public List<TagDto> getAllTag() {
        return tagService.getAll();
    }

    /**
     * Create tag
     *
     * @param tag tag to create
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public TagDto createTag(@RequestBody TagDto tag, BindingResult bindingResult) {
        tagValidator.validate(tag, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidUserDataException("Validation exception: ", bindingResult);
        }
        return tagService.create(tag);
    }

    /**
     * Delete tag. If tag linked to the certificate -
     * link will removed.
     *
     * @param id deleted tag identifier
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    public void deleteTag(@PathVariable("id") Long id) {
        tagService.delete(id);
    }
}
