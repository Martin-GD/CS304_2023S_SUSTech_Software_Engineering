package com.team127.atom.mapper;

import com.team127.atom.model.Tag;
import com.team127.atom.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int delete(@Param("id") String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insert(@Param("tag") Tag row, @Param("userId") String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insertSelective(Tag row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    Tag selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKeySelective(Tag row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKeyWithBLOBs(Tag row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKey(Tag row);

    List<Tag> selectAllTags(String userId);

    List<Tag> selectRepoTags(String repoId);

    List<Tag> selectNoteTags(String noteId);

    int addRepoTag(String repoId, String tagId);

    int removeRepoTag(String repoId, String tagId);

    int addNoteTag(String noteId, String tagId);

    int removeNoteTag(String noteId, String tagId);
}