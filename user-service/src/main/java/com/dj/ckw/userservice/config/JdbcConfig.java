package com.dj.ckw.userservice.config;

import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.postgresql.util.PGobject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC configuration with custom converters for PostgreSQL JSON types.
 * Only loads when datasource is available.
 * In degraded mode (no database), this configuration is skipped.
 */
@Configuration
@ConditionalOnProperty(name = "spring.datasource.url")
public class JdbcConfig extends AbstractJdbcConfiguration {
    @Bean
    @Override
    @NullMarked
    public JdbcCustomConversions jdbcCustomConversions() {
        final List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(EntityWritingConverter.INSTANCE);
        converters.add(EntityReadingConverter.INSTANCE);
        return new JdbcCustomConversions(converters);
    }

    @WritingConverter
    enum EntityWritingConverter implements Converter<JsonNode, PGobject> {
        INSTANCE;

        @Override
        public @Nullable PGobject convert(JsonNode source) {
            ObjectMapper objectMapper = new ObjectMapper();

            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            try {
                jsonObject.setValue(objectMapper.writeValueAsString(source));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (JsonNodeException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }

    @ReadingConverter
    enum EntityReadingConverter implements Converter<PGobject, JsonNode> {
        INSTANCE;

        @Override
        public JsonNode convert(PGobject pgObject) {
            ObjectMapper objectMapper = new ObjectMapper();
            String source = pgObject.getValue();
            try {
                return objectMapper.readTree(source);
            } catch (JsonNodeException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
