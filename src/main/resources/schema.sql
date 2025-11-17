CREATE TABLE IF NOT EXISTS users(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  password VARCHAR(65) NOT NULL,
  email VARCHAR(100) NOT NULL UNIQUE,
  nickname VARCHAR(100) NOT NULL UNIQUE,
  INDEX idx_user_nickname (nickname),
  INDEX idx_user_email (email)
);

CREATE TABLE IF NOT EXISTS board(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  visitors INT NOT NULL DEFAULT 0,
  title VARCHAR(150) NOT NULL,
  content VARCHAR(1000) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_board_user_id (user_id),
  INDEX idx_board_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS comment(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  board_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  content VARCHAR(300) NOT NULL,
  FOREIGN KEY (board_id) REFERENCES board(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_comment_board_id (board_id),
  INDEX idx_comment_user_id (user_id),
  INDEX idx_comment_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS  likes(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  board_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  UNIQUE KEY uq_like_board_user (board_id, user_id),
  FOREIGN KEY (board_id) REFERENCES board(id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX idx_like_board_id (board_id),
  INDEX idx_like_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS  image (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
);

CREATE TABLE IF NOT EXISTS  board_image(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  board_id BIGINT NOT NULL,
  image_id BIGINT NOT NULL,
  UNIQUE KEY uq_board_image (board_id, image_id),
  FOREIGN KEY (board_id) REFERENCES board(id),
  FOREIGN KEY (image_id) REFERENCES image(id)
  INDEX idx_board_image_board_id (board_id),
  INDEX idx_board_image_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS  user_image(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  user_id BIGINT NOT NULL,
  image_id BIGINT NOT NULL,
  UNIQUE KEY uq_user_image (user_id, image_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (image_id) REFERENCES image(id)
  INDEX idx_user_image_user_id (user_id),
  INDEX idx_user_image_created_at (created_at)
);