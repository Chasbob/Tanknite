CREATE TABLE IF NOT EXISTS aticatac.public.challenges
(
  id          SERIAL                              NOT NULL
    CONSTRAINT challenges_pk
      PRIMARY KEY,
  date        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  description TEXT,
  reward      INT       DEFAULT 0
)
;

ALTER TABLE challenges
  OWNER TO example
;

CREATE UNIQUE INDEX challenges_id_uindex
  ON challenges (id)
;