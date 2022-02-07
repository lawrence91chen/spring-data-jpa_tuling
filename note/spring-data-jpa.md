# spring-data-jpa



## Idea

- .var: assign variable
- .new: new instance
- sout: system out
- 右方 Maven Tab 可以看各個專案的依賴關係
- ctrl + alt + L: auto format
- alt + / : Code completion
- ctrl + shift + space : code suggestion
  - some completion may not be provided on Community Edition
- ctrl + /: comment



## Lec 04、JPA 的介紹及 JDBC 的關係

- JDBC 和 JPA 都是一種規範，JDBC 由各個關聯式資料庫(MySQL、Oracle、...) 來實現，也就是開發時所引入的 driver jar 檔

- JPA 是由 ORM 框架 (全自動) 實現

  > MyBatis 是半自動 ORM 框架，甚至嚴格來說都不能算 ORM 框架
  >
  > MyBatis 其實只是對 JDBC 做簡單的封裝而已

- 全自動 ORM 框架最主流的是 Hibernate

- JDBC 用 SQL 語句操作 SQL

- JPA 用 OOP 的方式操作 DB，通過 ORM 框架生成 SQL

  e.g xxx.find -> SELECT，xxx.remove -> DELETE

  前提是要建立好 ORM Mapping

  > Java Class (attribute) <-> RDBMS TABLE (column/field)

- 開發人員可以不用會 SQL，由 JPA 充當翻譯器
- JPA 資料庫移植性較高，例如 MySQL 和 Oracle 分頁語法不同，要轉換很麻煩



## Lec 05、JPA、Hibernate、MyBatis 關係

- JPA (Java/Jarkata Persistence API)，是 Sun 官方在 JDK 1.5 提出的**一種 ORM 規範**，O: Object、R: Relational、M: Mapping

  > Jarkata 簡單說是 Oracle 法律上不讓 Eclipse 基金會使用 Java 所衍生出的別名
  >
  > https://www.ithome.com.tw/news/133034

  - 簡化持久化操作的開發，讓開發者從 SQL 代碼中解脫，面向對象操作
  - Sun 希望持久化技術統一，實現天下歸一，基於 JPA 進行持久化操作可以任意切換資料庫

- JPA 支持 XML 與 註解兩種元數據映射、提供基礎 API 與 JPQL

- JPA 僅是一種規範，也就是只定義了接口(interface)，而接口需要實現才能 work

- 而 JPA 的實現就是俗稱的 ORM 框架，如 Hibernate(最主流)、...

  - Spring boot 默認的 JPA 實現也是 Hibernate

- Hibernate V.S. MyBatis 

  - MyBatis: 小巧、方便、高效、簡單、直接、半自動

    - 半自動的 ORM 框架: 因為需要自己寫 SQL，與 JPA 對 ORM 框架的定義(用面向對象的方式進行操作)有差異，嚴格來說都不能算 ORM 框架
    - 小巧: MyBatis 就是 JDBC 封裝，再加上 緩存、動態 SQL 等等功能
    - 方便: 因為還是得寫 SQL，方便性來說要打個問號，但若是用 MyBatis-plus，有提供一些基本的 CRUD，就比較方便
    - 在中國較流行，因為系統邏輯較偏複雜，例如有的審核系統一個 SQL 幾百行...
    - 使用場景: 在業務比較複雜的系統使用
    - 學習成本較低

  - Hibernate: 強大、方便、高效、複雜、繞彎子、全自動

    - 全自動的 ORM 框架
    - 強大: 根據 ORM 映射生成不同 SQL
    - 複雜: 當業務邏輯複雜時套用不易
    - 在歐美較流行，狠甩 MyBatis 幾條街
    - 使用場景: 在業務相對簡單的系統使用

    > Q: 既然 Hibernate 業務複雜時難用，學習成本又高，那為什麼還要用?
    >
    > A: 隨著微服務的流行，Spring Data 也逐漸嶄露頭角被廣為使用。(微服務的精神是"拆"，把系統功能拆的越細越好，數據庫也會跟隨著進行拆分，例如商品表單獨拆放一個 DB，訂單表單獨拆放一個 DB，用戶相關再另外放一個 DB，此時對單一商品數據庫一般就不太會寫到複雜 SQL 了，比較會是分開查商品 DB 和訂單 DB 再返回資料給前端使用者，SQL 語句也就簡化了)

## Lec 06、Hibernate & JPA 快速搭建

- Spring Data JPA 也是依賴 Hibernate，所以就沒有辦法拒絕學習 Hibernate 囉

  要用 Spring Data JPA 就必須學 Hibernate

- 父 Maven 項目主要是用來管理子項目和公共依賴，不會有程式代碼(src 目錄)

- Hibernate 編程思想中是不需要手動創建 Table 的，它叫做 Code First 代碼先行，與傳統先設計表的 DB First 不同。只需要關注 POJO 類，Hibernate 會透過 POJO 類生成表 (但數據庫要自行手動創建)

