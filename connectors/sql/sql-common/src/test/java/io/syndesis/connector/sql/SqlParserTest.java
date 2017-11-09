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

import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.syndesis.connector.sql.SqlParser.Info;
import io.syndesis.connector.sql.SqlParser.Param;


public class SqlParserTest {

    @Test
    public void parseSelect() throws JsonProcessingException, JSONException {
        
        SqlParser parser = new SqlParser("SELECT FIRSTNAME, LASTNAME FROM NAME WHERE ID=:id");
        Info info = parser.parse();
        Assert.assertEquals("NAME", info.getTableNames().get(0));
        Assert.assertEquals(1, info.getInParams().size());
        Assert.assertEquals("ID", info.getInParams().get(0).getName());
        Assert.assertEquals("ID", info.getInParams().get(0).getColumn());
        
    }

    @Test
    public void parseUpdate() throws JsonProcessingException, JSONException {
        
        SqlParser parser = new SqlParser("UPDATE NAME SET FIRSTNAME=:first WHERE ID=:id");
        Info info = parser.parse();
        Assert.assertEquals("NAME", info.getTableNames().get(0));
        Assert.assertEquals(2, info.getInParams().size());
        Assert.assertEquals("FIRST", info.getInParams().get(0).getName());
        Assert.assertEquals("FIRSTNAME", info.getInParams().get(0).getColumn());
        Assert.assertEquals("ID", info.getInParams().get(1).getName());
        Assert.assertEquals("ID", info.getInParams().get(1).getColumn());
    }
    
    @Test
    public void parseDelete() throws JsonProcessingException, JSONException {
        
        SqlParser parser = new SqlParser("DELETE FROM NAME WHERE ID=:id");
        Info info = parser.parse();
        Assert.assertEquals("NAME", info.getTableNames().get(0));
        Assert.assertEquals(1, info.getInParams().size());
        Assert.assertEquals("ID", info.getInParams().get(0).getName());
        Assert.assertEquals("ID", info.getInParams().get(0).getColumn());
    }
    
    @Test
    public void parseInsertIntoAllColumnsOfTheTable() throws JsonProcessingException, JSONException {
        
        SqlParser parser = new SqlParser("INSERT INTO NAME VALUES (:id, :firstname, :lastname)");
        Info info = parser.parse();
        Assert.assertEquals("NAME", info.getTableNames().get(0));
        Assert.assertEquals(3, info.getInParams().size());
        Assert.assertEquals("ID", info.getInParams().get(0).getName());
        Assert.assertEquals(0, info.getInParams().get(0).getColumnPos());
        Assert.assertEquals("FIRSTNAME", info.getInParams().get(1).getName());
        Assert.assertEquals(1, info.getInParams().get(1).getColumnPos());
        Assert.assertEquals("LASTNAME", info.getInParams().get(2).getName());
        Assert.assertEquals(2, info.getInParams().get(2).getColumnPos());
    }

    @Test
    public void parseInsertWithSpecifiedColumnNames() throws JsonProcessingException, JSONException {
        
        SqlParser parser = new SqlParser("INSERT INTO NAME (FIRSTNAME, LASTNAME) VALUES (:firstname, :lastname)");
        Info info = parser.parse();
        Assert.assertEquals("NAME", info.getTableNames().get(0));
        Assert.assertEquals(2, info.getInParams().size());
        Assert.assertEquals("FIRSTNAME", info.getInParams().get(0).getName());
        Assert.assertEquals(0, info.getInParams().get(0).getColumnPos());
        Assert.assertEquals("FIRSTNAME", info.getInParams().get(0).getColumn());
        Assert.assertEquals("LASTNAME", info.getInParams().get(1).getName());
        Assert.assertEquals(1, info.getInParams().get(1).getColumnPos());
        Assert.assertEquals("LASTNAME", info.getInParams().get(1).getColumn());
    }


}
