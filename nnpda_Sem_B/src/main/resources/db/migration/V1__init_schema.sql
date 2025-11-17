CREATE TABLE application_users (
                                   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                   username VARCHAR(50) NOT NULL UNIQUE,
                                   email VARCHAR(255) NOT NULL UNIQUE,
                                   password VARCHAR(255) NOT NULL
);

CREATE TABLE projects (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name VARCHAR(120) NOT NULL,
                          description VARCHAR(255),
                          state VARCHAR(50) NOT NULL,
                          owner_id BIGINT NOT NULL REFERENCES application_users(id)
);

CREATE TABLE ticket_states (
    code VARCHAR(50) PRIMARY KEY
);

INSERT INTO ticket_states(code) VALUES ('OPEN'), ('IN_PROGRESS'), ('DONE');

CREATE TABLE ticket_priorities (
    code VARCHAR(50) PRIMARY KEY
);

INSERT INTO ticket_priorities(code) VALUES ('LOW'), ('MED'), ('HIGH');

CREATE TABLE tickets (
                         id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         title VARCHAR(160) NOT NULL,
                         type VARCHAR(50) NOT NULL,
                         priority VARCHAR(50) NOT NULL REFERENCES ticket_priorities(code),
                         state VARCHAR(50) NOT NULL REFERENCES ticket_states(code),
                         project_id BIGINT NOT NULL REFERENCES projects(id),
                         solver_id BIGINT REFERENCES application_users(id),
                         last_modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ticket_histories (
                                  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                  ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                                  old_state VARCHAR(50) NOT NULL REFERENCES ticket_states(code),
                                  new_state VARCHAR(50) NOT NULL REFERENCES ticket_states(code),
                                  changed_by_id BIGINT NOT NULL REFERENCES application_users(id),
                                  changed_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ticket_comments (
                                 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                                 author_id BIGINT NOT NULL REFERENCES application_users(id),
                                 content VARCHAR(2000) NOT NULL,
                                 created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ticket_attachments (
                                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
                                    uploaded_by_id BIGINT NOT NULL REFERENCES application_users(id),
                                    file_name VARCHAR(255) NOT NULL,
                                    url VARCHAR(500) NOT NULL,
                                    uploaded_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);