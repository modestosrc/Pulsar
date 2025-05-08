#!/bin/bash

# HEADER
echo ""
echo "▄▖  ▜         ▌     ▌      ▌   ▄▖▄▖▄▖▄▖"
echo "▙▌▌▌▐ ▛▘▀▌▛▘  ▛▌▀▌▛▘▙▘█▌▛▌▛▌▖  ▐ ▙▖▚ ▐ "
echo "▌ ▙▌▐▖▄▌█▌▌   ▙▌█▌▙▖▛▖▙▖▌▌▙▌▖  ▐ ▙▖▄▌▐ "
                                       



API_URL="http://localhost:8080"
USERNAME="test_user"
PASSWORD="1504"

# Obter token JWT
echo "Realizando login para obter token..."
JWT=$(curl -s -X POST "$API_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d "{\"username\":\"$USERNAME\", \"password\":\"$PASSWORD\"}")

if [[ -z "$JWT" ]]; then
    echo "❌ Falha ao obter token JWT"
    exit 1
fi

echo "✅ Token JWT obtido!"
echo ""

# Função para executar testes
NUM_TESTES_SUCESSO=0
executar_teste() {
    local DESCRICAO=$1
    local COMANDO=$2

    echo " $DESCRICAO"
    HTTP_STATUS=$(eval "$COMANDO -s -o /dev/null -w '%{http_code}'")

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo " Sucesso (HTTP $HTTP_STATUS)"
        NUM_TESTES_SUCESSO=$((NUM_TESTES_SUCESSO + 1))

    else
        echo " Falha (HTTP $HTTP_STATUS)"
    fi
    echo ""
}

# Lista de testes para /card/create
declare -a TESTES_CREATE=(
    "Criando card na coluna 0 (Teste 1)|curl -X POST '$API_URL/col/create?col=0' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT' -d 'Teste 1'"
    "Criando card na coluna 1 (Teste 2)|curl -X POST '$API_URL/col/create?col=1' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT' -d 'Teste 2'"
)

# Lista de testes para /card/get
declare -a TESTES_GET=(
    "Obtendo cards da coluna 0|curl -X GET '$API_URL/col/get?col=0' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT'"
    "Obtendo cards da coluna 1|curl -X GET '$API_URL/col/get?col=1' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT'"
)

# Lista de testes para /card/delete
declare -a TESTES_DELETE=(
    "Deletando card 0 da coluna 1|curl -X DELETE '$API_URL/col/delete?col=1&index=0' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT'"
)

# Lista de testes para /card/update
declare -a TESTES_UPDATE=(
    "Atualizando card 0 da coluna 0|curl -X PUT '$API_URL/col/update?col=0&index=0' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT' -d 'Teste Atualizado 1'"
)

# Lista de testes para /card/move
declare -a TESTES_MOVE=(
    "Movendo card 0 da coluna 0 para coluna 1|curl -X PUT '$API_URL/col/move?col=0&newCol=1&index=0&newIndex=0' -H 'Content-Type: text/plain' -H 'Authorization: Bearer $JWT'"
)

# Contar numero de testes:
NUM_TESTES_TOTAL=$(( ${#TESTES_CREATE[@]} + ${#TESTES_GET[@]} + ${#TESTES_DELETE[@]} + ${#TESTES_UPDATE[@]} + ${#TESTES_MOVE[@]} ))

print_line() {
    echo "----------------------------------------"
}


# Execução dos testes
print_line
echo "Teste /card/create"
for TESTE in "${TESTES_CREATE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

print_line
echo "Teste /card/get"
for TESTE in "${TESTES_GET[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

print_line
echo "Teste /card/update"
for TESTE in "${TESTES_UPDATE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

print_line
echo "Teste /card/move"
for TESTE in "${TESTES_MOVE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

print_line
echo "Teste /card/delete"
for TESTE in "${TESTES_DELETE[@]}"; do
    DESCRICAO="${TESTE%%|*}"
    COMANDO="${TESTE##*|}"
    executar_teste "$DESCRICAO" "$COMANDO"
done

# Resumo dos testes
print_line
echo "($NUM_TESTES_SUCESSO/$NUM_TESTES_TOTAL) testes passaram com sucesso."
if [ $NUM_TESTES_SUCESSO -eq $NUM_TESTES_TOTAL ]; then
    exit 0
else
    exit 1
fi
