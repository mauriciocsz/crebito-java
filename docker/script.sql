CREATE UNLOGGED TABLE IF NOT EXISTS "user" (
	"id" VARCHAR(100) PRIMARY KEY,
	credit_limit BIGINT NOT NULL,
	balance BIGINT NOT NULL
);

CREATE UNLOGGED TABLE IF NOT EXISTS "transaction" (
	"id" SERIAL PRIMARY KEY,
	"value" BIGINT NOT NULL,
	"type" VARCHAR(1) NOT NULL,
	description TEXT,
	date TIMESTAMP NOT NULL,
	user_id VARCHAR(100) REFERENCES "user"("id")
);

INSERT INTO "user"
	("id", "credit_limit", balance)
VALUES
	('1',100000,0),
	('2',80000,0),
	('3',1000000,0),
	('4',10000000,0),
	('5',500000,0);