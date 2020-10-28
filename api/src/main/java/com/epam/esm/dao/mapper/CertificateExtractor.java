package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.epam.esm.dao.mapper.MapperConstant.*;

@Component
public class CertificateExtractor implements ResultSetExtractor<Map<Certificate, List<Tag>>> {

    @Override
    public Map<Certificate, List<Tag>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Certificate, List<Tag>> data = new LinkedHashMap<>();
        while (resultSet.next()) {
            Certificate certificate = Certificate.builder()
                    .id(resultSet.getLong(CERTIFICATE_ID))
                    .name(resultSet.getString(CERTIFICATE_NAME))
                    .description(resultSet.getString(CERTIFICATE_DESCRIPTION))
                    .duration(resultSet.getInt(CERTIFICATE_DURATION))
                    .createDate(resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime())
                    .lastUpdateDate(resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime())
                    .build();
            data.putIfAbsent(certificate, new ArrayList<>());
            long tagId = resultSet.getLong(TAG_ID);
            if (tagId != 0) {
                Tag tag = Tag.builder()
                        .id(tagId)
                        .name(resultSet.getString(TAG_NAME))
                        .build();
                data.get(certificate).add(tag);
            }
        }
        return data;
    }
}
