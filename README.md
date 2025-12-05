-USERS-

TESTER:
username: bellingham - password: ayhan
username: tester - password: test

JUNIOR_DEV:
username: terminatör - password: bilal | calısmıyor
username: junior - password: junior

SENIOR_DEV:
username: dede - password: dede
username: senior - password: senior

TESTER:
username: sezo - password: sezo
username: manager - password: manager

-DATABASE USER-
CREATE USER 'myuser'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'myuser'@'localhost' WITH GRANT OPTION;

-RUN MAVEN-
compile:
mvn compile

run:
mvn exec:java


-RUN-
compile:
javac -cp "libs\mysql-connector-j-9.5.0.jar" -d out src\app\Main.java src\dao\*.java src\model\*.java src\service\*.java src\ui\menu\*.java src\ui\screen\*.java src\undo\*.java src\util\*.java

run:
java -cp "out;libs\mysql-connector-j-9.5.0.jar" app.Main

```
Role-Based-Contact-Management-System
├─ db
│  └─ oop_rbcm_db.sql
├─ libs
│  └─ mysql-connector-j-9.5.0.jar
├─ src
│  ├─ app
│  │  └─ Main.java
│  ├─ dao
│  │  ├─ ContactDAO.java
│  │  ├─ DatabaseConnection.java
│  │  └─ UserDAO.java
│  ├─ model
│  │  ├─ Contact.java
│  │  ├─ JuniorDeveloper.java
│  │  ├─ Manager.java
│  │  ├─ Role.java
│  │  ├─ SeniorDeveloper.java
│  │  ├─ Tester.java
│  │  └─ User.java
│  ├─ service
│  │  ├─ AuthService.java
│  │  ├─ ContactService.java
│  │  ├─ StatisticsService.java
│  │  └─ UserService.java
│  ├─ ui
│  │  ├─ menu
│  │  │  ├─ BaseMenu.java
│  │  │  ├─ JuniorDevMenu.java
│  │  │  ├─ ManagerMenu.java
│  │  │  ├─ SeniorDevMenu.java
│  │  │  └─ TesterMenu.java
│  │  └─ screen
│  │     ├─ AsciiAnimator.java
│  │     └─ LoginScreen.java
│  ├─ undo
│  │  ├─ UndoAction.java
│  │  └─ UndoManager.java
│  └─ util
│     ├─ ConsoleColors.java
│     ├─ DateUtil.java
│     ├─ HashUtil.java
│     └─ InputHelper.java
└─ video

```
```
Role-Based-Contact-Management-System
├─ db
│  └─ oop_rbcm_db.sql
├─ libs
│  └─ mysql-connector-j-9.5.0.jar
├─ out
│  ├─ app
│  ├─ dao
│  ├─ model
│  ├─ service
│  ├─ ui
│  │  ├─ menu
│  │  └─ screen
│  ├─ undo
│  └─ util
├─ pom.xml
├─ README.md
├─ src
│  └─ main
│     └─ java
│        ├─ app
│        │  └─ Main.java
│        ├─ dao
│        │  ├─ ContactDAO.java
│        │  ├─ DatabaseConnection.java
│        │  └─ UserDAO.java
│        ├─ model
│        │  ├─ Contact.java
│        │  ├─ JuniorDeveloper.java
│        │  ├─ Manager.java
│        │  ├─ Role.java
│        │  ├─ SeniorDeveloper.java
│        │  ├─ Tester.java
│        │  └─ User.java
│        ├─ service
│        │  ├─ AuthService.java
│        │  ├─ ContactService.java
│        │  ├─ StatisticsService.java
│        │  └─ UserService.java
│        ├─ ui
│        │  ├─ menu
│        │  │  ├─ BaseMenu.java
│        │  │  ├─ JuniorDevMenu.java
│        │  │  ├─ ManagerMenu.java
│        │  │  ├─ SeniorDevMenu.java
│        │  │  └─ TesterMenu.java
│        │  └─ screen
│        │     ├─ AsciiAnimator.java
│        │     └─ LoginScreen.java
│        ├─ undo
│        │  ├─ UndoAction.java
│        │  └─ UndoManager.java
│        └─ util
│           ├─ ConsoleColors.java
│           ├─ DateUtil.java
│           ├─ HashUtil.java
│           └─ InputHelper.java
├─ target
│  ├─ classes
│  │  ├─ app
│  │  │  └─ Main.class
│  │  ├─ dao
│  │  │  ├─ ContactDAO.class
│  │  │  ├─ DatabaseConnection.class
│  │  │  └─ UserDAO.class
│  │  ├─ model
│  │  │  ├─ Contact.class
│  │  │  ├─ JuniorDeveloper.class
│  │  │  ├─ Manager.class
│  │  │  ├─ Role.class
│  │  │  ├─ SeniorDeveloper.class
│  │  │  ├─ Tester.class
│  │  │  └─ User.class
│  │  ├─ service
│  │  │  ├─ AuthService.class
│  │  │  ├─ ContactService.class
│  │  │  ├─ StatisticsService.class
│  │  │  └─ UserService.class
│  │  ├─ ui
│  │  │  ├─ menu
│  │  │  │  ├─ BaseMenu.class
│  │  │  │  ├─ JuniorDevMenu.class
│  │  │  │  ├─ ManagerMenu.class
│  │  │  │  ├─ SeniorDevMenu.class
│  │  │  │  └─ TesterMenu.class
│  │  │  └─ screen
│  │  │     ├─ AsciiAnimator.class
│  │  │     ├─ LoginScreen$1.class
│  │  │     └─ LoginScreen.class
│  │  ├─ undo
│  │  │  ├─ UndoAction.class
│  │  │  └─ UndoManager.class
│  │  └─ util
│  │     ├─ ConsoleColors.class
│  │     ├─ DateUtil.class
│  │     ├─ HashUtil.class
│  │     └─ InputHelper.class
│  ├─ generated-sources
│  │  └─ annotations
│  ├─ javadoc-bundle-options
│  │  └─ javadoc-options-javadoc-resources.xml
│  ├─ maven-javadoc-plugin-stale-data.txt
│  ├─ maven-status
│  │  └─ maven-compiler-plugin
│  │     └─ compile
│  │        └─ default-compile
│  │           ├─ createdFiles.lst
│  │           └─ inputFiles.lst
│  └─ reports
│     └─ apidocs
│        ├─ allclasses-index.html
│        ├─ allpackages-index.html
│        ├─ app
│        │  ├─ class-use
│        │  │  └─ Main.html
│        │  ├─ Main.html
│        │  ├─ package-summary.html
│        │  ├─ package-tree.html
│        │  └─ package-use.html
│        ├─ dao
│        │  ├─ class-use
│        │  │  ├─ ContactDAO.html
│        │  │  ├─ DatabaseConnection.html
│        │  │  └─ UserDAO.html
│        │  ├─ ContactDAO.html
│        │  ├─ DatabaseConnection.html
│        │  ├─ package-summary.html
│        │  ├─ package-tree.html
│        │  ├─ package-use.html
│        │  └─ UserDAO.html
│        ├─ element-list
│        ├─ help-doc.html
│        ├─ index-all.html
│        ├─ index.html
│        ├─ legal
│        │  ├─ COPYRIGHT
│        │  ├─ dejavufonts.md
│        │  ├─ jquery.md
│        │  ├─ jqueryUI.md
│        │  └─ LICENSE
│        ├─ member-search-index.js
│        ├─ model
│        │  ├─ class-use
│        │  │  ├─ Contact.html
│        │  │  ├─ JuniorDeveloper.html
│        │  │  ├─ Manager.html
│        │  │  ├─ Role.html
│        │  │  ├─ SeniorDeveloper.html
│        │  │  ├─ Tester.html
│        │  │  └─ User.html
│        │  ├─ Contact.html
│        │  ├─ JuniorDeveloper.html
│        │  ├─ Manager.html
│        │  ├─ package-summary.html
│        │  ├─ package-tree.html
│        │  ├─ package-use.html
│        │  ├─ Role.html
│        │  ├─ SeniorDeveloper.html
│        │  ├─ Tester.html
│        │  └─ User.html
│        ├─ module-search-index.js
│        ├─ overview-summary.html
│        ├─ overview-tree.html
│        ├─ package-search-index.js
│        ├─ resource-files
│        │  ├─ copy.svg
│        │  ├─ glass.png
│        │  ├─ jquery-ui.min.css
│        │  ├─ link.svg
│        │  ├─ stylesheet.css
│        │  └─ x.png
│        ├─ script-files
│        │  ├─ jquery-3.7.1.min.js
│        │  ├─ jquery-ui.min.js
│        │  ├─ script.js
│        │  ├─ search-page.js
│        │  └─ search.js
│        ├─ search.html
│        ├─ service
│        │  ├─ AuthService.html
│        │  ├─ class-use
│        │  │  ├─ AuthService.html
│        │  │  ├─ ContactService.html
│        │  │  ├─ StatisticsService.html
│        │  │  └─ UserService.html
│        │  ├─ ContactService.html
│        │  ├─ package-summary.html
│        │  ├─ package-tree.html
│        │  ├─ package-use.html
│        │  ├─ StatisticsService.html
│        │  └─ UserService.html
│        ├─ tag-search-index.js
│        ├─ type-search-index.js
│        ├─ ui
│        │  ├─ menu
│        │  │  ├─ BaseMenu.html
│        │  │  ├─ class-use
│        │  │  │  ├─ BaseMenu.html
│        │  │  │  ├─ JuniorDevMenu.html
│        │  │  │  ├─ ManagerMenu.html
│        │  │  │  ├─ SeniorDevMenu.html
│        │  │  │  └─ TesterMenu.html
│        │  │  ├─ JuniorDevMenu.html
│        │  │  ├─ ManagerMenu.html
│        │  │  ├─ package-summary.html
│        │  │  ├─ package-tree.html
│        │  │  ├─ package-use.html
│        │  │  ├─ SeniorDevMenu.html
│        │  │  └─ TesterMenu.html
│        │  └─ screen
│        │     ├─ AsciiAnimator.html
│        │     ├─ class-use
│        │     │  ├─ AsciiAnimator.html
│        │     │  └─ LoginScreen.html
│        │     ├─ LoginScreen.html
│        │     ├─ package-summary.html
│        │     ├─ package-tree.html
│        │     └─ package-use.html
│        ├─ undo
│        │  ├─ class-use
│        │  │  ├─ UndoAction.html
│        │  │  └─ UndoManager.html
│        │  ├─ package-summary.html
│        │  ├─ package-tree.html
│        │  ├─ package-use.html
│        │  ├─ UndoAction.html
│        │  └─ UndoManager.html
│        └─ util
│           ├─ class-use
│           │  ├─ ConsoleColors.html
│           │  ├─ DateUtil.html
│           │  ├─ HashUtil.html
│           │  └─ InputHelper.html
│           ├─ ConsoleColors.html
│           ├─ DateUtil.html
│           ├─ HashUtil.html
│           ├─ InputHelper.html
│           ├─ package-summary.html
│           ├─ package-tree.html
│           └─ package-use.html
└─ video

```