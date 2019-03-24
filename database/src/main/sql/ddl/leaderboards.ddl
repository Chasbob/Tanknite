CREATE TABLE IF NOT EXISTS tanknite.public.leaderboards
(
  id   SERIAL NOT NULL
    CONSTRAINT leaderboard_pk
      PRIMARY KEY,
  name TEXT
)
;

ALTER TABLE leaderboards
  OWNER TO example
;

CREATE UNIQUE INDEX leaderboard_id_uindex
  ON leaderboards (id)
;