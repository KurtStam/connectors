/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.connector.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.camel.component.extension.metadata.AbstractMetaDataExtension;
import org.apache.camel.component.extension.metadata.DefaultMetaData;

import io.syndesis.connector.sql.DatabaseProduct;

public class SqlMetaData {



    protected String getDefaultSchema(final String databaseProductName, final Map<String, Object> parameters) {

        String defaultSchema = null;
        // Oracle uses the username as schema
        if (databaseProductName.equalsIgnoreCase(DatabaseProduct.ORACLE.name())) {
            defaultSchema = parameters.get("user").toString();
        } else if (databaseProductName.equalsIgnoreCase(DatabaseProduct.POSTGRESQL.name())) {
            defaultSchema = "public";
        } else if (databaseProductName.equalsIgnoreCase(DatabaseProduct.APACHE_DERBY.nameWithSpaces())) {
            if (parameters.get("user") != null) {
                defaultSchema = parameters.get("user").toString().toUpperCase();
            } else {
                defaultSchema = "NULL";
            }
        }
        return defaultSchema;
    }

    /* default */ static ResultSet fetchProcedureColumns(final DatabaseMetaData meta, final String catalog,
        final String schema, final String procedureName) throws SQLException {
        if (meta.getDatabaseProductName().equalsIgnoreCase(DatabaseProduct.POSTGRESQL.name())) {
            return meta.getFunctionColumns(catalog, schema, procedureName, null);
        }

        return meta.getProcedureColumns(catalog, schema, procedureName, null);
    }

    /* default */ static ResultSet fetchProcedures(final DatabaseMetaData meta, final String catalog,
        final String schemaPattern, final String procedurePattern) throws SQLException {
        if (meta.getDatabaseProductName().equalsIgnoreCase(DatabaseProduct.POSTGRESQL.name())) {
            return meta.getFunctions(catalog, schemaPattern, procedurePattern);
        }

        return meta.getProcedures(catalog, schemaPattern, procedurePattern);
    }
    
    /* default */ static Set<String> fetchTables(final DatabaseMetaData meta, final String catalog,
        final String schemaPattern, final String tableNamePattern) throws SQLException {
        Set<String> tablesInSchema = new HashSet<>();
        ResultSet rs = meta.getTables(catalog, schemaPattern, tableNamePattern, new String[] { "TABLE" });
        while (rs.next()) {
            tablesInSchema.add(rs.getString(3));
        }
        return tablesInSchema;
    }

    /* default */ static ResultSet fetchTableColumns(final DatabaseMetaData meta, final String catalog,
            final String schema, final String tableName, final String columnName) throws SQLException {

        return meta.getColumns(catalog, schema, tableName, columnName);
    }}
