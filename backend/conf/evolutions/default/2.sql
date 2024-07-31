-- !Ups
CREATE TABLE "POSTS"(
  "ID" uuid PRIMARY KEY,
  "ID_USER" uuid REFERENCES "USERS",
  "IMAGE" text,
  "CONTENT" text NOT NULL,
  "CREATED_AT" timestamp NOT NULL
);

-- !Downs
DROP TABLE IF EXISTS "POSTS";