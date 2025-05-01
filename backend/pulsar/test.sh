
#!/bin/bash

executar_teste() {
    local DESCRICAO=$1
    local COMANDO=$2

    echo " $DESCRICAO"
    HTTP_STATUS=$(eval "$COMANDO -s -o /dev/null -w '%{http_code}'")

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo " Sucesso (HTTP $HTTP_STATUS)"
    else
        echo " Falha (HTTP $HTTP_STATUS)"
    fi
    echo ""
}

# Lista de testes para /card/create
declare -a TESTES_CREATE=(
    "Criando card na coluna 0 (Teste 1)|curl -X POST 'localhost:8080/card/create?col=0' -H 'Content-Type: text/plain' -d 'Teste 1'"
    "Criando card na coluna 1 (Teste 2)|curl -X POST 'localhost:8080/card/create?col=1' -H 'Content-Type: text/plain' -d 'Teste 2'"
    "Criando card na coluna 0 (Teste 3)|curl -X POST 'localhost:8080/card/create?col=0' -H 'Content-Type: text/plain' -d 'Teste 3'"
    "Criando card na coluna 0 (Teste 4)|curl -X POST 'localhost:8080/card/create?col=0' -H 'Content-Type: text/plain' -d 'Teste 4'"
)

# Lista de testes para /card/get
declare -a TESTES_GET=(
    "Obtendo cards da coluna 0|curl -X GET 'localhost:8080/card/get?col=0' -H 'Content-Type: text/plain'"
    "Obtendo cards da coluna 1|curl -X GET 'localhost:8080/card/get?col=1' -H 'Content-Type: text/plain'"
)

# Lista de testes para /card/delete
declare -a TESTES_DELETE=(
    "Deletando card 0 da coluna 0|curl -X DELETE 'localhost:8080/card/delete?col=0&index=0' -H 'Content-Type: text/plain'"
    "Deletando card 1 da coluna 1|curl -X DELETE 'localhost:8080/card/delete?col=1&index=1' -H 'Content-Type: text/plain'"
)

# Lista de testes para /card/update
declare -a TESTES_UPDATE=(
    "Atualizando card 0 da coluna 0|curl -X PUT 'localhost:8080/card/update?col=0&index=0' -H 'Content-Type: text/plain' -d 'Teste Atualizado'"
    "Atualizando card 1 da coluna 1|curl -X PUT 'localhost:8080/card/update?col=1&index=1' -H 'Content-Type: text/plain' -d 'Teste Atualizado'"
)

# Lista de testes para /card/move
declare -a TESTES_MOVE=(
    "Movendo card 0 da coluna 0 para coluna 1|curl -X PUT 'localhost:8080/card/move?col=0&newCol=1&index=0&newIndex=0' -H 'Content-Type: text/plain'"
    "Movendo card 1 da coluna 1 para coluna 0|curl -X PUT 'localhost:8080/card/move?col=1&newCol=0&index=0&newIndex=0' -H 'Content-Type: text/plain'"
)

echo "Teste /card/create"
for TESTE in "${TESTES_CREATE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

echo "Teste /card/get"
for TESTE in "${TESTES_GET[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

echo "Teste /card/delete"
for TESTE in "${TESTES_DELETE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

echo "Teste /card/update"
for TESTE in "${TESTES_UPDATE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

echo "Teste /card/move"
for TESTE in "${TESTES_MOVE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done
