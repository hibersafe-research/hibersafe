#!/bin/bash
# Call the original entrypoint script to start the server
/usr/local/bin/docker-entrypoint.sh postgres &

# Wait for the database to be available
until pg_isready -h localhost -p 5432 -U postgres; do
    echo "Waiting for PostgreSQL to be ready..."
    sleep 2
done

# Create the 'vector' extension
psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "rag_db" <<-EOSQL
    CREATE EXTENSION IF NOT EXISTS vector;
EOSQL

# Keep the container running
wait