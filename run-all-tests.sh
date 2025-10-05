#!/bin/bash

# Comprehensive build and test script for the Greetings project
# This script runs all projects with tests in the correct order

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_step() {
    echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')] $1${NC}"
}

print_success() {
    echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')] ✓ $1${NC}"
}

print_error() {
    echo -e "${RED}[$(date '+%Y-%m-%d %H:%M:%S')] ✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}[$(date '+%Y-%m-%d %H:%M:%S')] ⚠ $1${NC}"
}

# Function to check if required tools are installed
check_prerequisites() {
    print_step "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 25+ (Temurin distribution recommended)"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$java_version" -lt 25 ]; then
        print_warning "Java version is $java_version. Java 25+ is recommended for this project"
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed. Please install Maven 3.8+"
        exit 1
    fi
    
    # Check Node.js
    if ! command -v node &> /dev/null; then
        print_error "Node.js is not installed. Please install Node.js 18+"
        exit 1
    fi
    
    node_version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$node_version" -lt 18 ]; then
        print_error "Node.js version is $node_version. Node.js 18+ is required"
        exit 1
    fi
    
    # Check yarn
    if ! command -v yarn &> /dev/null; then
        print_error "yarn is not installed. Please install yarn"
        exit 1
    fi
    
    print_success "All prerequisites are satisfied"
}

# Function to clean all projects
clean_all() {
    print_step "Cleaning all projects..."
    
    # Clean Maven projects
    print_step "Cleaning Maven projects..."
    mvn clean -f greetings-service/pom.xml || { print_error "Failed to clean greetings-service"; exit 1; }
    mvn clean -f greetings-stat-service/pom.xml || { print_error "Failed to clean greetings-stat-service"; exit 1; }
    mvn clean -f greetings-e2e/pom.xml || { print_error "Failed to clean greetings-e2e"; exit 1; }
    
    # Clean UI project
    print_step "Cleaning UI project..."
    cd greetings-ui
    rm -rf node_modules dist coverage .nyc_output 2>/dev/null || true
    cd ..
    
    print_success "All projects cleaned"
}

# Function to build and test greetings service
build_greetings_service() {
    print_step "Building and testing Greetings Service..."
    
    cd greetings-service
    
    # Build
    print_step "Compiling greetings-service..."
    mvn -B -T 1C clean compile -Pcoverage || { print_error "Failed to compile greetings-service"; exit 1; }
    
    # Unit tests
    print_step "Running unit tests for greetings-service..."
    mvn -B -T 1C -Pcoverage test -Dmaven.main.skip || { print_error "Unit tests failed for greetings-service"; exit 1; }
    
    # Integration tests
    print_step "Running integration tests for greetings-service..."
    mvn -B -T 1C -Pcoverage verify -Dmaven.main.skip -DskipUTs || { print_error "Integration tests failed for greetings-service"; exit 1; }
    
    cd ..
    print_success "Greetings Service build and tests completed"
}

# Function to build and test stats service
build_stats_service() {
    print_step "Building and testing Stats Service..."
    
    cd greetings-stat-service
    
    # Build
    print_step "Compiling greetings-stat-service..."
    mvn -B -T 1C clean compile -Pcoverage || { print_error "Failed to compile greetings-stat-service"; exit 1; }
    
    # Unit tests
    print_step "Running unit tests for greetings-stat-service..."
    mvn -B -T 1C -Pcoverage test -Dmaven.main.skip || { print_error "Unit tests failed for greetings-stat-service"; exit 1; }
    
    # Integration tests
    print_step "Running integration tests for greetings-stat-service..."
    mvn -B -T 1C -Pcoverage verify -Dmaven.main.skip -DskipUTs || { print_error "Integration tests failed for greetings-stat-service"; exit 1; }
    
    cd ..
    print_success "Stats Service build and tests completed"
}

# Function to build and test UI
build_ui() {
    print_step "Building and testing UI..."
    
    cd greetings-ui
    
    # Install dependencies
    print_step "Installing UI dependencies..."
    yarn install || { print_error "Failed to install UI dependencies"; exit 1; }

    # Run tests
    print_step "Running UI tests..."
    yarn test || { print_error "UI tests failed"; exit 1; }

    # Run Pact tests
    print_step "Running UI Pact tests..."
    yarn run pact || { print_error "UI Pact tests failed"; exit 1; }

    # Build UI
    print_step "Building UI..."
    yarn run build || { print_error "UI build failed"; exit 1; }
    
    cd ..
    print_success "UI build and tests completed"
}

