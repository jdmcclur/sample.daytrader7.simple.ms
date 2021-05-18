## (C) Copyright IBM Corporation 2015.
##
## Licensed under the Apache License, Version 2.0 (the "License");
## you may not use this file except in compliance with the License.
## You may obtain a copy of the License at
##
## http://www.apache.org/licenses/LICENSE-2.0
##
## Unless required by applicable law or agreed to in writing, software
## distributed under the License is distributed on an "AS IS" BASIS,
## WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
## See the License for the specific language governing permissions and
## limitations under the License.

# Each SQL statement in this file should terminate with a semicolon (;)
# Lines starting with the pound character (#) are considered as comments
DROP TABLE KEYGENEJB;
DROP TABLE ORDEREJB;

CREATE TABLE KEYGENEJB
  (KEYVAL INTEGER NOT NULL,
   KEYNAME VARCHAR(250) NOT NULL);

ALTER TABLE KEYGENEJB
  ADD CONSTRAINT PK_KEYGENEJB PRIMARY KEY (KEYNAME);
  
INSERT INTO KEYGENEJB (KEYNAME,KEYVAL) VALUES ('order', 0);

CREATE TABLE ORDEREJB
  (ORDERFEE DECIMAL(14, 2),
   COMPLETIONDATE TIMESTAMP,
   ORDERTYPE VARCHAR(250),
   ORDERSTATUS VARCHAR(250),
   PRICE DECIMAL(14, 2),
   QUANTITY DOUBLE NOT NULL,
   OPENDATE TIMESTAMP,
   ORDERID INTEGER NOT NULL,
   ACCOUNTID VARCHAR(250),
   QUOTE_SYMBOL VARCHAR(250),
   HOLDINGID INTEGER);

ALTER TABLE ORDEREJB
  ADD CONSTRAINT PK_ORDEREJB PRIMARY KEY (ORDERID);


