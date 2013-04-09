CREATE TABLE user_type (
	id BIGINT NOT NULL,
	description VARCHAR(64) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE users (
	id BIGINT NOT NULL,
	full_name VARCHAR(64) NOT NULL,
	email VARCHAR(64) NOT NULL,
	password VARCHAR(32) NOT NULL,
	active BOOLEAN DEFAULT TRUE,
	admin BOOLEAN DEFAULT FALSE,
	user_type_id BIGINT NOT NULL,
	PRIMARY KEY(id)
);

ALTER TABLE users ADD CONSTRAINT fk_user_user_type FOREIGN KEY(user_type_id) REFERENCES user_type(id);
ALTER TABLE users ADD CONSTRAINT unique_full_name UNIQUE(full_name);
ALTER TABLE users ADD CONSTRAINT unique_email UNIQUE(email);

CREATE TABLE request_type (
	id BIGINT NOT NULL,
	description VARCHAR(64) NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE requests (
	id BIGINT NOT NULL,
	from_date DATE NOT NULL,
	to_date DATE NOT NULL,
	ask_comment VARCHAR(256),
	answer_comment VARCHAR(256),
	request_type_id BIGINT NOT NULL,
	status VARCHAR(256) NOT NULL,
	user_id BIGINT NOT NULL,
	PRIMARY KEY(id)
);

ALTER TABLE requests ADD CONSTRAINT fk_request_request_type FOREIGN KEY(request_type_id) REFERENCES request_type(id);
ALTER TABLE requests ADD CONSTRAINT fk_request_user FOREIGN KEY(user_id) REFERENCES users(id);