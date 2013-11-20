package com.github.drinking_buddies.ui.comments;

import static com.github.drinking_buddies.jooq.Tables.REVIEW_COMMENT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jooq.DSLContext;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.Review;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.DateUtils;

import eu.webtoolkit.jwt.WContainerWidget;

public class ReviewCommentsWidget extends CommentsWidget {
    private Review review;
    
    public ReviewCommentsWidget(Review review, List<Comment> comments, User poster, WContainerWidget root) {
        super(comments, poster, root);
        this.review = review;
    }
    
    public ReviewCommentsWidget(Review review, List<Comment> comments, User poster) {
        this(review, comments, poster, null);
    }

    @Override
    protected void saveComment(Comment comment) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            DSLContext dsl = app.createDSLContext(conn);
            dsl
                        .insertInto(REVIEW_COMMENT, 
                                    REVIEW_COMMENT.REVIEW_ID,
                                    REVIEW_COMMENT.USER_ID,
                                    REVIEW_COMMENT.TEXT,
                                    REVIEW_COMMENT.POST_TIME)
                        .values(review.getId(), 
                                comment.getPoster().getId(), 
                                comment.getText(),
                                DateUtils.javaDateToSqliteFormat(comment.getPostDate()))
                        .execute();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
