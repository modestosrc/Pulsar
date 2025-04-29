echo "Teste /card/create"
curl localhost:8080/card/create?col=0 -H "Content-Type: text/plain" -d "Teste 1"
curl localhost:8080/card/create?col=1 -H "Content-Type: text/plain" -d "Teste 2"
curl localhost:8080/card/create?col=0 -H "Content-Type: text/plain" -d "Teste 3"

echo "Teste /card/get"
curl localhost:8080/card/get?col=0 -H "Content-Type: text/plain"

# Commit teste
