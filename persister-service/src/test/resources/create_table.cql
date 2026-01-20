CREATE KEYSPACE IF NOT EXISTS documents WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS documents.documents (
    id uuid PRIMARY KEY,
    author text,
    text text
);
