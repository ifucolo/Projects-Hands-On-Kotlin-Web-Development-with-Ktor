#curl -X POST "localhost:8080/fuelup/607da4ad147ca82b1d59c949"
#curl -X POST -v -H "Content-Type: application/json" -d '{"id": "607da4ad147ca82b1d59c949", "name": "iago", "fuel" : 20.0}' "localhost:8080/replace"
#curl "localhost:8080/ships?fuelmin=40"
curl "localhost:8080/sortedships?page=2&pagesize=1"