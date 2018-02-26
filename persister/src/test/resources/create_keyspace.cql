CREATE KEYSPACE IF NOT EXISTS documents
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};