> ### Idea
>
> - File/New/Project -> Maven -> Next -> 設定 Artifact coordinate -> Finish
>
> - 刪除 src 目錄
> - 專案右鍵 -> New/Module/Maven -> Next -> 設定 Artifact coordinate -> Finish

- javax.persistence-api-2.2.jar 是 JPA 依賴，內部定義的基本上都是接口，所以只依賴這個的話就沒有實現類，不能進行持久化操作。所以說 JPA 只定義規範這件事從這個 jar 就看的出來。

## Lec 07、基於 Hibernate 數據庫持久化操作

testC 執行 log

```
二月 07, 2022 12:04:22 上午 org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@7f4037ed] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: 
    
    create table cst_customer (
       cust_id bigint not null auto_increment,
        cust_address varchar(255),
        cust_name varchar(255),
        primary key (cust_id)
    ) engine=InnoDB
二月 07, 2022 12:04:22 上午 org.hibernate.engine.transaction.jta.platform.internal.JtaPlatformInitiator initiateService
INFO: HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
Hibernate: 
    insert 
    into
        cst_customer
        (cust_address, cust_name) 
    values
        (?, ?)

Process finished with exit code 0
```



- HQL是基於 JPQL 的擴充(更加強大)，例如可以再執行一些 SQL 函數、支持 JPQL 不支持的 INSERT 等等。 
  - 詳細可參考 Hibernate 官網文件
    - 文件內 `[]` 中括號的意思表示可寫可不寫
  - Spring Data JPA 也可以使用 HQL。



## Lec 08、基於 JPA 數據庫持久化操作

- 將 SSH 中的 Hibernate 替換成 JPA，有利於未來 實現類的切換

- 如果有接觸到 Spring Data JPA 的源碼，就會看到 EntityManager、Persistence 等 JPA API 的操作

  若實際運行會發現這些 API 底層實現也還是 Hibernate

- JPA 未提供單獨的 update 方法，想硬性規定只做 update 要自己寫 JPQL

  insert 的話要看 JPA 的實現類是否支持 (Hibernate 支持)

- 若真的有實際業務場景必須寫 SQL 也是可以的

- 無法刪除游離狀態的實例 (涉及 JPA 對象四大狀態觀念)，只能刪除持久化狀態的對象 (必須從數據庫里查出來的才是持久化對象)

  ```java
  ...
  Customer customer = new Customer();
  customer.setCustId(5L);
  
  entityManager.remove(customer);
  ...
  
  java.lang.IllegalArgumentException: Removing a detached instance org.example.pojo.Customer#5
  ```

  只能先查再刪的話，否則要寫 JPQL

  ```
  ...
  Customer customer = entityManager.find(Customer.class, 5L);
  entityManager.remove(customer);
  ...
  ```



## Lec 09、JPA 對象的四種狀態和緩存

TODO: 所以 JPA/Hibernate 將物件狀態設計成這樣的邏輯主要考量為何? 優勢在哪?



> Spring Data JPA 是在 JPA 的基礎上進行實現，如果項目開發中不了解 JPA 的這些特性。很容易陷入莫名奇妙更新數據庫或不更新數據庫等問題。



### JPA 對象的四種狀態

