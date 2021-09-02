curl -X PUT -d '{"id" : "100", "title" : "new book", "author" : "Iago", "price" : 2.5}' \
 -H "Content-Type: application/json" "localhost:8080/book"