package com.team127.atom.mapper;

import com.team127.atom.model.Repository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CollectionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insert(Repository row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int insertSelective(Repository row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    Repository selectByPrimaryKey(String id);

    /**
     * match the string with author or collection name
     * */
    List<Repository> selectByString(String word);


    List<Repository> selectUserStarRepo(String userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKeySelective(Repository row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table collection
     *
     * @mbg.generated Sun Mar 19 16:39:56 HKT 2023
     */
    int updateByPrimaryKey(Repository row);

    List<Repository> selectListByStars(Integer num);

    List<Repository> selectListByOwner(String ownerId);

    int selectStarNum(String id);

    int addUserStar(String userId, String collectionId);

    int removeUserStar(String userId, String collectionId);
}