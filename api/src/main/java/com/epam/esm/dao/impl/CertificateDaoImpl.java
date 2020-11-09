package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.mapper.CertificateExtractor;
import com.epam.esm.dao.mapper.CertificateRowMapper;
import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Filter;
import com.epam.esm.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CertificateDaoImpl implements CertificateDao {

    private static final String GET_BY_ID = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration FROM " +
            "certificate WHERE certificate.id=?";
    private static final String GET_All = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration FROM " +
            "certificate";
    private static final String INSERT_CERTIFICATE = "INSERT INTO `certificate` (`name`, `description`, `createDate`, " +
            "`lastUpdateDate`, `duration`) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_CERTIFICATE = "DELETE FROM `certificate` WHERE id=?";
    private static final String DELETE_CERTIFICATE_LINK = "DELETE FROM `certificate_has_tag` WHERE certificate_id=?";
    private static final String INSERT_CERTIFICATE_TAG_LINK = "INSERT INTO `certificate_has_tag`" +
            "(`certificate_id`, `tag_id`) VALUES (?,?)";
    private static final String GET_BY_FILTER = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration, tag.id t_id, " +
            "tag.name t_name FROM certificate " +
            "left join certificate_has_tag on certificate_has_tag.certificate_id=certificate.id " +
            "left join tag on certificate_has_tag.tag_id=tag.id  WHERE 1";
    private static final String UPDATE_CERTIFICATE = "UPDATE `certificate` SET";

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;
    private final CertificateExtractor certificateExtractor;

    @Override
    public Optional<Certificate> getOne(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id}, certificateRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Certificate> getAll() {
        return jdbcTemplate.query(GET_All, certificateRowMapper);
    }

    @Override
    public Certificate insert(Certificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{"id"});
                    ps.setString(1, certificate.getName());
                    ps.setString(2, certificate.getDescription());
                    ps.setTimestamp(3, Timestamp.from(certificate.getCreateDate()));
                    ps.setTimestamp(4, Timestamp.from(certificate.getLastUpdateDate()));
                    ps.setInt(5, certificate.getDuration());
                    return ps;
                },
                keyHolder);
        long id = keyHolder.getKey().longValue();
        certificate.setId(id);
        return certificate;
    }

    @Override
    public void insertTagLink(Long certificateId, List<Long> tagsId) {
        tagsId.forEach(e -> this.jdbcTemplate.update(INSERT_CERTIFICATE_TAG_LINK, certificateId, e));
    }

    @Override
    public void deleteTagLink(Long id) {
        this.jdbcTemplate.update(DELETE_CERTIFICATE_LINK, id);
    }

    @Override
    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    @Override
    public Map<Certificate, List<Tag>> filterCertificate(Filter filter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GET_BY_FILTER);
        List<Object> data = new ArrayList<>();

        if (!Objects.isNull(filter.getTagName())) {
            stringBuilder.append(" AND tag.name = ?");
            data.add(filter.getTagName());
        }

        if (!Objects.isNull(filter.getName())) {
            stringBuilder.append(" AND certificate.name LIKE ?");
            data.add("%" + filter.getName() + "%");
        }

        if (!Objects.isNull(filter.getDescription())) {
            stringBuilder.append(" AND certificate.description LIKE ?");
            data.add("%" + filter.getDescription() + "%");
        }

        if (!Objects.isNull(filter.getSort()) && filter.getSort().equals("name")) {
            stringBuilder.append(" ORDER BY certificate.name");
        }

        if (!Objects.isNull(filter.getSort()) && filter.getSort().equals("createDate"))
            stringBuilder.append(" ORDER BY certificate.createDate");

        if (!Objects.isNull(filter.getSort()) && !Objects.isNull(filter.getOrder()) && filter.getOrder().equals("ASC"))
            stringBuilder.append(" ASC");

        if (!Objects.isNull(filter.getSort()) && !Objects.isNull(filter.getOrder()) && filter.getOrder().equals("DESC"))
            stringBuilder.append(" DESC");

        String query = String.valueOf(stringBuilder);

        Map<Certificate, List<Tag>> result = new HashMap<>();
        try {
            return jdbcTemplate.query(query, data.toArray(), certificateExtractor);
        } catch (Exception e) {
            return result;
        }
    }

    @Override
    public void update(Certificate certificate) {
        List<Object> data = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UPDATE_CERTIFICATE);
        stringBuilder.append(" lastUpdateDate=?,");
        data.add(certificate.getLastUpdateDate());

        if (!Objects.isNull(certificate.getName())) {
            stringBuilder.append(" name=?,");
            data.add(certificate.getName());
        }

        if (!Objects.isNull(certificate.getDescription())) {
            stringBuilder.append(" description=?,");
            data.add(certificate.getDescription());
        }

        if (certificate.getDuration() != 0) {
            stringBuilder.append(" duration=?,");
            data.add(certificate.getDuration());
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" WHERE id=?");
        data.add(certificate.getId());
        this.jdbcTemplate.update(String.valueOf(stringBuilder), data.toArray());
    }

}
