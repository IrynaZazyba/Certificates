package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

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
    public Optional<Tag> getOne(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id}, tagRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> getByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_NAME, new Object[]{name}, tagRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, tagRowMapper);
    }

    @Override
    public Tag insert(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        long generatedId = keyHolder.getKey().longValue();
        tag.setId(generatedId);
        return tag;
    }

    @Override
    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public void deleteTagLink(Long id) {
        this.jdbcTemplate.update(DELETE_TAG_LINK, id);
    }

}
