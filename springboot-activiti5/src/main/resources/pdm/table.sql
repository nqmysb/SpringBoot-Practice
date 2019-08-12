-- CREATE TABLE
CREATE TABLE YHXXB
(
  YHZH VARCHAR2(40) NOT NULL,
  YHMC VARCHAR2(100),
  YHMM VARCHAR2(40),
  LXDH VARCHAR2(20),
  LXYX VARCHAR2(20),
  ZT   VARCHAR2(2)
);

-- Create sequence 
create sequence SEQ_YHXXB
minvalue 1
maxvalue 999999
start with 80
increment by 1
nocache;