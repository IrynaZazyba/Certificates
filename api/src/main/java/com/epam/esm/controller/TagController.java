package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Paginator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
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

    /**
     * Get tag by identifier
     *
     * @param id requested tag identifier
     * @return requested tag
     */
    @RequestMapping(value = "/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public TagDto getTag(@PathVariable("id") Long id) {
        TagDto tag = tagService.getOne(id);
        tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel());
        return tag;
    }

    /**
     * Show list of tags
     *
     * @param page current page
     * @param size number records per page
     * @return list of tags
     */
    @RequestMapping(method = GET)
    @ResponseBody
    public CollectionModel<TagDto> getAllTag(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size) {
        Paginator paginator = new Paginator(size, page);
        List<TagDto> tags = tagService.getAll(paginator);
        tags.forEach(tag -> tag.add(linkTo(TagController.class).slash(tag.getId()).withSelfRel()));
        Link link = linkTo(TagController.class).withSelfRel();
        return CollectionModel.of(tags).add(link);
    }

    /**
     * Create tag
     *
     * @param tag tag to create
     * @return created tag
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    public TagDto createTag(@Valid @RequestBody TagDto tag) {
        return tagService.create(tag);
    }

    /**
     * Delete tag
     *
     * @param id deleted tag identifier
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    public void deleteTag(@PathVariable("id") Long id) {
        tagService.delete(id);
    }


}
