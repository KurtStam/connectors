package io.syndesis.connector.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {

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
    String sql;
    List<String> sqlArray = new ArrayList<>();
    
    
    public SqlParser(String sql) {
        super();
        this.sql = sql.toUpperCase().trim();
    }

    public Info parse() {

        split();
        String command = sqlArray.get(0);
        Info info = new Info();
        
        switch (command) {
        
            case "INSERT": 
                info.tableNames.add(sqlArray.get(2));
                info.inParams.addAll(findInsertParams(info.tableNames.get(0)));
                break;
            case "SELECT":
                info.tableNames.addAll(findTables());
                info.inParams.addAll(findInputParams());
                info.outParams.addAll(findOutputParams());
                break;
            case "UPDATE":
                info.tableNames.add(sqlArray.get(1));
                info.inParams.addAll(findInputParams());
                break;
            case "DELETE":
                info.tableNames.add(sqlArray.get(2));
                info.inParams.addAll(findInputParams());
                break;
            default:
                //not implemented command
                
                
        
        }
        return info;
    }
    
    /* default */ SqlParser split() {
        String[] segments = sql.split("=|\\,|\\s|\\(|\\)");
        for (String segment : segments) {
            if (!"".equals(segment)) {
                sqlArray.add(segment);
            }
        }
        return this;
    }

    /* default */ List<Param> findInsertParams(String tableName) {
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
        List<Param> params = findInputParams();
        if (columnNames.size() == params.size()) {
            for (int i=0; i<params.size(); i++) {
                params.get(i).setColumn(columnNames.get(i));
            }
        }
        return params;
    }
    
    /* default */ List<Param> findInputParams() {
        List<Param> params = new ArrayList<>();
        int i=0;
        int columnPos=0;
        for (String word: sqlArray) {
            if (word.startsWith(":")) {
                Param param = new Param(word.substring(1));
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
    
    /* default */ List<Param> findOutputParams() {
        boolean isParam = true;
        List<Param> params = new ArrayList<>();
        for (String word: sqlArray) {
            if (isParam && !"SELECT".equals(word) && !"DISTINCT".equals(word)) {
                Param param = new Param(word);
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
     */
    /* default */ List<String> findTables() {
        boolean isTable = false;
        List<String> tables = new ArrayList<>();
        for (String word: sqlArray) {
            if ("WHERE".equals(word) || "SORT".equals(word)) {
                isTable = false;
                break;
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
    
    public class Param {
        private String name;
        private String column;
        private int columnPos;

        public Param(String name) {
            super();
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getColumn() {
            return column;
        }
        public void setColumn(String column) {
            this.column = column;
        }
        public int getColumnPos() {
            return columnPos;
        }
        public void setColumnPos(int columnPos) {
            this.columnPos = columnPos;
        }
    }
    
    public class Info {
        private List<Param> inParams = new ArrayList<>();
        private List<Param> outParams = new ArrayList<>();
        private List<String> tableNames = new ArrayList<>();
        
        public List<Param> getInParams() {
            return inParams;
        }
        public List<Param> getOutParams() {
            return outParams;
        }
        public List<String> getTableNames() {
            return tableNames;
        }
        
        
    }
}
