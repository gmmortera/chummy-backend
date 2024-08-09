-- !Ups
CREATE TABLE "REPLIES"(
  "ID" uuid PRIMARY KEY,
  "ID_USER" uuid REFERENCES "USERS",
  "ID_COMMENT" uuid REFERENCES "COMMENTS",
  "TEXT" text NOT NULL,
  "CREATED_AT" timestamp NOT NULL,
  "UPDATED_AT" timestamp
);

-- !Downs
DROP TABLE IF EXISTS "REPLIES";