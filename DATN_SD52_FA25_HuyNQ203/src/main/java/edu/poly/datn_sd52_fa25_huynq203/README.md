(1) Application
    -  check: databaseName (url) & username & password

(2) Rules BE
    - Folder: 1 word-lowercase (e.g., controller, service, repository)
    - Class/Interface naming conventions: 
        + Names should be in PascalCase (e.g., MyClassName)
        + Nouns for classes (e.g., User, Product)
        + Verbs for interfaces (e.g., Runnable, Serializable)
    - Const: UPPER_SNAKE_CASE (e.g., MAX_VALUE)
    - Method: camelCase + verb (e.g., calculateTotal, getUserName)
    - Variable: 
        + camelCase + noun (e.g., userName, totalAmount)
        + Avoid: tên entity nên đặt chung: name thay vì userName, productName

(3) endpoint API (Controller) Restfull API
    -  

(4) Response
    - Sử dụng chung 1 ResponseData<T> để trả về dữ liệu cho Client.
    - ResponseData<T> gồm:
        + status: int (200, 400, 500, ...)
        + message: String
        + data: T (dữ liệu trả về)
    