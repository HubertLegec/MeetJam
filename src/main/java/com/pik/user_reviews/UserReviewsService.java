package com.pik.user_reviews;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class UserReviewsService {
    private UserReviewsRepository userReviewsRepository;

    public UserReviewsService(UserReviewsRepository userReviewsRepository) {
        this.userReviewsRepository = userReviewsRepository;
    }

    public List<ReviewDTO> getReviewsOfUser(String userLogin, int page) {
        Pageable pageable = new PageRequest(page, 20);
        List<Review> reviews = userReviewsRepository.findByReviewedLogin(userLogin, pageable);
        return reviews.stream().map(this::transformReviewToDTO).collect(Collectors.toList());
    }

    private ReviewDTO transformReviewToDTO(Review review) {
        return new ReviewDTO(review.getReviewerLogin(), review.getComment(), review.getRating());
    }
}
