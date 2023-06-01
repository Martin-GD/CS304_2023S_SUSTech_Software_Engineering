package com.team127.atom.service.impl;

import com.team127.atom.mapper.CollectionMapper;
import com.team127.atom.model.Repository;
import com.team127.atom.model.User;
import com.team127.atom.service.RepoService;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RepoServiceImpl implements RepoService {
    @Autowired
    CollectionMapper collectionMapper;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Repository getRepoById(String id) {
        Repository res;
        String key = "Collection" + id;
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            res = collectionMapper.selectByPrimaryKey(id);
            if (res != null) {
                redisTemplate.opsForValue().set(key, res, 1, TimeUnit.HOURS);
            }
        } else {
            res = (Repository) redisTemplate.opsForValue().get(key);
        }
        return res;
    }

    @Override
    public List<Repository> getRepoByName(String word) {
        word = "%" + word.toLowerCase() + "%";
        return collectionMapper.selectByString(word);
    }

    @Override
    public List<Repository> getReposByStars(int num) {
        return collectionMapper.selectListByStars(num);
    }

    @Override
    public List<Repository> getReposByOwner(String ownerId) {
        return collectionMapper.selectListByOwner(ownerId);
    }

    @Override
    public List<Repository> getUserStarRepos(String userId) {
        return collectionMapper.selectUserStarRepo(userId);
    }


    @Override
    public boolean removeRepoById(String id) {
        int signal = collectionMapper.deleteByPrimaryKey(id);
        redisTemplate.delete("Collection"+id);
        return signal == 1;
    }

    @Override
    public boolean updateRepoInfoById(Repository updated) {
        int signal = collectionMapper.updateByPrimaryKeySelective(updated);
        redisTemplate.delete("Collection"+updated.getId());
        return signal == 1;
    }

    @Override
    public boolean addNewRepo(Repository repo) {
        return collectionMapper.insert(repo) == 1;
    }

    @Override
    public int getStarNum(Repository repo) {
        int num = 0;
        String key = "Star"+repo.getId();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))){
            num = collectionMapper.selectStarNum(repo.getId());
            redisTemplate.opsForValue().set(key, num);
        } else {
            String cache = (String) redisTemplate.opsForValue().get(key);
            assert cache != null;
            num = Integer.parseInt(cache);
        }
        return num;
    }

    @Override
    public boolean starRepo(Repository repo, User user) {
        int res = collectionMapper.addUserStar(user.getId(), repo.getId());
        redisTemplate.delete("Star"+repo.getId());
        return res == 1;
    }

    @Override
    public boolean unStarRepo(Repository repo, User user) {
        int res = collectionMapper.removeUserStar(user.getId(), repo.getId());
        redisTemplate.delete("Star"+repo.getId());
        return res == 1;
    }
}
