/*
 * Copyright (c) 2023. Kaloyan Doychinov
 */

package tech.kaloyan.snackoverflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.kaloyan.snackoverflow.entity.User;
import tech.kaloyan.snackoverflow.exeception.NotAuthorizedException;
import tech.kaloyan.snackoverflow.mapper.CommentMapper;
import tech.kaloyan.snackoverflow.mapper.QuestionMapper;
import tech.kaloyan.snackoverflow.mapper.ReplyMapper;
import tech.kaloyan.snackoverflow.repository.UserRepository;
import tech.kaloyan.snackoverflow.resources.req.UserSignupReq;
import tech.kaloyan.snackoverflow.resources.resp.*;
import tech.kaloyan.snackoverflow.service.UserService;

import java.util.List;
import java.util.Optional;

import static tech.kaloyan.snackoverflow.mapper.UserMapper.MAPPER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    @Override
    public List<UserAccountResp> getAll() {
        return MAPPER.toUserRespList(userRepository.findAll());
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserAccountResp> getById(String id) {
        return userRepository.findById(id).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByUsername(String username) {
        return userRepository.findByUsername(username).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByEmail(String email) {
        return userRepository.findByEmail(email).map(MAPPER::toUserAccountResp);
    }

    @Override
    public Optional<UserAccountResp> getByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).map(MAPPER::toUserAccountResp);
    }

    @Override
    public UserResp save(UserSignupReq userReq) {
        User user = MAPPER.toUser(userReq);
        user.setPasshash(BCrypt.hashpw(userReq.getPassword(), BCrypt.gensalt()));
        return MAPPER.toUserResp(userRepository.save(user));
    }

    @Override
    public UserResp update(String id, UserSignupReq user, User currentUser) {
        if (!id.equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to update another user");
        }

        User userToUpdate = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );

        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            userToUpdate.setUsername(user.getUsername());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userToUpdate.setPasshash(passwordEncoder.encode(user.getPassword()));
        }

        return MAPPER.toUserResp(userRepository.save(userToUpdate));
    }

    @Override
    public void delete(String id, User currentUser) {
        if (!id.equals(currentUser.getId())) {
            throw new NotAuthorizedException("User is not authorized to delete another user");
        }

        userRepository.deleteById(id);
    }

    @Override
    public List<QuestionResp> getUserQuestions(String id) {
        Optional<User> user = this.getUserById(id);

        if (user.isPresent()) {
            return QuestionMapper.MAPPER.toQuestionResps(user.get().getQuestions());
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public List<CommentResp> getUserComments(String id) {
        Optional<User> user = this.getUserById(id);

        if (user.isPresent()) {
            return CommentMapper.MAPPER.toCommentResps(user.get().getComments());
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    public List<ReplyResp> getUserReplies(String id) {
        Optional<User> user = this.getUserById(id);

        if (user.isPresent()) {
            return ReplyMapper.MAPPER.toReplyResps(user.get().getReply());
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }
}
