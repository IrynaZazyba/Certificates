package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.domain.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private static final String GET_BY_ID = "SELECT tag.id t_id, tag.name t_name FROM `tag` WHERE tag.id=?";
    private static final String GET_ALL = "SELECT tag.id t_id, tag.name t_name FROM `tag`";
    private static final String INSERT_TAG = "INSERT INTO `tag` (`name`) VALUES (?)";
    private static final String DELETE_TAG_LINK = "DELETE FROM `certificate_has_tag` WHERE tag_id=?";
    private static final String DELETE_TAG = "DELETE FROM `tag` WHERE id=?";
    private static final String GET_BY_NAME = "SELECT tag.id t_id, tag.name t_name FROM `tag` WHERE tag.name=?";

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Override
    public Tag getOne(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id}, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found", e, id);
        }
    }

    @Override
    public Tag getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_NAME, new Object[]{name}, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Resource not found", e);
        }
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(GET_ALL, tagRowMapper);
    }

    @Override
    public Long insertTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void deleteTag(Long id) {
        this.jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public void deleteTagLink(Long id) {
        this.jdbcTemplate.update(DELETE_TAG_LINK, id);
    }

}
