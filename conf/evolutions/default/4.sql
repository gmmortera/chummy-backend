-- !Ups
CREATE TABLE "COMMENTS"(
  "ID" uuid PRIMARY KEY,
  "ID_USER" uuid,
  "ID_POST" uuid REFERENCES "POSTS",
  "TEXT" text NOT NULL,
  "CREATED_AT" timestamp NOT NULL
);

-- !Downs
DROP TABLE IF EXISTS "COMMENTS";