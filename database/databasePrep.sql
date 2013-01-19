-- Table creation statements:
CREATE TABLE call_number (
       id INT AUTO_INCREMENT PRIMARY KEY,
       text_value VARCHAR(40) ,
       start_or_end ENUM('start', 'end')
);

CREATE TABLE stacks_date (
       id INT AUTO_INCREMENT PRIMARY KEY,
       date_type ENUM('shifted', 'faced', 'shelved', 'dusted', 'checked'),
       is_current_date BOOL,
       shifted_date DATETIME
);

CREATE TABLE book_range (
       id INT AUTO_INCREMENT PRIMARY KEY,
       parent_floor INT NOT NULL,
       FOREIGN KEY (parent_floor) REFERENCES floor(id) -- This is the floor
);

CREATE TABLE floor (
       id INT AUTO_INCREMENT PRIMARY KEY,
       floor_name VARCHAR(20)
);

CREATE TABLE shelf_column (
       id INT AUTO_INCREMENT PRIMARY KEY,
       start_call_number INT,
       end_call_number INT,
       last_shelved INT,
       last_shifted INT,
       last_faced INT,
       last_checked INT,
       last_dusted INT,
       parent_range INT,

       FOREIGN KEY (start_call_number) REFERENCES call_number(id),
       FOREIGN KEY (end_call_number)   REFERENCES call_number(id),
       FOREIGN KEY (last_shelved)      REFERENCES stacks_date(id), -- TODO Add checks to make sure that the referenced stacks_date has date_type('shelved')
       FOREIGN KEY (last_shifted)      REFERENCES stacks_date(id),
       FOREIGN KEY (last_faced)        REFERENCES stacks_date(id),
       FOREIGN KEY (last_checked)      REFERENCES stacks_date(id),
       FOREIGN KEY (last_dusted)       REFERENCES stacks_date(id),
       FOREIGN KEY (parent_range)      REFERENCES book_range(id)
);
