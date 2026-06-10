#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="$SCRIPT_DIR/.env"

if [[ -f "$ENV_FILE" ]]; then
  echo "Cargando variables de entorno desde $ENV_FILE"
  set -a
  # shellcheck disable=SC1090
  source "$ENV_FILE"
  set +a
else
  echo "Advertencia: no se encontró $ENV_FILE. Usando valores por defecto del código." >&2
fi

cd "$SCRIPT_DIR"

# Ejecuta los tests de Gradle
./gradlew clean build
