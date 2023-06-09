package com.team127.atom.mapper;

import com.team127.atom.model.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insert(Reply row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insertSelective(Reply row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    Reply selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKeySelective(Reply row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKeyWithBLOBs(Reply row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table reply
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKey(Reply row);

    List<Reply> selectReplyByUser(String userId);

    List<Reply> selectReplyByIssue(String issueId);

    List<Reply> selectPopularReplyByIssue(String issueId);

    int modifyReply(String replyId, String content);

    int starReply(String userId, String replyId);

    int unStarReply(String userId, String replyId);

    Reply selectReply(String issueId, Long createdAt);
}