# Function to run E2E tests
run_e2e_tests() {
    print_step "Running E2E tests..."
    
    cd greetings-e2e
    mvn -B test || { print_error "E2E tests failed"; exit 1; }
    cd ..
    
    print_success "E2E tests completed"
}

# Function to generate coverage reports
generate_coverage_reports() {
    print_step "Generating coverage reports..."
    
    # Generate coverage for greetings-service
    cd greetings-service
    mvn -B -Pcoverage jacoco:report || print_warning "Failed to generate coverage report for greetings-service"
    cd ..
    
    # Generate coverage for greetings-stat-service
    cd greetings-stat-service
    mvn -B -Pcoverage jacoco:report || print_warning "Failed to generate coverage report for greetings-stat-service"
    cd ..
    
    print_success "Coverage reports generated"
}

# Main execution function
main() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  Greetings Project Build & Test Script${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
    
    start_time=$(date +%s)
    
    # Parse command line arguments
    SKIP_CLEAN=false
    SKIP_E2E=false
    SKIP_UI=false
    PARALLEL_SERVICES=true
    
    while [[ $# -gt 0 ]]; do
        case $1 in
            --skip-clean)
                SKIP_CLEAN=true
                shift
                ;;
            --skip-e2e)
                SKIP_E2E=true
                shift
                ;;
            --skip-ui)
                SKIP_UI=true
                shift
                ;;
            --no-parallel)
                PARALLEL_SERVICES=false
                shift
                ;;
            -h|--help)
                echo "Usage: $0 [OPTIONS]"
                echo "Options:"
                echo "  --skip-clean     Skip the clean step"
                echo "  --skip-e2e       Skip E2E tests"
                echo "  --skip-ui        Skip UI build and tests"
                echo "  --no-parallel    Build services sequentially instead of in parallel"
                echo "  -h, --help       Show this help message"
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                echo "Use --help for usage information"
                exit 1
                ;;
        esac
    done
    
    # Check prerequisites
    check_prerequisites
    
    # Clean projects (optional)
    if [ "$SKIP_CLEAN" = false ]; then
        clean_all
    else
        print_warning "Skipping clean step"
    fi
    
    # Build and test services
    if [ "$PARALLEL_SERVICES" = true ]; then
        print_step "Building services in parallel..."
        # Run services in parallel
        (build_greetings_service) &
        pid1=$!
        (build_stats_service) &
        pid2=$!
        
        # Wait for both to complete
        wait $pid1 || { print_error "Greetings service build failed"; exit 1; }
        wait $pid2 || { print_error "Stats service build failed"; exit 1; }
        
        print_success "Both services built successfully"
    else
        print_step "Building services sequentially..."
        build_greetings_service
        build_stats_service
    fi
    
    # Build and test UI
    if [ "$SKIP_UI" = false ]; then
        build_ui
    else
        print_warning "Skipping UI build and tests"
    fi
    
    # Run E2E tests
    if [ "$SKIP_E2E" = false ]; then
        run_e2e_tests
    else
        print_warning "Skipping E2E tests"
    fi
    
    # Generate coverage reports
    generate_coverage_reports
    
    # Calculate and display total time
    end_time=$(date +%s)
    duration=$((end_time - start_time))
    minutes=$((duration / 60))
    seconds=$((duration % 60))
    
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  BUILD AND TESTS COMPLETED SUCCESSFULLY${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}Total time: ${minutes}m ${seconds}s${NC}"
    echo ""
    
    # Display coverage report locations
    echo -e "${BLUE}Coverage reports available at:${NC}"
    echo "  - greetings-service/target/site/jacoco/index.html"
    echo "  - greetings-stat-service/target/site/jacoco/index.html"
    echo ""
}

# Trap to handle script interruption
trap 'echo -e "\n${RED}Script interrupted by user${NC}"; exit 1' INT

# Run main function with all arguments
main "$@"