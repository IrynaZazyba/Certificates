package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.mapper.MapperConstant.*;

@Component
public class CertificateRowMapper implements RowMapper<Certificate> {

    @Override
    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        return Certificate.builder()
                .id(resultSet.getLong(CERTIFICATE_ID))
                .name(resultSet.getString(CERTIFICATE_NAME))
                .description(resultSet.getString(CERTIFICATE_DESCRIPTION))
                .duration(resultSet.getInt(CERTIFICATE_DURATION))
                .createDate(resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime())
                .lastUpdateDate(resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime())
                .build();
    }
}
