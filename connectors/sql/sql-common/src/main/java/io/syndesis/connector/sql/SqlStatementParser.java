package io.syndesis.connector.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlStatementParser {

    /*
     * C - INSERT INTO NAME VALUES (:id, :firstname, :lastname)
     * R - SELECT FIRSTNAME, LASTNAME FROM NAME WHERE ID=:id
     * U - UPDATE NAME SET FIRSTNAME=:firstname WHERE ID=:id
     * D - DELETE FROM NAME WHERE ID=:id
     * 
     * DEMO_ADD(INTEGER ${body[A]}
     * 
     * validate no "AS"
     * input params
     * output params
     * table name
     */
    private Connection connection;
    private String schema;
    private SqlStatementMetaData statementInfo;
    private List<String> sqlArray = new ArrayList<>();

    public SqlStatementParser(Connection connection, String schema, String sql) {
        super();
        statementInfo = new SqlStatementMetaData(sql.toUpperCase().trim());
        this.connection = connection;
        this.schema = schema;
    }

    public SqlStatementMetaData parse() throws SQLException {

        DatabaseMetaData meta = connection.getMetaData();
        statementInfo.setTablesInSchema(DatabaseMetaDataHelper.fetchTables(meta, null, schema, null));
        sqlArray = splitSqlStatement(statementInfo.getSqlStatement());
        
        switch (sqlArray.get(0)) {
            case "INSERT":
                statementInfo.setStatementType(StatementType.INSERT);
                String tableNameInsert = statementInfo.addTable(sqlArray.get(2));
                if (statementInfo.hasInputParams()) {
                    List<SqlParam> inputParams = findInsertParams(tableNameInsert);
                    if (inputParams.get(0).getColumn() != null) {
                        statementInfo.setInParams(
                                DatabaseMetaDataHelper.getJDBCInfoByColumnNames(
                                        meta, null, schema, tableNameInsert, inputParams));
                    } else {
                        statementInfo.setInParams(
                                DatabaseMetaDataHelper.getJDBCInfoByColumnOrder(
                                        meta, null, schema, tableNameInsert, inputParams));
                    }
                }
                break;
            case "UPDATE":
                statementInfo.setStatementType(StatementType.UPDATE);
                String tableNameUpdate = statementInfo.addTable(sqlArray.get(1));
                if (statementInfo.hasInputParams()) {
                    List<SqlParam> inputParams = findInputParams();
                    statementInfo.setInParams(
                            DatabaseMetaDataHelper.getJDBCInfoByColumnNames(
                                    meta, null, schema, tableNameUpdate, inputParams));
                }
                break;
            case "DELETE":
                statementInfo.setStatementType(StatementType.DELETE);
                String tableNameDelete = statementInfo.addTable(sqlArray.get(2));
                if (statementInfo.hasInputParams()) {
                    List<SqlParam> inputParams = findInputParams();
                    statementInfo.setInParams(
                            DatabaseMetaDataHelper.getJDBCInfoByColumnNames(
                                    meta, null, schema, tableNameDelete, inputParams));
                }
                break;
            case "SELECT":
                statementInfo.setStatementType(StatementType.SELECT);
                if (statementInfo.hasInputParams()) {
                    List<SqlParam> inputParams = findInputParams();
                    statementInfo.setTableNames(findTablesInSelectStatement()); //TODO support multiple tables
                    statementInfo.setInParams(
                            DatabaseMetaDataHelper.getJDBCInfoByColumnNames(
                                    meta, null, schema, statementInfo.getTableNames().get(0), inputParams));
                }
                statementInfo.setOutParams(DatabaseMetaDataHelper.getOutputColumnInfo(connection, statementInfo.getDefaultedSqlStatement()));
                break;
            default:
                //not implemented command
        
        }
        return statementInfo;
    }
    
    String createCamelStatement(String sql, List<SqlParam> params) {
        String camelStatement = sql;
        for (SqlParam param : params) {
            camelStatement = camelStatement.replace(":" + param.getName(), ":#" + param.getName());
        }
        return camelStatement;
    }

    /* default */ List<String> splitSqlStatement(String sql) {
        List<String> sqlArray = new ArrayList<>();
        String[] segments = sql.split("=|\\,|\\s|\\(|\\)");
        for (String segment : segments) {
            if (!"".equals(segment)) {
                sqlArray.add(segment);
            }
        }
        return sqlArray;
    }

    /* default */ List<SqlParam> findInsertParams(String tableName) {
        boolean isColumnName = false;
        List<String> columnNames = new ArrayList<>();
        for (String word: sqlArray) {
            if ("VALUES".equals(word)) {
                isColumnName = false;
            }
            if (isColumnName) {
                columnNames.add(word);
            }
            if (tableName.equals(word)) {
                isColumnName = true; //in the next iteration
            }
        }
        List<SqlParam> params = findInputParams();
        if (columnNames.size() == params.size()) {
            for (int i=0; i<params.size(); i++) {
                params.get(i).setColumn(columnNames.get(i));
            }
        }
        return params;
    }
    
    /* default */ List<SqlParam> findInputParams() {
        List<SqlParam> params = new ArrayList<>();
        int i=0;
        int columnPos=0;
        for (String word: sqlArray) {
            if (word.startsWith(":")) {
                SqlParam param = new SqlParam(word.substring(1));
                String column = sqlArray.get(i-1);
                if (column.startsWith(":") || "VALUES".equals(column)) {
                    param.setColumnPos(columnPos++);
                } else {
                    param.setColumn(column);
                }
                params.add(param);
            }
            i++;
        }
        return params;
    }
    
    /* default */ List<SqlParam> findOutputColumnsInSelectStatement() {
        boolean isParam = true;
        List<SqlParam> params = new ArrayList<>();
        for (String word: sqlArray) {
            if (isParam && !"SELECT".equals(word) && !"DISTINCT".equals(word)) {
                SqlParam param = new SqlParam(word);
                param.setColumn(word);
            }
            if ("FROM".equals(word)) {
                isParam = false;
                break;
            }
        }
        return params;
    }

    /*
     * Only for Select
     * 1. table needs to be in the table list
     * 2. if more then one, the parameters need to have a period
     * 3. if parameters contain period, figure out what's in front and find that table
     */
    /* default */ List<String> findTablesInSelectStatement() {
        boolean isTable = false;
        List<String> tables = new ArrayList<>();
        for (String word: sqlArray) {
            if (! statementInfo.getTablesInSchema().contains(word)) {
                isTable = false;
            }
            if (isTable) {
                tables.add(word);
            }
            if ("FROM".equals(word)) {
                isTable = true; //in the next iteration
            }
            
        }
        return tables;
    }


}
