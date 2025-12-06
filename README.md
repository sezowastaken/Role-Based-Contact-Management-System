-USERS-

TESTER:
username: tt         - password: tt   
username: bellingham - password: ayhan
username: tester     - password: test

JUNIOR_DEV:
unsername: jd        - password: jd 
username: terminatör - password: bilal
username: junior     - password: junior

SENIOR_DEV:
username: sd     - password: sd
username: dede   - password: dede
username: senior - password: senior

MANAGER:
username: man     - password: man
username: sezo    - password: sezo
username: manager - password: manager



-DATABASE USER-
CREATE USER 'myuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'myuser'@'localhost' WITH GRANT OPTION;


-RUN MAVEN-
compile:
mvn compile

run:
mvn exec:java


