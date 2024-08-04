CREATE TABLE user
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE file
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(50) NOT NULL,
    filePath VARCHAR(50) NOT NULL
);

CREATE TABLE event
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    user_id INT,
    file_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (file_id) REFERENCES file(id)
);
