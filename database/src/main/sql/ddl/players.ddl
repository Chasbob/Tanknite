CREATE TABLE aticatac.public.players
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
  loss            INTEGER DEFAULT 0,
  score           INTEGER DEFAULT 0,
  xp              INTEGER DEFAULT 0
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

CREATE OR REPLACE FUNCTION calc_score(_kills           INT, _deaths INT, _win INT, _loss INT,
                                      _top_kill_streak INT)
  RETURNS INT AS
$$
BEGIN
  RETURN (_kills - _deaths + _win - _loss + _top_kill_streak * 10);
END;
$$
  LANGUAGE 'plpgsql'
;