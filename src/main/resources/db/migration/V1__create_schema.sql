CREATE TABLE users (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL
);

CREATE TABLE courses (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    average_method VARCHAR(512) NOT NULL,
    cut_off_grade  DOUBLE       NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projections (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id   BIGINT       NOT NULL,
    name        VARCHAR(255) NOT NULL,
    final_grade DOUBLE       NOT NULL DEFAULT 0.0,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE assessments (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    projection_id  BIGINT       NOT NULL,
    identifier     VARCHAR(50)  NOT NULL,
    max_value      DOUBLE       NOT NULL,
    grade          DOUBLE       NOT NULL DEFAULT 0.0,
    required_grade DOUBLE       NOT NULL DEFAULT 0.0,
    fixed          BOOLEAN      NOT NULL DEFAULT FALSE,
    FOREIGN KEY (projection_id) REFERENCES projections(id) ON DELETE CASCADE
);
