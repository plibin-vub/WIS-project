package com.github.drinking_buddies.ui.comments;

import static com.github.drinking_buddies.jooq.Tables.BAR_COMMENT;
import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_COMMENT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jooq.DSLContext;

import com.github.drinking_buddies.Application;
import com.github.drinking_buddies.db.DBUtils;
import com.github.drinking_buddies.entities.Bar;
import com.github.drinking_buddies.entities.Comment;
import com.github.drinking_buddies.entities.User;
import com.github.drinking_buddies.ui.utils.DateUtils;

import eu.webtoolkit.jwt.WContainerWidget;

public class BarCommentsWidget extends CommentsWidget {

    private Bar bar;

    public BarCommentsWidget(Bar bar,List<Comment> comments, User poster,
            WContainerWidget root) {
        super(comments, poster, root);
        this.bar=bar;
    }

    @Override
    protected void saveComment(Comment comment) {
        Application app = Application.getInstance();
        Connection conn = null;
        try {
            conn = app.getConnection();
            
            DSLContext dsl = DBUtils.createDSLContext(conn);
            int id = dsl
                        .insertInto(BAR_COMMENT, 
                                BAR_COMMENT.USER_ID,
                                BAR_COMMENT.TEXT,
                                BAR_COMMENT.TIMESTAMP
                                )
                        .values(comment.getPoster().getId(), 
                                comment.getText(),
                                DateUtils.javaDateToSqliteFormat(comment.getPostDate())).returning(BAR_COMMENT.ID)
                        .execute();
            dsl.insertInto(BAR2_BAR_COMMENT,BAR2_BAR_COMMENT.BAR_COMMENT_ID,BAR2_BAR_COMMENT.BAR_ID)
                .values(id,bar.getId()).execute();
            
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
