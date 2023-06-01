package com.team127.atom.service;

import com.team127.atom.model.Repository;
import com.team127.atom.model.User;

import java.util.List;

public interface RepoService {

    Repository getRepoById(String id);

    /**
     * 模糊匹配
     * */
    List<Repository> getRepoByName(String word);

    /**
     * 根据收藏数排序推荐
     * */
    List<Repository> getReposByStars(int num);

    List<Repository> getReposByOwner(String ownerId);

    List<Repository> getUserStarRepos(String userId);

    boolean removeRepoById(String id);

    boolean updateRepoInfoById(Repository updated);

    boolean addNewRepo(Repository repo);

    int getStarNum(Repository repo);

    boolean starRepo(Repository repo, User user);

    boolean unStarRepo(Repository repo, User user);
}