> **JPA entity lifecycle**: 
>
> - [JPA Entity lifecycle – JPA developer should know](https://javabydeveloper.com/jpa-entity-lifecycle-jpa-developer-should-know/)
> - https://matthung0807.blogspot.com/2018/06/hibernate-jpa-entity-jpa-entity.html
> - https://blog.csdn.net/yingxiake/article/details/50968059

![image-20220207134928372](spring-data-jpa.assets/jpa-entity-lifecycle.png)

- 臨時狀態: 剛創建(new)出來，尚未與 entityManager 發生關係，沒有被持久話，不在 entityManager 中的對象

- 持久狀態: 與 entityManager 發生關係，已經被持久化，可以把持久化狀態視為實實在在的數據庫記錄 (若對數據庫記錄修改則會進行同步)

  > 持久狀態下即使未呼叫 merge 之類的 API，一旦事務提交，仍然會執行 UPDATE
  >
  > 所以這也是我們必須好好理解這四種狀態關係的原因

- 刪除狀態: 執行 remove 方法，事務提交之前

- 游離狀態: 事務 commit 到數據庫後實體的狀態。因為事務已經提交了，此時實體的屬性改變也不會同步到數據庫。游離是沒人管的孩子，不在持久化上下文中。

  - 也就是說即使對象在數據庫中有對應的資料，但如果沒有和 entityManager 產生關聯，那就是一個游離狀態。



### JPA 緩存

- 基本上就是 Hibernate 緩存，因為是由 Hibernate 來實現的

  - 一級緩存: 同一個 entityManager 對象底下，同樣的查詢只會查一次 DB。也就是查完第一次 DB 之後都從緩存中獲取數據。

  - 二級緩存: 屬於應用級別，那怕是不同 entityManager 也只會查一次。後面一樣從緩存取得

  - 詳細不介紹，需要另外再深入去看 Hibernate。

    二級緩存一般不太會用到。

    一級緩存則需要知道一下，因為用不用不是你來決定XD 只要你在同一個 entityManager 下進行相同查詢默認就會用到



## Lec 10、Spring Data JPA 介紹

> 正式進入主軸，前 9 講屬於開胃菜(先備知識)



https://spring.io/projects/spring-data-jpa

Spring Data JPA, part of the larger Spring Data family, makes it easy to easily implement JPA based repositories. This module deals with enhanced support for JPA based data access layers. It makes it easier to build Spring-powered applications that use data access technologies.

Implementing a data access layer of an application has been cumbersome for quite a while. Too much boilerplate code has to be written to execute simple queries as well as perform pagination, and auditing. **Spring Data JPA aims to significantly improve the implementation of data access layers by reducing the effort to the amount that’s actually needed.** (Spring Data JPA 旨在改進數據訪問層的實現以提升開發效率) As a developer you write your repository interfaces, including custom finder methods, and Spring will provide the implementation automatically.



更簡單地說,

- Spring Data JPA 是 Spring 提供的一套框架，用來簡化 JPA 的開發 (例如不需再重複撰寫 entityManager 創建、 transaction commit 等代碼)。
- 依照約定好的規則進行 方法命名 去寫 DAO 層接口則可以不寫實現數據庫的訪問與操作。(https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- 提供除了 CRUD 以外的功能，如分頁、排序、複雜查詢等等
- 讓我們解脫了 DAO 層的操作，基本上所有 CRUD 都可以依賴它來實現。實際的開發中推薦使用 Spring Data JPA + ORM (如 Hibernate) ，未來若切換不同 ORM 框架也更方便，且對數據庫層的操作也更簡單，易於解耦。



### Features

- Sophisticated support to build repositories based on Spring and JPA
- Support for [Querydsl](http://www.querydsl.com/) predicates and thus type-safe JPA queries
- Transparent auditing of domain class
- Pagination support, dynamic query execution, ability to integrate custom data access code
- Validation of `@Query` annotated queries at bootstrap time
- Support for XML based entity mapping
- JavaConfig based repository configuration by introducing `@EnableJpaRepositories`.



上述特性講起來其實有點囉嗦，有些實際感受過才能體會，簡單來說就是 Spring Data JPA 極大地簡化了 DAO 層的代碼，我們只需要寫接口就**自動**具有了 CRUD、分頁查詢等方法，牛逼!



> Spring Data JPA 本質是利用了 JDK 的動態代理將 原生  JPA 包了一層而已
>
> > 如果動態代理是什麼不懂的話，最好額外自行去補充一下這種基礎知識!!!
> >
> > - https://www.gushiciku.cn/pl/glln/zh-tw
> > - https://www.liaoxuefeng.com/wiki/1252599548343744/1264804593397984



TODO: 架構圖



## Lec 11、Spring data JPA 搭建 XML 的配置方式

> 不建議直接創建 spring boot 項目來學習 Spring data，因為 spring boot 的自動配置類會幫你做好很多 Bean 的配置，我們會不知道到底 Spring data 需要配置那些 Bean，你會成為一個傻瓜式的 spring boot programmer
>
> P.S. 一定要知道 spring boot 幫我們配置了什麼，以後才能在 spring boot 的基礎上進行擴展。如果這些都不知道要談何擴展，更別說要去看懂它底層的一些源碼了。
>
> 自己去配置一遍，才能熟悉整體的體系，對未來才有實質幫助。



- 參照官方說明(https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#dependencies)添加依賴

  使用 spring-data-bom 統一管理所有 spring-data 子項目的版本可以避免未來一些版本衝突。

- 純 spring-data 項目要另外添加 Hibernate 依賴，Spring boot 默認 JPA 實現才是 Hibernate

- spring-test: 整合 Spring 項目的單元測試

- 連接池: 管理 Data Source、druid: 德魯伊

  - https://blog.csdn.net/hailongcsdn/article/details/106223821
    - 什麼是連接池？
      連接池是一種創建和管理連接的緩衝池技術，這些連接已經準備好被其他的線程進行調用。
    - 那為什麼要使用連接池呢？
      我們知道，連接的創建和銷毀時需要時間的，而連接池就是在服務器初始化的時候，創建好一些連接，然後把它們放到內存中的連接池裡，使用的時候，可以直接從內存中獲取。使用完成又歸還給連接池。這樣從內存中獲取和歸還連接的效率，遠遠高高每次連接的創建和銷毀，**大大提高了服務器的性能**，所以使用連接池是很用必要的。
  - https://www.gushiciku.cn/pl/gb9z/zh-tw
  - https://kknews.cc/zh-tw/code/nlr2k93.html

- 使用 XML 配置 spring

  - 官網的配置說明
    - 使用 XML https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.namespace
    - 使用 Java Config https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.java-config
  - https://www.itread01.com/content/1549220976.html
  - JPA 整合 Spring 概念是把 JPA 基本的 API (例如 EntityManagerFactory) 交給 Spring 來管理



> 要會使用 一手資料(官方文檔) 來學習，而非 二手資料 (別人再整理過的)，因為官方始終會更新