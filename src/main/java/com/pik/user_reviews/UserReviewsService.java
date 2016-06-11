package com.pik.user_reviews;


import com.pik.account.AccountRepository;
import com.pik.security.TokenHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class UserReviewsService {
    private UserReviewsRepository userReviewsRepository;
    private AccountRepository accountRepository;
    private TokenHandler tokenHandler;

    public UserReviewsService(UserReviewsRepository userReviewsRepository, AccountRepository accountRepository,TokenHandler tokenHandler) {
        this.userReviewsRepository = userReviewsRepository;
        this.accountRepository = accountRepository;
        this.tokenHandler = tokenHandler;
    }

    List<ReviewDTO> getReviewsOfUser(String userLogin, int page) {
        Pageable pageable = new PageRequest(page, 20);
        List<Review> reviews = userReviewsRepository.findByReviewedLogin(userLogin, pageable);
        return reviews.stream().map(this::transformReviewToDTO).collect(Collectors.toList());
    }

    void postReview(String token, String reviewedUserLogin, String comment, int rating)
    {
        String userLogin = getLoginFromToken(token);

        if(userLogin == null) {
            throw new ReviewPostingException(ReviewPostingError.REVIEWING_USER_NOT_FOUND);
        }
        if(!userExists(reviewedUserLogin)) {
            throw new ReviewPostingException(ReviewPostingError.USER_TO_BE_REVIEWED_NOT_FOUND);
        }
        if(userIsRatingHimself(userLogin, reviewedUserLogin)){
            throw new ReviewPostingException(ReviewPostingError.USER_TRIED_TO_REVIEW_HIMSELF);
        }
        if(userReviewAlreadyExists(userLogin)){
            throw new ReviewPostingException(ReviewPostingError.USER_REVIEW_ALREADY_EXISTS);
        }
        if(!ratingIsValid(rating)) {
            throw new ReviewPostingException(ReviewPostingError.WRONG_RATING);
        }

        userReviewsRepository.save(new Review(reviewedUserLogin, userLogin, rating, comment));

    }


    private String getLoginFromToken(String token) {
        try {
            return tokenHandler.parseUserFromToken(token)
                    .getUsername();
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    private ReviewDTO transformReviewToDTO(Review review) {
        return new ReviewDTO(review.getReviewerLogin(), review.getComment(), review.getRating());
    }

    private boolean ratingIsValid(int rating) {
        return rating>0 && rating<6;
    }

    private boolean userReviewAlreadyExists(String login) {
        return !userReviewsRepository.findByReviewerLogin(login).isEmpty();
    }

    private boolean userIsRatingHimself(String login, String toReview) {
        return login.equals(toReview);
    }

    private boolean userExists(String login)
    {
        return accountRepository.findByLogin(login) != null;
    }
}
