debop4j-redis
==============================


Redis Commands

List
------------------------------
LPUSH + LPOP   = Stack
LPUSH + RPOP   = Queue
LPUsH + LTRIM  = Capped Collection
LPUSH + BRPOP  = Realtime Message Queue

Set
------------------------------
SADD = Tagging
SPOP = Random item
SADD + SINTER = Social Graph
Sorted Seets = Set w/ score


Hash
------------------------------
SET + EXPIRE  = Sessions
SET + EXPIRE  = Invalid Cache
INC + EXPIRE  = Rate Limiting