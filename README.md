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