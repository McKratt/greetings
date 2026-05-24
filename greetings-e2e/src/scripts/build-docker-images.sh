#!/bin/bash
# Builds local Docker images for greetings-service and greetings-stat-service.
# Images are tagged to match compose-test.yaml expectations.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../.." && pwd)"

RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

print_step()    { echo -e "${BLUE}[$(date '+%H:%M:%S')] $1${NC}"; }
print_success() { echo -e "${GREEN}[$(date '+%H:%M:%S')] ✓ $1${NC}"; }
print_error()   { echo -e "${RED}[$(date '+%H:%M:%S')] ✗ $1${NC}"; }

cd "$PROJECT_ROOT"

if ! command -v docker &> /dev/null; then
    print_error "Docker is not installed or not in PATH"
    exit 1
fi

if ! docker info &> /dev/null; then
    print_error "Docker daemon is not running"
    exit 1
fi

VERSION=$(mvn -f greetings-service/pom.xml help:evaluate -Dexpression=project.version -q -DforceStdout)
GREETINGS_IMAGE="bakaar/greetings-service:${VERSION}"
STATS_IMAGE="bakaar/greetings-stat-service:${VERSION}"

echo ""
print_step "Building Docker images (version ${VERSION})"
echo ""

# --- greetings-service (Jib) ---
# Install all modules to local repo first (skip the bound jib:build which pushes to the registry)
print_step "Installing greetings-service modules..."
mvn -f greetings-service/pom.xml install -DskipTests -Djib.skip=true -q

# Build local Docker image with jib:dockerBuild using the buildImage profile config
print_step "Building Docker image ${GREETINGS_IMAGE}..."
mvn -f greetings-service/greetings-bootstrap/pom.xml -PbuildImage jib:dockerBuild -q
# Jib dockerBuild prefixes with localhost/ — retag so Docker Compose can resolve it
docker tag "localhost/${GREETINGS_IMAGE}" "${GREETINGS_IMAGE}"
print_success "${GREETINGS_IMAGE} ready"

echo ""

# --- greetings-stat-service (fabric8) ---
# Override image.name so the tag matches compose-test.yaml (strips the -sb3-aot suffix)
print_step "Packaging and building Docker image ${STATS_IMAGE}..."
mvn -f greetings-stat-service/pom.xml package -PbuildImage -DskipTests \
    -Dimage.name="${STATS_IMAGE}" -q
print_success "${STATS_IMAGE} ready"

echo ""
print_success "All images built. Local images:"
docker images --filter "reference=bakaar/*" --format "  {{.Repository}}:{{.Tag}}\t{{.Size}}\t{{.CreatedSince}}"
echo ""
