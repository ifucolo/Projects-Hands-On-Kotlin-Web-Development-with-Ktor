curl -X POST -d '{"id" : "100", "title" : "NEW", "author" : "Mari", "price" : 122.5}' \
 -H "Content-Type: application/json" "localhost:8080/book/100"