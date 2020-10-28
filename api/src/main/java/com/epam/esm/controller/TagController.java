package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
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
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;

    /**
     * Get tag by identifier
     *
     * @param id requested tag identifier
     * @return requested tag
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto getTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    /**
     * Show list of tags
     *
     * @return list of tags
     */
    @GetMapping
    @ResponseBody
    public List<TagDto> getAllTag() {
        return tagService.getAllTags();
    }

    /**
     * Create tag
     *
     * @param tag tag to create
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTag(@RequestBody TagDto tag) {
        tagService.createTag(tag);
    }

    /**
     * Delete tag. If tag linked to the certificate -
     * link will removed.
     *
     * @param id deleted tag identifier
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
    }
}
