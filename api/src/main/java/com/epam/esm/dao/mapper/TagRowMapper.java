package com.epam.esm.dao.mapper;

import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.epam.esm.dao.mapper.MapperConstant.TAG_ID;
import static com.epam.esm.dao.mapper.MapperConstant.TAG_NAME;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int i) throws SQLException {
        return Tag.builder().id(rs.getLong(TAG_ID)).name(rs.getString(TAG_NAME)).build();
    }
}
