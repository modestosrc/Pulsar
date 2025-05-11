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
NUM_ERROS=0
executar_teste() {
    local DESCRICAO=$1
    local COMANDO=$2

    echo " $DESCRICAO"
    HTTP_STATUS=$(eval "$COMANDO -s -o /dev/null -w '%{http_code}'")

    if [ "$HTTP_STATUS" -eq 200 ]; then
        echo " Sucesso (HTTP $HTTP_STATUS)"

    else
        NUM_ERROS=$((NUM_ERROS + 1))
        echo " Falha (HTTP $HTTP_STATUS)"
    fi
    echo ""
}

# Lista de testes para create /{user}/coluna: cria as colunas
declare -a TESTES_CREATE=(
    "Criando coluna 0|curl -X POST '$API_URL/$USERNAME/coluna' -H 'Content-Type: application/json' -H 'Authorization: Bearer $JWT'"

    "Criando coluna 1|curl -X POST '$API_URL/$USERNAME/coluna' -H 'Content-Type: application/json' -H 'Authorization: Bearer $JWT'"
)

# Lista de testes para set /{user}/coluna/{coluna}: put na coluna com cards
declare -a TESTES_SET=(
    "Adicionando card 0 na coluna 0|curl -X PUT \"$API_URL/$USERNAME/coluna/0\" -H 'Content-Type: application/json' -H \"Authorization: Bearer $JWT\" -d '{\"id\":0, \"name\":\"Coluna 0\", \"user\":\"'$USERNAME'\", \"cards\":[{\"name\":\"Teste 1\"}]}'"

    "Adicionando card 1 na coluna 0|curl -X PUT \"$API_URL/$USERNAME/coluna/0\" -H 'Content-Type: application/json' -H \"Authorization: Bearer $JWT\" -d '{\"id\":0, \"name\":\"Coluna 0\", \"user\":\"'$USERNAME'\", \"order\":0, \"cards\":[{\"name\":\"Teste 1\"},{\"name\":\"Teste 2\"}]}'"
)

# Lista de testes para delete /{user}/coluna/{coluna}: delete na coluna
declare -a TESTES_DELETE=(
    "Deletando coluna 1|curl -X DELETE '$API_URL/$USERNAME/coluna/1' -H 'Authorization: Bearer $JWT'"
)

# Lista de testes para get /{user}/coluna: get nas colunas
declare -a TESTES_GET=(
    "Obtendo coluna 0|curl -X GET '$API_URL/$USERNAME/coluna/0' -H 'Authorization: Bearer $JWT'"

    "Obtendo colunas |curl -X GET '$API_URL/$USERNAME/coluna' -H 'Authorization: Bearer $JWT'"
)

print_line() {
    echo "----------------------------------------"
}

# Execução dos testes
print_line
for TESTE in "${TESTES_CREATE[@]}"; do
    DESCRICAO=$(echo "$TESTE" | cut -d'|' -f1)
    COMANDO=$(echo "$TESTE" | cut -d'|' -f2)
    executar_teste "$DESCRICAO" "$COMANDO"
done
print_line
for TESTE in "${TESTES_SET[@]}"; do
    DESCRICAO=$(echo "$TESTE" | cut -d'|' -f1)
    COMANDO=$(echo "$TESTE" | cut -d'|' -f2)
    executar_teste "$DESCRICAO" "$COMANDO"
done
print_line
for TESTE in "${TESTES_DELETE[@]}"; do
    DESCRICAO=$(echo "$TESTE" | cut -d'|' -f1)
    COMANDO=$(echo "$TESTE" | cut -d'|' -f2)
    executar_teste "$DESCRICAO" "$COMANDO"
done
print_line
for TESTE in "${TESTES_GET[@]}"; do
    DESCRICAO=$(echo "$TESTE" | cut -d'|' -f1)
    COMANDO=$(echo "$TESTE" | cut -d'|' -f2)
    executar_teste "$DESCRICAO" "$COMANDO"
done
print_line

# Resumo dos testes
print_line
if [ $NUM_ERROS -eq 0 ]; then
    echo "✅ Todos os testes passaram com sucesso!"
else
    echo "❌ $NUM_ERROS teste(s) falharam."
fi
