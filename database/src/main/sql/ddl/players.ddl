CREATE TABLE tanknite.public.players
(
  id              SERIAL NOT NULL
    CONSTRAINT table_name_pk
      PRIMARY KEY,
  username        TEXT   NOT NULL,
  password        TEXT   NOT NULL,
  tank_colour     INTEGER DEFAULT 1,
  top_kill_streak INTEGER DEFAULT 0,
  kills           INTEGER DEFAULT 0,
  deaths          INTEGER DEFAULT 0,
  win             INTEGER DEFAULT 0,
  loss            INTEGER DEFAULT 0
)
;

ALTER TABLE players
  OWNER TO example
;

CREATE UNIQUE INDEX table_name_id_uindex
  ON players (id)
;

CREATE UNIQUE INDEX table_name_username_uindex
  ON players (username)
;