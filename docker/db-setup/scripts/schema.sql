CREATE SCHEMA IF NOT EXISTS event;

CREATE TABLE IF NOT EXISTS event.events (
    UserId       INT                NOT NULL,
    GameId       VARCHAR(50)       NOT NULL,
    State        BYTEA              NOT NULL,
    GameRoundId  BIGINT PRIMARY KEY NOT NULL,
    Id           SERIAL,
   GameRoundData BYTEA 
)
