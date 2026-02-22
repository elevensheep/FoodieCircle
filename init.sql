-- Create schemas for microservices
CREATE SCHEMA IF NOT EXISTS user_schema;
CREATE SCHEMA IF NOT EXISTS map_schema;
CREATE SCHEMA IF NOT EXISTS feed_schema;
CREATE SCHEMA IF NOT EXISTS schedule_schema;

-- Grant usage to the user (if using a non-superuser, but we are using 'user' which owns the DB in docker-compose)
-- ALTER SCHEMA user_schema OWNER TO "user";
-- ALTER SCHEMA map_schema OWNER TO "user";
-- ALTER SCHEMA feed_schema OWNER TO "user";
-- ALTER SCHEMA schedule_schema OWNER TO "user";
