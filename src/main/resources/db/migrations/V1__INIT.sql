/*
 * Copyright (c) 2024. Kaloyan Doychinov
 */

ALTER TABLE comment
    MODIFY content LONGTEXT;

ALTER TABLE comment_aud
    MODIFY content VARCHAR;

ALTER TABLE question
    MODIFY `description` LONGTEXT;

ALTER TABLE question_aud
    MODIFY `description` VARCHAR;