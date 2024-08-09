-- !Ups
CREATE TABLE "NOTIFICATIONS"(
  "ID" uuid PRIMARY KEY,
  "ID_USER" uuid REFERENCES "USERS",
  "ID_POST" uuid REFERNCES "POSTS",
  "ACTION" varchar(10) NOT NULL,
  "CREATED_AT" timestamp NOT NULL,
  "SEEN_AT" timestamp
);

-- !Downs
DROP TABLE IF EXISTS "NOTIFICATIONS";