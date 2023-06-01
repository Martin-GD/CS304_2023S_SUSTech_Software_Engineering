package com.team127.atom.web.controller;

import com.team127.atom.model.*;

import com.team127.atom.service.RepoService;
import com.team127.atom.service.TagService;
import com.team127.atom.service.UserService;
import com.team127.atom.util.HashGenerator;
import com.team127.atom.util.JWTUtil;

import java.util.ArrayList;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class RepositoryController {
    @Autowired
    RepoService repoService;
    @Autowired
    TagService tagService;

    @Autowired
    UserService userService;

    @PostMapping("/addRepository")
    public Response addRepository(@RequestParam("name") String name,
                                  @RequestParam("token") String token,
                                  @RequestParam("auth") int auth,
                                  @RequestBody List<String> tags) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        Repository repository = new Repository();
        repository.setName(name);
        repository.setOwnerId(user.getId());
        repository.setAuth(auth);
        // 用hash值验证权限
        repository.setId(
            HashGenerator.hash(name, user.getId()));
        boolean res = true;
        if (repoService.getRepoById(repository.getId()) != null) {
            return Response.error(0, "The repository name has exist!");
        } else {
            res = repoService.addNewRepo(repository);
        }
        if (res) {
            for (String tag : tags) {
                String tagId = HashGenerator.hash(tag, user.getId());
                Tag tagDB = tagService.getTag(tagId);
                if (tagDB == null) {
                    Tag newTag = new Tag(tagId, tag, "0x0000", "0000", user.getId());
                    tagService.createTag(newTag, user.getId());
                    tagService.addRepoTag(repository.getId(), newTag.getId());
                }else{
                    tagService.addRepoTag(repository.getId(), tagId);
                }
            }
            return Response.success(null);
        } else {
            return Response.error(401, "Fail to add a repository.");
        }
    }

    @PostMapping("/modifyRepository")
    public Response modifyRepository(@RequestBody String name,
                                     @RequestBody String repoId,
                                     @RequestBody String token,
                                     @RequestBody int auth) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Repository repo = repoService.getRepoById(repoId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !Objects.equals(repo.getOwnerId(), user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        Repository repository = new Repository();
        repository.setName(name);
        repository.setOwnerId(user.getId());
        repository.setAuth(auth);
        repository.setId(repoId);
        boolean res = true;
        if (repoService.getRepoById(repository.getId()) == null) {
            return Response.error(0, "The repository doesn't exist!");
        } else {
            res = repoService.updateRepoInfoById(repository);
        }
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(0, "Fail to modify the repository.");
        }
    }

    @PostMapping("/deleteRepository")
    public Response deleteRepository(@RequestParam("token") String token, @RequestParam("repoId") String repoId) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        Repository repo = repoService.getRepoById(repoId);
        if (!JWTUtil.verify(token, user.getId(), user.getPassword()) ||
            !Objects.equals(repo.getOwnerId(), user.getId())) {
            return Response.error(1004, "Token verification failed");
        }
        Repository repository = new Repository();
        repository.setId(repoId);
        boolean res = repoService.removeRepoById(repository.getId());
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(0, "Fail to delete repository.");
        }
    }

    @GetMapping("/getRepositoryList")
    public Response getRepositories(@RequestParam("token") String token, @RequestParam("user") String ownerId) {
        String s = JWTUtil.getUserId(token);
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        System.out.println("hello " + ownerId);
        List<Repository> list = repoService.getReposByOwner(ownerId);
        list.removeIf(repository -> !repository.getOwnerId().equals(user.getId()) && repository.getAuth() == 2);
        List<RepoWithTags> result = new ArrayList<>();
        for (Repository repo: list){
            List<Tag> tags = tagService.getRepoTags(repo.getId());
            RepoWithTags tem = new RepoWithTags(repo, tags);
            tem.setOwnerName(userService.getUserById(tem.getOwnerName()).getName());
            result.add(tem);
        }
        return Response.success(result);
    }


    @PostMapping("/getRCRepositories")
    public Response getPopularRepositories() {
        // 获取点赞数前5的笔记仓库
        List<Repository> list = repoService.getReposByStars(5);
        list.removeIf(repository -> repository.getAuth() == 2);
        return Response.success(list);
    }

    @GetMapping("/getStarRepositoryList")
    public Response getStarRepository(@RequestParam("token") String token) {
        User user = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, user.getId(), user.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        List<Repository> res = repoService.getUserStarRepos(user.getId());
        res.removeIf(repository -> !repository.getOwnerId().equals(user.getId()) && repository.getAuth() == 2);
        List<RepoWithTags> result = new ArrayList<>();
        for (Repository repo: res){
            List<Tag> tags = tagService.getRepoTags(repo.getId());
            RepoWithTags tem = new RepoWithTags(repo, tags);
            tem.setOwnerName(userService.getUserById(tem.getOwnerName()).getName());
            result.add(tem);
        }
        return Response.success(result);
    }

    @PostMapping("/starRepository")
    public Response starRespository(@RequestParam("repoId") String repoId,
                                    @RequestParam("token") String token) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        Repository repository = new Repository();
        repository.setId(repoId);
        User user = new User();
        user.setId(userDB.getId());
        boolean res = repoService.starRepo(repository, user);
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(0, "Fail to star the repository.");
        }
    }


    @GetMapping("/checkRepoStar")
    public Response checkRepoStar(@RequestParam("token") String token,
                                  @RequestParam("repoId") String repoId){
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        List<Repository> list = repoService.getUserStarRepos(userDB.getId());
        Long count = list.stream().filter(o->o.getId().equals(repoId)).count();
        if (count > 0){
            return Response.success("true");
        } else {
            return Response.success("false");
        }
    }

    @PostMapping("/unStarRepository")
    public Response unStarRepository(@RequestParam("repoId") String repoId,
                                     @RequestParam("token") String token) {
        User userDB = userService.getUserById(JWTUtil.getUserId(token));
        if (!JWTUtil.verify(token, userDB.getId(), userDB.getPassword())) {
            return Response.error(1004, "Token verification failed");
        }
        Repository repository = new Repository();
        repository.setId(repoId);
        User user = new User();
        user.setId(userDB.getId());
        boolean res = repoService.unStarRepo(repository, user);
        if (res) {
            return Response.success(null);
        } else {
            return Response.error(0, "Fail to unsubscribe the repository.");
        }
    }
}
