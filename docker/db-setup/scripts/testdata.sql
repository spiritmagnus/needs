--userid | gameid | state | gameroundid | id | gamerounddata
INSERT INTO event.events (userid, gameid, state, gameroundid, gamerounddata) values (1, 'bananas', 'xxx', 1, 'yyy');
INSERT INTO event.events (userid, gameid, state, gameroundid, gamerounddata) values (1, 'bananas', 'xxx', 2, 'yyy');
SELECT * FROM event.events ORDER BY id DESC
