debop4j-data
=============

debop4j-data는 Data Access Layer 를 구현한 라이브러리입니다.
기본적으로 jdbc, hibernate를 사용한 Data Access 를 구현한 것으로, hibernate를 기본으로 적용되는 Application에 사용하기를 추천합니다.

주요 기능

1. hibernate 용 기본 entity class를 재공합니다.
2. hibernate의 장점인 다양한 DBMS에 대한 테스트를 수행할 수 있도록 Spring Configuration을 제공합니다.
3. hibernate Session 보다 편한 Repository를 제공합니다.
4. Unit of Work 패턴을 적용한 UnitOfWorks 라는 클래스를 제공하여, 같은 Thread Context 내에서는 무결성을 유지할 수 있습니다.
