# spring-data-jpa



## Idea

- .var: assign variable
- .new: new instance
- sout: system out



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



