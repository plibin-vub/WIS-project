package com.github.drinking_buddies.jooq.queries;

import static com.github.drinking_buddies.jooq.Tables.BAR2_BAR_SCORE;
import static com.github.drinking_buddies.jooq.Tables.BAR_SCORE;

import java.math.BigDecimal;

import org.jooq.DSLContext;

//Common bar queries that are used in different locations of the application
public class BarQueries {
    public static BigDecimal getAvgScore(DSLContext dsl, int id) {
        return dsl.select(BAR_SCORE.SCORE.avg())
            .from(BAR_SCORE)
            .join(BAR2_BAR_SCORE)
            .on(BAR2_BAR_SCORE.BAR_SCORE_ID.equal(BAR_SCORE.ID))
            .where(BAR2_BAR_SCORE.BAR_ID.eq(id))
            .fetchOne().getValue(BAR_SCORE.SCORE.avg());
    }
}
